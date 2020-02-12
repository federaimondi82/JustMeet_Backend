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
	public int postluogo(@PathVariable String citta,@PathVariable String indirizzo,
			@PathVariable String civico,@PathVariable String cap,@PathVariable String prov,@PathVariable String nome){
		if(citta==null || indirizzo==null || civico==null || cap==null || prov==null || nome==null) throw new NullPointerException("elemento nullo");
		else if(citta.equals("") || indirizzo.equals("") || civico.equals("") || cap.equals("") || prov.equals("") || nome.equals("")) throw new NullPointerException("elemento nullo");
		
		
		/**viene cercato se nel DB e' gia' presente il luogo da salvare*/
		int idToReturn=getIdLuogo(citta,indirizzo,civico,cap,prov,nome);
		
		try {
			if(idToReturn==-1) {/**se nel DB il luogo non c'e' viene poi salvato*/
				
				String query="INSERT INTO luogo(citta,indirizzo,civico,cap,provincia,nome) "
						+ "VALUES('"+citta+"','"+indirizzo+"','"+civico+"','"+cap+"','"+prov+"','"+nome+"')";
				DBConnection.getInstance().insertData(query);
			}
			else {
				/**se il luogo e' presente allora esce dal metodo ritornando l'id del luogo*/
				return idToReturn;
			}
		}catch(SQLException e) {e.printStackTrace();}
		return getIdLuogo(citta,indirizzo,civico,cap,prov,nome);
	}
	
	/**
	 * Permette di conoscere se un luogo e' gia' presente nel database
	 * @return Ritorna l'id del luogo,se presente; ritorna -1 se non presente
	 */
	@GetMapping(value="/luoghi/{citta}:{indirizzo}:{civico}:{cap}:{provincia}:{nome}")
	public  int getIdLuogo(@PathVariable String citta,@PathVariable String indirizzo,@PathVariable String civico
			,@PathVariable String cap,@PathVariable String prov,@PathVariable String nome) {
		if(citta==null || indirizzo==null || civico==null || cap==null || prov==null || nome==null) throw new NullPointerException("elemento nullo");
		else if(citta.equals("") || indirizzo.equals("") || civico.equals("") || cap.equals("") || prov.equals("") || nome.equals("")) throw new NullPointerException("elemento nullo");
		
		try {			
			String query="SELECT id FROM luogo WHERE citta='"+citta+"' "
					+ "AND indirizzo='"+indirizzo+"' "
					+ "AND civico='"+civico+"' "
					+ "AND cap='"+cap+"' "
					+ "AND provincia='"+prov+"' "
					+ "AND nome='"+nome+"' ";
			
			ResultSet result= DBConnection.getInstance().sendQuery(query);
			
			while(result.next()) {	return result.getInt(1);	}
		
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
}
