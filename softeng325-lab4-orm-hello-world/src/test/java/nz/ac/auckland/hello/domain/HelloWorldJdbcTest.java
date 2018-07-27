package nz.ac.auckland.hello.domain;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloWorldJdbcTest {
	
	private static final String DROP_TABLE = 
			"drop table if exists JDBC_MESSAGES";
	
	private static final String CREATE_TABLE = 
			"create table JDBC_MESSAGES (" +
			"ID BIGINT not null primary key," +
			"TEXT varchar(40));";
	
	private static final String INSERT_MESSAGE =
			"insert into JDBC_MESSAGES values(1, 'Hello, World!');";
	
	private static final String QUERY_MESSAGE = 
			"select * from JDBC_MESSAGES";
	
	private static final String UPDATE_MESSAGE = 
			"update JDBC_MESSAGES set TEXT = 'Take me to your leader!' where ID = 1;";

	// Set up database table.
	@BeforeClass
	public static void setUp() throws SQLException, ClassNotFoundException {
		DatabaseUtility.openDatabase();
		DatabaseUtility.executeStatement(DROP_TABLE);
		DatabaseUtility.executeStatement(CREATE_TABLE);
	}
	
	@AfterClass
	public static void tearDown() throws SQLException, ClassNotFoundException {
		Set<String> tableNames = new HashSet<String>();
		tableNames.add("JDBC_MESSAGES");
		
		DatabaseUtility.dumpDatabase(tableNames);
		DatabaseUtility.closeDatabase();
	}
	
	@Test
	public void testBasicUsage() throws SQLException, ClassNotFoundException {
		// Store Message text in the database.
		DatabaseUtility.executeStatement(INSERT_MESSAGE);
		
		// Execute a query to return all rows from the MESSAGES table.
		Message message = queryMessage();
		assertEquals("Hello, World!", message.getText());
		
		// Execute an update statement to change the Message's text in the 
		// database.
		DatabaseUtility.executeStatement(UPDATE_MESSAGE);
		
		// Requery the database to see if the row was updated.
		message = queryMessage();
		assertEquals("Take me to your leader!", message.getText());
	}
	
	protected Message queryMessage() throws SQLException, ClassNotFoundException {
		Message message = new Message();
		ResultSet resultSet = DatabaseUtility.executeQuery(QUERY_MESSAGE);
		
		int messageCount = 0;
		while (resultSet.next()) {
			messageCount++;
            int id = resultSet.getInt("ID");
            String text = resultSet.getString("TEXT");

            // Initialise the Message object based on the row data.
            message.setText(text);
        }
		// Ensure that the query returned a single result.
		assertEquals(1, messageCount);
		
		return message;
	}

}
