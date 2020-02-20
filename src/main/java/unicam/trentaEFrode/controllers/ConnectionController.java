package unicam.trentaEFrode.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
*
*Controller per le chimate REST per quanto riguarda la connessione tra clinet e server
*/
@RestController
public class ConnectionController {
	
/**
*Se la chiamata REST arriva al controller automaticamente si capisce che la connessione esiste.
*/
	@GetMapping(value="/testConnessione")
	public boolean getConnessione() {
		return true;
	}

}
