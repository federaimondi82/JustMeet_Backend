package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.ThrowsException;

import unicam.trentaEFrode.controllers.EventoController;

class Eventi_Test {

	private static final EventoController cont=new EventoController();
	
	/*@Test
	public final void creaEvento_IllegalArgumentException() {
		//capodanno:2020:1:1:11:11:10:100:festa:5:bar:Ascoli:viaTuring:10:63100:AP:2:18
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
	}*/
	
	@Test
	void test_() {
		System.out.println(cont.getEventi("2"));
	}

}
