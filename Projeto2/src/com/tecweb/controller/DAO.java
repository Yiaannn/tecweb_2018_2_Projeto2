package com.tecweb.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tecweb.entidades.*;

public class DAO {

	private Connection connection = null;
	
	public DAO(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection= DriverManager.getConnection("jdbc:mysql://localhost/tecwebprojeto1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "12063cb5d72571662926aa355c97abc8a28c87f3");
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
		//Deliberadamente n�o queremos que o login_name seja alter�vel, apenas o display_name e a senha
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
	
	public User authenticate(String auth){
			
		String[] tokens= auth.split("-");
		if (tokens.length!=2){
			//poderia considerar devolver um malformed nesta condi��o
			return null;
		}
		String login= tokens[0];
		String pass= tokens[1];
		
		/*
		System.out.println();
		System.out.println("O split do meu cookie resultou em:");
		System.out.println(login);
		System.out.println(hash);
		System.out.println();
		*/
			
		User user = new User();
		user.setLoginName(login);
		user.setPassHash(hashFromPass(pass));
		
		if ( validateUser(user) ){
			return user;
		}
		return null;
	}
	
	public boolean validateUser(User user) {
		//Retorna false se o signUp falhar (par login e hash incorreto)
		//pesquisar pelo par login+senha
		//preenche user com os demais campos
		
		PreparedStatement stmt= null;
		String sql= "";
		boolean ans= false;
		try {
			sql = "SELECT * FROM User WHERE login_name=? AND pass_hash=?";
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
		
		System.out.println("Checando a valida��o do usu�rio: "+Boolean.toString(ans));
		return ans;
	}
	
	public String hashFromPass(String pass){
		String passhash= null;
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(  pass.getBytes(StandardCharsets.UTF_8)  );
			passhash = bytesToHex(hash);
			
		}catch (Exception e){
			System.out.println("Something went really wrong while hashing");
			e.printStackTrace();
		}
		
		/*
		System.out.println("Para garantir, printar o hash:");
		System.out.println(passhash);
		*/
		
		return passhash;
	}
	

	
	private String bytesToHex(byte[] bytes) { //adaptado de https://stackoverflow.com/a/9855338
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
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