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
package dk.nsi.sdm4.yder;


import com.mysql.jdbc.Driver;
import dk.nsi.sdm4.core.persistence.migration.DbMigrator;
import dk.sdsd.nsp.slalog.api.SLALogger;
import dk.sdsd.nsp.slalog.impl.SLALoggerDummyImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class YderTestConfiguration {
	@Value("${test.mysql.port}")
	private int mysqlPort;
	private String testDbName = "sdm_warehouse_yder_test";
	private String db_username = "root";
	private String db_password = "papkasse";

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties(){
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public DataSource dataSource() throws Exception{
		String jdbcUrlPrefix = "jdbc:mysql://127.0.0.1:" + mysqlPort + "/";

		// we need to make sure we have an empty database. We do it by dropping and creating it, but we can't do that
		// from a datasource connected to the database we want to drop/create, so lets's connect to the mysql db first
		long startMillis = System.currentTimeMillis();
		DataSource adminDs = new SimpleDriverDataSource(new Driver(), jdbcUrlPrefix + "mysql", db_username, db_password);
		JdbcTemplate adminJdbc = new JdbcTemplate(adminDs);
		adminJdbc.update("DROP DATABASE IF EXISTS " + testDbName); // will be created automatically by the "normal" datasource
		System.out.println("Drop db took " + (System.currentTimeMillis()-startMillis) + " ms");

		return new SimpleDriverDataSource(new Driver(), jdbcUrlPrefix + testDbName + "?createDatabaseIfNotExist=true", db_username, db_password);
	}

	@Bean(initMethod = "migrate")
	public DbMigrator dbMigrator() {
		return new DbMigrator();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}

	@Bean
	public SLALogger slaLogger() {
		return new SLALoggerDummyImpl();
	}
}
