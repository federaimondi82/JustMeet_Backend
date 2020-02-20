package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.UsersControllers;

class UserTest {
	
	UsersControllers contr=new UsersControllers();


	@Test
	void testGetUsers() {
		assertTrue(contr.getUsers().size()!=0);	
	}

	@Test
	void testRegistraUtente() {
		
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "", "ASCULE", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "", "AP", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "", "2_3_1_"));
		assertThrows(IllegalArgumentException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", ""));
		assertThrows(NullPointerException.class, ()->contr.registraUtente(null, "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", null, "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", null, "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", null, "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", null, "asd", "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", null, "1880-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", null, "63100", "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", null, "ASCULE", "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", null, "AP", "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", null, "2_3_1_"));
		assertThrows(NullPointerException.class, ()->contr.registraUtente("mario", "rossi", "mario@gmail.com", "marione", "asd", "asd", "1880-12-12", "63100", "ASCULE", "AP", null));
	
		String rand=String.valueOf((int)(Math.random()*10000));
		
		assertTrue(contr.registraUtente("mario", "rossi", rand+"@gmail.com", rand, "asd", "asd", "1980-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
		assertFalse(contr.registraUtente("mario", "rossi", rand+"@gmail.com", rand, "asd", "asd", "1980-12-12", "63100", "ASCULE", "AP", "2_3_1_"));
	
	}

	@Test
	void testUtentePresente() {
		assertThrows(IllegalArgumentException.class, ()->contr.utentePresente(""));
		assertThrows(NullPointerException.class, ()->contr.utentePresente(null));
		assertTrue(contr.utentePresente("mariorossi@email.it"));
		assertFalse(contr.utentePresente("email"));
	}

	@Test
	void testNickNamePresente() {
		assertThrows(IllegalArgumentException.class, ()->contr.nickNamePresente(""));
		assertThrows(NullPointerException.class, ()->contr.nickNamePresente(null)); 		
		assertTrue(contr.nickNamePresente("marione"));
		assertFalse(contr.utentePresente("nickname"));	
	}

	@Test
	void testAutenticazione() {
		assertThrows(IllegalArgumentException.class, ()->contr.autenticazione("", "password"));
		assertThrows(NullPointerException.class, ()->contr.autenticazione(null, "password"));
		assertThrows(IllegalArgumentException.class, ()->contr.autenticazione("email", ""));		
		assertThrows(NullPointerException.class, ()->contr.autenticazione("email", null));
		assertTrue(contr.autenticazione("mariorossi@email.it", "abc")!="");
		assertTrue(contr.autenticazione("email.it", "password")=="");
	}

	@Test
	void testGetInteressiUtente() {
		assertTrue( contr.getInteressiUtente(-1) == ""); // utente inesistente, quindi non ha interessi.
		assertFalse(contr.getInteressiUtente(3)== ""); // utente esistente con interessi
	}
	

}
