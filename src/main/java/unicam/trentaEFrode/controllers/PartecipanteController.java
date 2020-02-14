package unicam.trentaEFrode.controllers;

import java.sql.SQLException;

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

}
