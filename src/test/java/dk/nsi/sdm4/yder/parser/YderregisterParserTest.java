/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.sdm4.yder.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.joda.time.Instant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import dk.nsi.sdm4.core.parser.OutOfSequenceException;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordPersister;
import dk.nsi.sdm4.yder.YderTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class YderregisterParserTest {

    @Configuration
    @PropertySource("classpath:test.properties")
    @Import(YderTestConfiguration.class)
    static class ContextConfiguration {
        @Bean
        public YderregisterParser parser() {
            return new YderregisterParser();
        }

        @Bean
        public RecordPersister persister() {
            return new RecordPersister(Instant.now());
        }

    }

    @Autowired
    YderregisterParser parser;
    
    @Autowired
    RecordPersister persister;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test(expected = OutOfSequenceException.class)
    public void testImportingOldVersionWillResultInAnOutOfSequenceException() {
        String sql = "insert into YderregisterKeyValue (`key`, value) values ('Yderregister_version', '20120104')";
        jdbcTemplate.update(sql);
        
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/yderregister/csc"));
        parser.process(fileSet);
        
    }

    @Test(expected = OutOfSequenceException.class)
    public void testImportingCurrentVersionResultInAnOutOfSequenceException() throws Exception {
        String sql = "insert into YderregisterKeyValue (`key`, value) values ('Yderregister_version', '20120103')";
        jdbcTemplate.update(sql);
        
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/yderregister/csc"));
        parser.process(fileSet);
    }

    @Test
    public void testThatFileContentAreParsed() throws Exception {
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/yderregister/csc"));
        parser.process(fileSet);
        
        assertEquals(58, jdbcTemplate.queryForInt("select count(*) from Yderregister"));
        assertEquals(54, jdbcTemplate.queryForInt("select count(*) from YderregisterPerson"));
    }
}
