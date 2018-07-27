package nz.ac.auckland.hello.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class to demonstrate use of JPA's EntityManager for mediating access to
 * a relational database.  
 *
 */
public class HelloWorldJpaTest {

	private static Logger _logger = LoggerFactory.getLogger(HelloWorldJpaTest.class);
	
	private EntityManagerFactory entityManagerFactory;

	/**
	 * Runs before each test to create a new EntityManagerFactory.
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		// Create an EntityManagerFactory from which an EntityManager can be 
		// requested. The argument to createEntityManagerFactory() is the name
		// of a persistence unit, named in META-INF/persistence.xml. The 
		// factory configures itself based on reading the xml file. 
		// persistence.xml must contain a persistence unit named, in this case,
		// "nz.ac.auckland.hello".
		entityManagerFactory = Persistence.createEntityManagerFactory("nz.ac.auckland.hello");
	}

	/**
	 * Runs after each test to destroy the EntityManagerFactory.
	 * 
	 */
	@After
	public void tearDown() throws Exception {
		// Close the EntityManagerFactory once all tests have executed.
		entityManagerFactory.close();
	}
	
	/**
	 * Runs after all tests have executed to print the contents of the 
	 * database.
	 */
	@AfterClass
	public static void dumpDatabase() throws Exception {
		Set<String> tableNames = new HashSet<String>();
		tableNames.add("MESSAGE");
		DatabaseUtility.openDatabase();
		DatabaseUtility.dumpDatabase(tableNames);
		DatabaseUtility.closeDatabase();
	}
	
	/**
	 * Illustrates use of the JPA EntityManager. This test uses a transaction 
	 * to store a Message object in the database. It then uses another
	 * transaction to query the database for Messages. It prints the Message,
	 * changes its state and updates the database. 
	 */
	@Test
	public void testBasicUsage() {
		// Acquire an EntityManager, representing a session with the database.
		// Using the entityManager, create a transaction.
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		
		// Create a Message.
		Message message = new Message();
		message.setText("Hello, World!");
		
		// Request the the EntityManager stores the Message.
		entityManager.persist(message);
		
		// Commit the transaction. This causes the JPA provider to execute the
		// SQL statement: 
		//   insert into MESSAGE (ID, TEXT) values (1, 'Hello, World!')
		entityManager.getTransaction().commit();

		// Now let's pull the Message from the database. Start a new 
		// transaction. 
		entityManager.getTransaction().begin();
		
		// Query the database for stored Messages. The query is expressed using
		// JPQL (Java Persistence Query Language) which looks similar to SQL. 
		// Rather than being written in terms of tables and columns, JPQL 
		// queries are written in terms of classes and properties. This JPQL
		// query generates the SQL query: select * from MESSAGE.
        List<Message> messages = entityManager.createQuery("select m from Message m", Message.class).getResultList();
		for (Message m : messages ) {
			_logger.info("Message: " + m);
		}
		// They query should return one Message object.
		assertEquals(1, messages.size());
		
		// The text of the returned Message should be what was originally 
		// persisted.
		Message retrievedMessage = messages.get(0);
		assertEquals("Hello, World!", retrievedMessage.getText());
		
		// The query actually returns a reference to the original Message
		// object. This is because the persistence context is managing the
		// original object. If a query returns an object that is already
		// managed by the persistence context, a separate copy of the object
		// isn't made.
		assertSame(message, retrievedMessage);
		
		// Change the content of the message, and update the database.
		messages.get(0).setText("Take me to your leader!");
		
		// When this transaction commits, the following SQL is executed:
		//   update MESSAGE set TEXT = 'Take me to your leader!' where ID = 1
        entityManager.getTransaction().commit();
        entityManager.close();
	}
}
