package com.tecweb.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tecweb.entidades.*;

public class DAO {

	private Connection connection = null;
	
	private final String AUTHCOOKIENAME= "auth";
	
	public DAO(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection= DriverManager.getConnection("jdbc:mysql://localhost/tecwebprojeto1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "12063cb5d72571662926aa355c97abc8a28c87f3");
			/*
			connection= DriverManager.getConnection("jdbc:mysql://localhost/tecwebprojeto1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "130879");
			*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Note> getActiveNoteList(User user){
		List<Note> notes= new ArrayList<Note>();
		
		PreparedStatement stmt;
		ResultSet rs;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Note WHERE id_owner=? ORDER BY priority_level DESC");
			stmt.setInt(1, user.getID() );
			rs = stmt.executeQuery();
			boolean is_active;
			while( rs.next()){
				is_active= rs.getBoolean("is_active");
				if(is_active){
					Note note= new Note();
					note.setMessageBody(rs.getString("message_body"));
					note.setCreationDate(rs.getDate("creation_date"));
					note.setExpiryDate(rs.getDate("expiry_date"));
					note.setPriorityLevel(rs.getInt("priority_level"));
					note.setID(rs.getInt("id_notes"));
					
					notes.add(note);
				}
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notes;
	}
	
	public List<Note> getArchivedNoteList(User user){
		List<Note> notes= new ArrayList<Note>();
		
		PreparedStatement stmt;
		ResultSet rs;
		try {
			stmt = connection.prepareStatement("SELECT * FROM Note WHERE id_owner=?");
			stmt.setInt(1, user.getID() );
			rs = stmt.executeQuery();
			boolean is_active;
			while( rs.next()){
				is_active= rs.getBoolean("is_active");
				if(!is_active){
					Note note= new Note();
					note.setMessageBody(rs.getString("message_body"));
					note.setCreationDate(rs.getDate("creation_date"));
					note.setExpiryDate(rs.getDate("expiry_date"));
					note.setPriorityLevel(rs.getInt("priority_level"));
					notes.add(note);
				}
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notes;
	}
	
	public void addUser(User user){
		String sql= "INSERT INTO User (login_name, display_name, pass_hash) values(?, ?, ?)";
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getLoginName() );
			stmt.setString(2, user.getDisplayName() );
			stmt.setString(3, user.getPassHash() );
			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disableNote(int note_id){
		String sql= "UPDATE Note SET is_active=false WHERE id_notes=?";
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, note_id );
			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addNote(Note note, User user){
		if(note.getExpiryDate()==null) {
			String sql = "INSERT INTO Note (message_body, creation_date, priority_level, is_active, id_owner) values(?, ?, ?, ?, ?)";
			
			PreparedStatement stmt;
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, note.getMessageBody() );
				stmt.setDate(2, note.getCreationDate() );
				stmt.setInt(3, note.getPriorityLevel() );
				stmt.setBoolean(4, true);
				stmt.setInt(5, user.getID() );
				
				stmt.execute();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else { 
			String sql = "INSERT INTO Note "+"(message_body, creation_date, expiry_date, priority_level, id_owner, is_active) values(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt;
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, note.getMessageBody() );
				stmt.setDate(2, note.getCreationDate() );
				stmt.setDate(3, note.getExpiryDate() );
				stmt.setInt(4, note.getPriorityLevel() );
				stmt.setInt(5, user.getID() );
				stmt.setBoolean(6, true);
				
				stmt.execute();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void updateUser(User user){
		//Deliberadamente não queremos que o login_name seja alterável, apenas o display_name e a senha
		String sql= "UPDATE User SET "+"display_name=?, pass_hash=? WHERE id=?";
		
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getDisplayName() );
			stmt.setString(2, user.getPassHash() );
			stmt.setInt(3, user.getID() );
			
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkIfLoginIsAvailable(User user){
		String sql= "SELECT * FROM User WHERE login_name=?";
		
		PreparedStatement stmt;
		ResultSet rs;
		boolean ans= false;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getLoginName() );
			
			rs= stmt.executeQuery();
			ans= !rs.next();
			//System.out.println("debugando checkIfLoginIsAvailable");
			//System.out.println("ans vale: "+Boolean.toString(ans));
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public boolean authenticate(String auth){
			
		String[] tokens= auth.split("-");
		String login= tokens[0];
		String hash= tokens[1];
		
		/*
		System.out.println();
		System.out.println("O split do meu cookie resultou em:");
		System.out.println(login);
		System.out.println(hash);
		System.out.println();
		*/
			
		User user = new User();
		user.setLoginName(login);
		user.setPassHash(hash);
		
		return validateUser(user);
	}
	
	public boolean validateUser(User user) {
		//Retorna false se o signUp falhar (par login e hash incorreto)
		//pesquisar pelo par login+senha
		//preenche user com os demais campos
		
		PreparedStatement stmt= null;
		String sql= "";
		boolean ans= false;
		try {
			sql = "SELECT * FROM User WHERE login_name=? AND pass_hash=?"; //TODO confirmar se é assim que uso o prepared statement e se ele já adciona as aspas
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getLoginName());
			stmt.setString(2, user.getPassHash());
			ResultSet rs= stmt.executeQuery();
			
			ans= rs.next();
			
			if( ans){
				user.setDisplayName( rs.getString("display_name") );
				user.setID( rs.getInt("id_user") );
			}
			
			rs.close();
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Checando a validação do usuário: "+Boolean.toString(ans));
		return ans;
	}
	
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}