package nz.ac.auckland.concert.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import nz.ac.auckland.concert.jpa.LocalDateTimeConverter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * Class to represent a Concert. A Concert is characterised by an unique ID, 
 * title, date and time, and a featuring Performer.
 * 
 * Concert implements Comparable with a natural ordering based on its title.
 * Hence, in a List, Concert instances can be sorted into alphabetical order
 * based on their title value.
 *
 */
@XmlRootElement//
@Entity//
@XmlAccessorType(XmlAccessType.FIELD)//
public class Concert implements Comparable<Concert> {

	@Id//
	@GeneratedValue//
	private Long _id;

	private String _title;

	@Convert(converter = LocalDateTimeConverter.class)//
	private LocalDateTime _date;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})//
	@JoinColumn(name = "PID")//
	private Performer _performer;
	
	public Concert(Long id, String title, LocalDateTime date, Performer performer) {
		_id = id;
		_title = title;
		_date = date;
		_performer = performer;
	}
	
	public Concert(String title, LocalDateTime date, Performer performer) {
		this(null, title, date, performer);
	}
	
	// Required for JPA and JAXB.
	protected Concert() {}
	
	public Long getId() {
		return _id;
	}
	
	public void setId(Long id) {
		_id = id;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public LocalDateTime getDate() {
		return _date;
	}
	
	public void setDate(LocalDateTime date) {
		_date = date;
	}

	public Performer getPerformer() {
		return _performer;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Concert, id: ");
		buffer.append(_id);
		buffer.append(", title: ");
		buffer.append(_title);
		buffer.append(", date: ");
		buffer.append(_date.toString());
		buffer.append(", featuring: ");
		buffer.append(_performer.getName());
		
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
            append(_title, rhs.getTitle()).
            append(_date, rhs.getDate()).
            isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). 
	            append(_title).hashCode();
	}

	@Override
	public int compareTo(Concert concert) {
		return _title.compareTo(concert.getTitle());
	}
}
