package com.tecweb.entidades;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.stereotype.Component;


	@Entity
	@Component
	@Table(name = "User")

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id_user;
	
	@Column(name = "login_name", nullable = false)
	private String login_name;
	
	@Column(name = "display_name",nullable = true)
	private String display_name;
	
	@Column(name = "pass_hash", nullable = true)
	private String pass_hash;
	
	public User() {
	}
	
	public User(int id_user, String login_name, String display_name,String pass_hash) {
		this.id_user = id_user;
		this.login_name = login_name;
		this.display_name = display_name;
		this.pass_hash = pass_hash;
	}

	public int getID() {
		return id_user;
	}

	public void setID(int id_user) {
		this.id_user = id_user;
	}

	public String getLoginName() {
		return login_name;
	}

	public void setLoginName(String login_name) {
		this.login_name = login_name;
	}

	public String getDisplayName() {
		return display_name;
	}

	public void setDisplayName(String display_name) {
		this.display_name = display_name;
	}

	public String getPassHash() {
		return pass_hash;
	}

	public void setPassHash(String pass_hash) {
		this.pass_hash = pass_hash;
	}
	
}

	