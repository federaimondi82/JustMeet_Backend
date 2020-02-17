package unicam.trentaEFrode.controllers;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import unicam.trentaEFrode.domain.DBConnection;

@RestController
public class PartecipanteController {
	
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
	
	@GetMapping(value = "/partecipa/utente/{idUtente}")
	public String eventiPartecipati(@PathVariable String idUtente) {
		/*String query = "SELECT "
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,"
				+ "L.nome,L.indirizzo,L.civico,L.cap,L.citta,L.provincia,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U, partecipante P "
				+ "WHERE E.idLuogo = L.id "
				+ "AND E.idCategoria = C.id "
				+ "AND E.id = P.idEvento "
				+ "AND P.idUtente = " + Integer.parseInt(idUtente);*/
		String query = "SELECT " +
				"E.id, E.nome, E.data, E.orario, E.min, E.max, E.descrizione, E.durata, E.idUtente, " +
				"L.nome, L.indirizzo, L.civico, L.cap, L.citta, L.provincia, " +
				"C.id, C.nome, C.descrizione " +
				"FROM luogo L, evento E, categoria C "+
				"WHERE E.idLuogo = L.id AND E.idCategoria = C.id AND E.id IN " +
				"(SELECT idEvento FROM partecipante WHERE idUtente = " + Integer.parseInt(idUtente) + ");";
		
		System.out.println("query 47 partcipantecontroller = " + query);
		try {
			return new EventoController().resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			System.out.println("eccezione in 50 partecipanteController");
			e.printStackTrace();
			return "";
		}
	}

}
