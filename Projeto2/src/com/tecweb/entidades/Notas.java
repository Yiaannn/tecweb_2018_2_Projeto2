package com.tecweb.entidades;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Component
@Table(name = "Note")

public class Notas {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id_notes;
	
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
	
	public Notas() {
		
	}
	public Notas(Long id_notes, String message_body, Date creation_date, Date expiry_date, int priority_level, boolean is_active,int id_owner) {
		
		this.id_notes = id_notes;
		this.message_body = message_body;
		this.creation_date = creation_date;
		this.expiry_date = expiry_date;
		this.priority_level = priority_level;
		this.is_active = is_active;
		this.id_owner = id_owner;
	}
	
	public Long getId_notes() {
		return id_notes;
	}
	public void setId_notes(Long id_notes) {
		this.id_notes = id_notes;
	}
	
	public String getMessage_body() {
		return message_body;
	}
	public void setMessage_body(String message_body) {
		this.message_body = message_body;
	}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	public Date getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}
	
	public int getPriority_level() {
		return priority_level;
	}
	public void setPriority_level(int priority_level) {
		this.priority_level = priority_level;
	}
	
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	
	public int getId_owner() {
		return id_owner;
	}
	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}
	
	
}