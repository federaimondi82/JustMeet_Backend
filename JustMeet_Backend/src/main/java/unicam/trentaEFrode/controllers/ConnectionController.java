package unicam.trentaEFrode.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {
	
	@GetMapping(value="/testConnessione")
	public boolean getConnessione() {
		return true;
	}

}
