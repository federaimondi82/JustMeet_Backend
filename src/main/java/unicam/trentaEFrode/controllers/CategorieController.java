package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import unicam.trentaEFrode.domain.DBConnection;

/**
 * Controller relativo a tutte le chiamate rest riguardanti le categorie.
 * */
@RestController
public class CategorieController {

	@GetMapping(value="/cat")
	public String getCategorie() {
		
		String json="";
		ResultSet result=null;
		try {
			result=DBConnection.getInstance().sendQuery("SELECT * FROM categoria");
			
			while(result.next()) {
				json+=result.getInt(1)+"-"+result.getString(2)+"-"+result.getString(3)+"_";
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@GetMapping(value="/cat/{id}")
	public String getCategoria(@PathVariable String id) {
		if(id.equals("")) throw new IllegalArgumentException("Elemento vuoto");
		
		String json="";
		ResultSet result=null;
		try {
			result=DBConnection.getInstance().sendQuery("SELECT * FROM categoria WHERE id='"+id+"'");
			
			while(result.next()) {
				json+=result.getInt(1)+"-"+result.getString(2)+"-"+result.getString(3);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public boolean insertCategoria(String nome, String descrizione) {
		try {
			return DBConnection.getInstance().insertData("INSERT INTO categoria(nome, descrizione) VALUES (' " + nome + "', '" + descrizione + "')");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
