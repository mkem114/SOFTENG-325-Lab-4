package nz.ac.auckland.hello.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Simple mapped (persistent) class. 
 * 
 * Any persistent class must minimally be annotated with @Entity.
 *
 */
@Entity
public class Message {
	
	// A persistent class must have an identifier attribute, annotated using 
	// @Id. This attribute is mapped to the primary key column named ID of the
	// MESSAGE table.
	// @GeneratedValue causes the database to generate the value for _id.
	@Id
	@GeneratedValue
	private Long _id;
	
	private String _text;
	
	// A default constructor is required for persistent classes. Since there is
	// no non-default constructor for Message, this class implicitly has a 
	// default constructor.
	
	
	public String getText() {
		return _text;
	}
	
	public void setText(String text) {
		_text = text;
	}
	
	@Override
	public String toString() {
		return "Message: id=" + _id + ", text=" + _text;
	}
}
