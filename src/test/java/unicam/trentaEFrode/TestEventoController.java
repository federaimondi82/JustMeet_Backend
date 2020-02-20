package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.EventoController;
import unicam.trentaEFrode.domain.DBConnection;

class TestEventoController {

//	@AfterEach
//	void tearDown() throws Exception {
//	}
	
	private static final EventoController cont=new EventoController();

	@Test
	void testGestisciEvento() {
		cont.gestisciEvento("-1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18");		
		assertFalse(cont.gestisciEvento("-1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
	}

	@Test
	void testGetEvento() {
		assertThrows(IllegalArgumentException.class, ()->cont.getEvento(null));
		assertThrows(IllegalArgumentException.class, ()->cont.getEvento(""));
		assertTrue(cont.getEvento("2")!="");//un evento dell'utente 3
		assertTrue(cont.getEvento("-1")=="");

	}

	@Test
	void testCancellaEvento() {
		String nomeEvento=String.valueOf(new Random().nextInt(10000));
		//creazione di un evento fittizio per il test
		cont.gestisciEvento("-1",nomeEvento,"2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","1");	
		//reperisco l'id dell'evento
		ResultSet result=null;
		try {
			result = DBConnection.getInstance().sendQuery("SELECT id FROM evento WHERE nome='"+nomeEvento+"'");
		
			int idEvento=0;
			while(result.next()) idEvento=result.getInt(1);
			
			//cancellazione dell'evento
			assertTrue(cont.cancellaEvento(String.valueOf(idEvento)));
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testCerca() {
		//evento presente
		assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9")!="");
		//evento non presente
		assertTrue(cont.cerca("sdcobwefuieon23cbsiucbsdc", "motagna", "New York", "AP1", "2025", "02", "02", "2020", "02", "02", "-1")=="");
		//assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9")!="");
	}

	@Test
	void testResultSetEventiToString() {
		//evento presente
		assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9")!="");
		assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9").contains("{"));
		assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9").contains("}"));
		assertTrue(cont.cerca("", "cibo", "Roma", "AP", "2020", "02", "02", "2025", "02", "02", "9").contains("{"));
		//evento non presente
		assertTrue(cont.cerca("sdcobwefuieon23cbsiucbsdc", "motagna", "New York", "AP1", "2025", "02", "02", "2020", "02", "02", "-1")=="");
	}

	@Test
	void testCambiaOrganizzatore() {
		assertThrows(NullPointerException.class, ()->cont.cambiaOrganizzatore(null, "3", "2"));
		assertThrows(NullPointerException.class, ()->cont.cambiaOrganizzatore("3", null, "2"));
		assertThrows(NullPointerException.class, ()->cont.cambiaOrganizzatore("3", "3", null));
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("", "3", "2"));
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("3", "", "2"));
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("3", "3", ""));
		
		
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("asd", "3", "2"));
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("3", "asd", "2"));
		assertThrows(IllegalArgumentException.class, ()->cont.cambiaOrganizzatore("3", "3", "asd"));
	}

	@Test
	public final void test_ModificaEvento() {
		cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18");		
		assertFalse(cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
	}

	@Test
	public final void gestisciEvento_IllegalArgumentException() {
		//capodanno:2020:1:1:11:11:10:100:festa:5:bar:Ascoli:viaTuring:10:63100:AP:2:18
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","","10","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","","63100","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","","AP","bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","", "bar","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2",""));
	}
	
	@Test
	public final void gestisciEvento_NullPointerException() {
		//capodanno:2020:1:1:11:11:10:100:festa:5:bar:Ascoli:viaTuring:10:63100:AP:2:18
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento(null,"capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1",null,"2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno",null,"1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020",null,"1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1",null,"11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1",null,"11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11",null,"10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11",null,"100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10",null,"festa","5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100",null,"5","Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa",null,"Ascoli","viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP",null,"2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5",null,"viaTuring","10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli",null,"10","63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring",null,"63100","AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10",null,"AP","bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100",null,"bar","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar",null,"18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","Ascoli","viaTuring","10","63100","AP","bar","2",null));
	}
	
}
