package br.edu.ifpb.praticas.teste;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by diogomoreira on 17/07/16.
 */
public abstract class GenericDatabaseTestCase {

    private IDatabaseTester databaseTester;

    @Before
    public void defaultSetUp() throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream((getDataSetFile())));
        Properties prop = new Properties();
        prop.load(new FileInputStream(getClass().getClassLoader().getResource("config.properties").getFile()));
        databaseTester = new JdbcDatabaseTester(prop.getProperty("db.class"),
                prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"));
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();
    }

    public abstract String getDataSetFile();

    @After
    public void defaultTearDown() throws Exception {
        databaseTester.onTearDown();
    }

}
