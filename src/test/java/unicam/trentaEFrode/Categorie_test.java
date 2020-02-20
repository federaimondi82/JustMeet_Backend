package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.CategorieController;

class Categorie_test {

	private static final CategorieController cont=new CategorieController();

	@Test
	public final void getCategoria_Exceptions() {
		assertThrows(NullPointerException.class, ()->cont.getCategoria(null));
		assertThrows(IllegalArgumentException.class, ()->cont.getCategoria(""));
	}
	
	@Test
	public final void getCategoria() {
		assertEquals("1-sport-calcio", cont.getCategoria("1"));
	}
	
	@Test
	public final void getCategorie() {
		assertTrue(cont.getCategorie() != "");
	}
	
	@Test
	public final void insertCategorieException() {
		assertThrows(NullPointerException.class, ()->cont.insertCategoria(null, null));
		assertThrows(IllegalArgumentException.class, ()->cont.insertCategoria("", ""));
		assertFalse(cont.insertCategoria("sport", "calcio"));
	}

}
