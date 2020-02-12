package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.ThrowsException;

import unicam.trentaEFrode.controllers.EventoController;

class Eventi_Test {

	private static final EventoController cont=new EventoController();
	
	@Test
	public final void gestisciEvento_IllegalArgumentException() {
		//capodanno:2020:1:1:11:11:10:100:festa:5:bar:Ascoli:viaTuring:10:63100:AP:2:18
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","","viaTuring","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","","10","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","","63100","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","","AP","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","","2","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","","18"));
		assertThrows(IllegalArgumentException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2",""));
	}
	
	@Test
	public final void gestisciEvento_NullPointerException() {
		//capodanno:2020:1:1:11:11:10:100:festa:5:bar:Ascoli:viaTuring:10:63100:AP:2:18
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento(null,"capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1",null,"2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno",null,"1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020",null,"1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1",null,"11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1",null,"11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11",null,"10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11",null,"100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10",null,"festa","5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100",null,"5","bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa",null,"bar","Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5",null,"Ascoli","viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar",null,"viaTuring","10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli",null,"10","63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring",null,"63100","AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10",null,"AP","2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100",null,"2","18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP",null,"18"));
		assertThrows(NullPointerException.class, () -> cont.gestisciEvento("1","capodanno","2020","1","1","11","11","10","100","festa","5","bar","Ascoli","viaTuring","10","63100","AP","2",null));
	}
	
	@Test
	void test_() {
				
		System.out.println(cont.getEventi("18"));
	}

}
