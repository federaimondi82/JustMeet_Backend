package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import unicam.trentaEFrode.domain.DBConnection;

/**
 * Si occupa di gestire la tabella Partecipante del database.
 * */
@RestController
public class PartecipanteController {
	
	private static PartecipanteController instance= null;
	
	@PostMapping(value = "/partecipa/{idEvento}:{idUtente}")
	public boolean partecipa(@PathVariable String idEvento, @PathVariable String idUtente) {
		String query = "INSERT INTO partecipante(idEvento, idUtente) VALUES (" + Integer.parseInt(idEvento) + ", " + Integer.parseInt(idUtente) + ");";
		try {
			DBConnection.getInstance().insertData(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@DeleteMapping(value = "/partecipa/disdici/{idEvento}:{idUtente}")
	public boolean disdiciPartecipazione(@PathVariable String idEvento, @PathVariable String idUtente) {
		String query = "DELETE FROM partecipante WHERE idUtente = " + Integer.parseInt(idUtente) + " AND idEvento = " + Integer.parseInt(idEvento);
		try {
			DBConnection.getInstance().insertData(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@GetMapping(value = "/partecipa/utente/{idUtente}")
	public String eventiPartecipati(@PathVariable String idUtente) {
		String query = "SELECT " +
				"E.id, E.nome, E.data, E.orario, E.min, E.max, E.descrizione, E.durata, E.idUtente, " +
				"L.nome, L.indirizzo, L.civico, L.cap, L.citta, L.provincia, " +
				"C.id, C.nome, C.descrizione " +
				"FROM luogo L, evento E, categoria C "+
				"WHERE E.idLuogo = L.id AND E.idCategoria = C.id AND E.id IN " +
				"(SELECT idEvento FROM partecipante WHERE idUtente = " + Integer.parseInt(idUtente) + ");";
		
		try {
			return new EventoController().resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static PartecipanteController getInstance() {
		if(instance == null) instance = new PartecipanteController();
		return instance;
	}

	public boolean controllaPartecipanti(String idEvento) {
		String query = "DELETE FROM partecipante WHERE idEvento = " + Integer.parseInt(idEvento);
		try {
			DBConnection.getInstance().insertData(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@GetMapping(value ="/partecipa/partecipanti/{idEvento}")
	public String getPartecipanti(@PathVariable String idEvento) {
		String query = "SELECT id, nome, cognome, email FROM utente WHERE id IN(SELECT idUtente FROM partecipante WHERE idEvento = "+  idEvento + ")";
		String str = "";
		ResultSet result;
		try {
			result = DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				str += result.getInt(1) + "-" + result.getString(2) + "-" + result.getString(3)+ "-" + result.getString(4) + ",";
			}
			return str;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	
}
