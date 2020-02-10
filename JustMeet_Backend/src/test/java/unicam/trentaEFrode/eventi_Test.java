package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.EventoController;

class eventi_Test {

	@Test
	void test() {
		EventoController cont=new EventoController();
		
		cont.getEventi("18");
	}

}
