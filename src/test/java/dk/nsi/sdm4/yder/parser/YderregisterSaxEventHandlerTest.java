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

import static dk.nsi.sdm4.yder.recordspecs.YderregisterRecordSpecs.PERSON_RECORD_TYPE;
import static dk.nsi.sdm4.yder.recordspecs.YderregisterRecordSpecs.YDER_RECORD_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import dk.nsi.sdm4.core.parser.ParserException;
import dk.nsi.sdm4.core.persistence.recordpersister.Record;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordBuilder;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordPersister;
import dk.nsi.sdm4.yder.YderTestConfiguration;
import dk.nsi.sdm4.yder.recordspecs.YderregisterRecordSpecs;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class YderregisterSaxEventHandlerTest
{
    public static final String RECIPIENT_ID = "F053";
    private final String INTERFACE_ID = "S1040025";
    private final String ROOT_TAG = "etds1040025XML";
    private final String END_TAG = "Slut";
    private final String START_TAG = "Start";
    private final String YDER_TAG = "Yder";
    private final String PERSON_TAG = "Person";

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
    RecordPersister persister;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private YderregisterSaxEventHandler eventHandler;

    @Before
    public void setUp() throws Exception
    {
        persister = mock(RecordPersister.class);
        eventHandler = new YderregisterSaxEventHandler(persister, jdbcTemplate);
    }

    @Test(expected = ParserException.class)
    public void testThatIfTheNumberOfRecordsFromTheEndRecordDifferThenThrowAnException() throws SAXException
    {
        eventHandler.startElement(null, null, ROOT_TAG, null);

        writeStartElement(INTERFACE_ID, RECIPIENT_ID, "00001");

        writeRecordElement(createYderRecord("0433514B17DFC5BF"));

        writeEndElement("2");

        eventHandler.endElement(null, null, ROOT_TAG);
    }

    @Test
    public void testThatTheExpectedRecordsArePassedToThePersister() throws SAXException, SQLException
    {
        eventHandler.startElement(null, null, ROOT_TAG, null);

        writeStartElement(INTERFACE_ID, RECIPIENT_ID, "00001");

        Record yderRecord = createYderRecord("0433514B17DFC5BF");
        Record person1 = createPersonRecord("C31C77B63CDDC5BF");
        Record person2 = createPersonRecord("030182BC3CDDC5BF");

        writeRecordElement(yderRecord, person1, person2);

        writeEndElement("3");

        eventHandler.endElement(null, null, ROOT_TAG);

        persister.persist(yderRecord, YDER_RECORD_TYPE);
        persister.persist(person1, PERSON_RECORD_TYPE);
        persister.persist(person2, PERSON_RECORD_TYPE);
    }

    @Test(expected = ParserException.class)
    public void testThrowsExceptionIfTheWrongReceiverId() throws SAXException
    {
        eventHandler.startElement(null, null, ROOT_TAG, null);

        writeStartElement(INTERFACE_ID, "B012", "00001");
    }

    @Test(expected = ParserException.class)
    public void testThrowsExceptionIfTheWrongInterfaceId() throws SAXException
    {
        eventHandler.startElement(null, null, ROOT_TAG, null);

        writeStartElement("S2140021", RECIPIENT_ID, "00001");
    }
    
    @Test
    public void testVersionNumberIsExtracted() throws SAXException
    {
        eventHandler.startElement(null, null, ROOT_TAG, null);

        writeStartElement(INTERFACE_ID, RECIPIENT_ID, "00032");
        
        writeEndElement("0");

        eventHandler.endElement(null, null, ROOT_TAG);
        
        assertEquals(eventHandler.GetVersionFromFileSet(), "00032");
    }

    //
    // Helpers
    //

    public void writeStartElement(String interfaceId, String recipientId, String version) throws SAXException
    {
        eventHandler.startElement(null, null, START_TAG, createAttributes("SnitfladeId", interfaceId, "Modt", recipientId, "OpgDato", version));
        eventHandler.endElement(null, null, START_TAG);
    }

    public void writeEndElement(String recordCount) throws SAXException
    {
        eventHandler.startElement(null, null, END_TAG, createAttributes("AntPost", recordCount));
        eventHandler.endElement(null, null, END_TAG);
    }

    public Record createYderRecord(String histId)
    {
        return new RecordBuilder(YderregisterRecordSpecs.YDER_RECORD_TYPE)
		.field("HistIdYder", histId)
		.field("AmtKodeYder", "84")
		.field("AmtTxtYder", "Region Hovedstaden")
		.field("YdernrYder", "123456")
		.field("PrakBetegn", "PrakBetegn")
		.field("AdrYder", "R. Hougårds Vej 1")
		.field("PostnrYder", "8960")
		.field("PostdistYder", "Randers SØ")
		.field("AfgDatoYder", "")
		.field("TilgDatoYder", "19910101")
		.field("HvdSpecKode", "80")
		.field("HvdSpecTxt", "Almen lægegerning")
		.field("HvdTlf", "12345678")
		.field("EmailYder", "1@2.dk")
		.field("WWW", "www.2.dk")
		.build();
    }

    public Record createPersonRecord(String histId)
    {
        return new RecordBuilder(YderregisterRecordSpecs.PERSON_RECORD_TYPE)
		.field("HistIdPerson", histId)
		.field("YdernrPerson", "034835")
		.field("CprNr", "1508823378")
        .field("TilgDatoPerson", "19910101")
        .field("AfgDatoPerson", "")
        .field("PersonrolleKode", "26")
        .field("PersonrolleTxt", "Sygeplejerske")
        .build();
    }

    public void writeRecordElement(Record yderRecord, Record ... personRecords) throws SAXException
    {
        eventHandler.startElement(null, null, YDER_TAG, createAttributes(yderRecord));
        
        for(Record personRecord: personRecords)
        {
            eventHandler.startElement(null, null, PERSON_TAG, createAttributes(personRecord));
            eventHandler.endElement(null, null, PERSON_TAG);
        }
        
        eventHandler.endElement(null, null, YDER_TAG);
    }

    public Attributes createAttributes(Record record)
    {
        Map<String, String> map = new HashMap<String, String>();
        for(Map.Entry<String, Object> entry : record.fields())
        {
            map.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return createAttributes(map);
    }


    public Attributes createAttributes(String... namesAndValues)
    {
        return createAttributes(createMap(namesAndValues));
    }

    public Map<String, String> createMap(String... keysAndValues)
    {
        assertTrue(keysAndValues.length % 2 == 0);

        Map<String, String> map = Maps.newHashMap();
        for(int i = 0; i < keysAndValues.length; i += 2)
        {
            map.put(keysAndValues[i], keysAndValues[i+1]);
        }

        return map;
    }

    public Attributes createAttributes(final Map<String, String> values)
    {
        Attributes attributes = mock(Attributes.class);

        when(attributes.getValue(any(String.class))).thenAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return values.get(invocation.getArguments()[0]);
            }
        });

        return attributes;
    }
}
