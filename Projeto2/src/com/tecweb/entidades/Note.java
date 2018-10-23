package com.tecweb.entidades;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "Note")

public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id_note;
	
	@Column(name = "message_body", nullable = true)
	private String message_body;
	
	@Column(name = "creation_date", nullable = false)
	private Date creation_date;
	
	@Column(name = "expiry_date", nullable = true)
	private Date expiry_date;
	
	@Column(name = "priority_level", nullable = false)
	private int priority_level;
	
	@Column(name = "is_active", nullable = false)
	private boolean is_active;
	
	@Column(name = "id_owner", nullable = false)
	private int id_owner;
	
	public Note() {
		
	}
	public Note(int id_note, String message_body, Date creation_date, Date expiry_date, int priority_level, boolean is_active,int id_owner) {
		
		this.id_note = id_note;
		this.message_body = message_body;
		this.creation_date = creation_date;
		this.expiry_date = expiry_date;
		this.priority_level = priority_level;
		this.is_active = is_active;
		this.id_owner = id_owner;
	}
	
	public int getID() {
		return id_note;
	}
	public void setID(int id_note) {
		this.id_note = id_note;
	}
	
	public String getMessageBody() {
		return message_body;
	}
	public void setMessageBody(String message_body) {
		this.message_body = message_body;
	}
	
	
	public Date getCreationDate() {
		return creation_date;
	}
	public void setCreationDate(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	public Date getExpiryDate() {
		return expiry_date;
	}
	public void setExpiryDate(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	
	public int getPriorityLevel() {
		return priority_level;
	}
	public void setPriorityLevel(int priority_level) {
		this.priority_level = priority_level;
	}
	
	public boolean getIsActive() {
		return is_active;
	}
	public void setIsActive(boolean is_active) {
		this.is_active = is_active;
	}
	
	public int getIdOwner() {
		return id_owner;
	}
	public void setIdOwner(int id_owner) {
		this.id_owner = id_owner;
	}
	
	
}