package unicam.trentaEFrode.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class ConnectionController {
	
	@GetMapping(value="/testConnessione")
	public boolean getConnessione() {
		return true;
	}

}
