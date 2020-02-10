package unicam.trentaEFrode.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class ConncectionController {
	
	@GetMapping(value="/testConnessione")
	public boolean getConnessione() {
		return true;
	}

}
