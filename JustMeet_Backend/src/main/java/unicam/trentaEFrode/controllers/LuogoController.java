package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import unicam.trentaEFrode.domain.DBConnection;

public class LuogoController {

	
	/**
	 * Salva un nuovo luogo da associare ad un evento<br>
	 * viene cercato se nel DB e' gia' presente il luogo da salvare<br>:
	 * se il luogo non c'e' nel DB,allora viene salvato;
	 * se il luogo e' presente allora esce dal metodo ritornando l'id del luogo
	 * 
	 * @param id dell'utente che vuole creare un nuovo evento ed aggiunge un luogo
	 * @param citta
	 * @param indirizzo
	 * @param civico
	 * @param cap
	 * @param prov
	 * @param nome
	 * @return l'id del nuovo luogo
	 */
	@PostMapping(value="/luogo/{citta}:{indirizzo}:{civico}:{cap}:{prov}:{nome}")
	public synchronized int postluogo(@PathVariable String citta,@PathVariable String indirizzo,
			@PathVariable String civico,@PathVariable String cap,@PathVariable String prov,@PathVariable String nome){
		
		//viene cercato se nel DB e' gia' presente il luogo da salvare
		int idToReturn=getIdLuogo(citta,indirizzo,civico,cap,prov,nome);
		
		try {
			if(idToReturn==-1) {//se il luogo non c'e' nel DB viene salvato
				
				String query="insert into luogo(citta,indirizzo,civico,cap,provincia,nome) "
						+ "VALUES('"+citta+"','"+indirizzo+"','"+civico+"','"+cap+"','"+prov+"','"+nome+"')";
				DBConnection.getInstance().insertData(query);
			}
			else {
				//se il luogo e' presente allora esce dal metodo ritornando l'id del luogo
				return idToReturn;
			}
		}catch(SQLException e) {e.printStackTrace();}
		return idToReturn;
	}
	
	/**
	 * Permette di conoscere se un luogo e' gia' presente nel database
	 * @return Ritorna l'id del luogo,se presente; ritorna -1 se non presente
	 */
	@GetMapping(value="/luoghi/{citta}:{indirizzo}:{civico}:{cap}:{provincia}:{nome}")
	public synchronized int getIdLuogo(@PathVariable String citta,@PathVariable String indirizzo,@PathVariable String civico
			,@PathVariable String cap,@PathVariable String prov,@PathVariable String nome) {
		
		try {			
			String query="select id from luogo where citta='"+citta+"' "
					+ "and indirizzo='"+indirizzo+"' "
					+ "and civico='"+civico+"' "
					+ "and cap='"+cap+"' "
					+ "and provincia='"+prov+"' "
					+ "and nome='"+nome+"' ";
			
			ResultSet result=null;
		
			
			result = DBConnection.getInstance().sendQuery(query);
			
			while(result.next()) {
				return result.getInt(1);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;

	}
}
