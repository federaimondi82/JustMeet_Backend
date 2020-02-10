package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.LuogoController;
import unicam.trentaEFrode.domain.DBConnection;

class Luogo_test {
	
	private static final LuogoController cont=new LuogoController();

	@Test
	public final void getIdLuogo_Exists() {
		//citta indirizzo civico cap prov nome
		//"Ascoli","viaTuring","10","63100",  "AP","bar"
		assertEquals(cont.getIdLuogo("Ascoli","viaTuring","10","63100",  "AP","bar"),1);
	}
	
	@Test
	public final void getIdLuogo_NotExists() {
		//citta indirizzo civico cap prov nome
		//"Ascoli","viaTuring","10","63100",  "AP","bar"
		assertEquals(cont.getIdLuogo("isola di nessuno","via invisibile","-1","00000", "AP","bar"),-1);
	}
	
	@Test
	public final void getIdLuogo_NullPointerExceptions() {
		//citta indirizzo civico cap prov nome
		//"Ascoli","viaTuring","10","63100",  "AP","bar"
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo(null,"viaTuring","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli",null,"10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring",null,"63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10",null,  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10","63100",null,"bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10","63100",  "AP",null));
		
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("","viaTuring","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10","",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10","63100","","bar"));
		assertThrows(NullPointerException.class, ()->cont.getIdLuogo("Ascoli","viaTuring","10","63100",  "AP",""));
	}
		
	@Test
	public final void postLuogo_Exists_FirstLuogo() {
		//citta indirizzo civico cap prov nome
		//"Ascoli","viaTuring","10","63100",  "AP","bar"
		assertEquals(cont.postluogo("Ascoli","viaTuring","10","63100",  "AP","bar"),1);
	}
	
	@Test
	public final void postLuogo_NotExists_returnRight_idLuogo() {
		ResultSet result=null;
		int number = 0;
		int numeroCivicoFake =(int) (Math.random()*100); 
		System.out.println(String.valueOf(numeroCivicoFake));
		try {
			result=DBConnection.getInstance().sendQuery("SELECT count(*) FROM luogo");
			while(result.next()) number=result.getInt(1);
		
			assertEquals(cont.postluogo("Ascoli","viaTuring",String.valueOf(numeroCivicoFake),"63100",  "AP","bar"),(number+1));
		
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	
	@Test
	public final void postNewLuogo_NullPointerExceptions() {
		//citta indirizzo civico cap prov nome
		//"Ascoli","viaTuring","10","63100",  "AP","bar"
		assertThrows(NullPointerException.class, ()->cont.postluogo(null,"viaTuring","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli",null,"10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring",null,"63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10",null,  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10","63100",null,"bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10","63100",  "AP",null));
		
		assertThrows(NullPointerException.class, ()->cont.postluogo("","viaTuring","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","","10","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","","63100",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10","",  "AP","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10","63100","","bar"));
		assertThrows(NullPointerException.class, ()->cont.postluogo("Ascoli","viaTuring","10","63100",  "AP",""));
	}

}
