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


	/**
	 *Metodo per preperire tuttele categorie che sono sul database
	 * @return json: un elemento in formato json(una strigna strutturata)
	*/
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
	
	/**
	 *Metodo per preperire una specifica categoria da database
	 * @return json: un elemento in formato json(una strigna strutturata)
	*/
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

	private String getCategoria(String nome, String descrizione){
		if(nome == "" | descrizione == "") throw new IllegalArgumentException("Elemento vuoto");
		if (nome == null | descrizione == null ) throw new NullPointerException();
		String json="";
		ResultSet result=null;
		try {
			result=DBConnection.getInstance().sendQuery("SELECT * FROM categoria WHERE nome ='"+nome+"' AND descrizione = '" + descrizione + "'");
			while(result.next()) {
				json+=result.getInt(1)+"-"+result.getString(2)+"-"+result.getString(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	* Utilizza DBConnection per l'inserimento sul database di nuove categorie
	*/
	public boolean insertCategoria(String nome, String descrizione) {
		
		if(getCategoria(nome, descrizione) != "") return false;
		try {
			return DBConnection.getInstance().insertData("INSERT INTO categoria(nome, descrizione) VALUES (' " + nome + "', '" + descrizione + "')");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
