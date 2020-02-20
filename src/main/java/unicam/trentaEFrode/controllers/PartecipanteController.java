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
	
	
	/**
	 *metodo di accettazione da parte di un utente ad un evento
	 *
	 * @param idEvento l'identificativo dell'evento
	 * @param idUtente l'identificativo dell'utenteRegistrato
	 * @return true se l'inseriemnto nel database è andata a buon fine, altrimenti false
	 */
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
	
	/**
	 *Un utente che vuole disdire la propria partecipazione ad un evento
	 * @param idEvento l'identificativo dell'evento
	 * @param idUtente l'identificativo dell'utenteRegistrato
	 * @return true se la cancellazione della partecipazioned dell'utente all'evento è andata a buon fine, altrimenti false
	 */
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
	
	/**
	 * Per conoscere tutti lgi eventi nei quali l'utente è partecipante
	 * @param idUtente l'identificativo dell'utente
	 * @return un insieme di eventi in formato json
	 */
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

	/**
	 * Cancella tutt ele riche di uno specifico evento sulla tabella dele partecipazioni
	 * @param idEvento l'identificativo di un evento
	 * @return treu se la richiesta è stata sffisfatta, false atrimenti
	 */
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
	/**
	*Metodo per conoscere ogni partecipante  di uno specifico evento
	*@param idEvento: l'ientificativo di un evento
	*@return la serie di utenti in formato json
	*/
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
