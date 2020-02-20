package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.exceptions.DataConversionException;

import unicam.trentaEFrode.domain.DBConnection;

/**
 * Controller per le chiamate Rest riferite agli eventi;
 *gestisce le client request interrogando direttamente il DBMS con query SQL.
 *
 */
@RestController
public class EventoController {
	
	
	
	/**
	 * 
	 * Consente l'inserimento sul database di un nuovo evento
	 * @param nome : Nome dell'evento
	 * @param aaaa : Anno dell'evento
	 * @param mm : Mese dell'evento
	 * @param gg : Giorno dell'evento
	 * @param HH : Ora dell'evento
	 * @param MM : Minuto dell'evento
	 * @param min : Minino numero di partecipanti che prendno parte all'evento
	 * @param max : Massimo numero di partecipanti che prendno parte all'evento
	 * @param descr : Descrizione dell'evento
	 * @param durata : Durata in in ore dell'evento
	 * @param nomeLuogo : Un nome per identificare il luogo (bar all'angolo, palestra, piazza del quartiere ecc....)
	 * @param citta : Città dov si svolge l'evento
	 * @param indirizzo : Indirizzo dove si svolge l'evento
	 * @param civico : Civico dell'indirizzo di dove si svolge l'evento (accetta elementi del tipi 12/A )
	 * @param cap :  Codice di avviamento postale della città dove si svolge l'evento
	 * @param prov : Provincia della città dove si svolge l'evento
	 * @param idCat : un id numerico della categoria scelta per questo particolare evento
	 * @param idUtente : un id numerico riferito all'utente
	 * 
	 * @throws NullPointerException se uno degli elementi è nullo
	 * @throws IllegalArgumentException se uno degli elementi è vuoto
	 * @return true se l'inserimento del nuovo evento è andto a buon fine, altrimenti false;
	 */
	@PostMapping(value="/eventi/nuovo/{id}:{nome}:{aaaa}:{mm}:{gg}:{HH}:{MM}:{min}:"
			+ "{max}:{descr}:{durata}:{nomeLuogo}:{indirizzo}:{civico}:"
			+ "{cap}:{citta}:{prov}:{idCat}:{idUtente}")
	public  boolean gestisciEvento(@PathVariable String id, @PathVariable String nome,@PathVariable String aaaa,
			@PathVariable String mm,@PathVariable String gg,@PathVariable String HH,@PathVariable String MM,
			@PathVariable String min,@PathVariable String max,@PathVariable String descr,
			@PathVariable String durata, @PathVariable String citta,@PathVariable String indirizzo,
			@PathVariable String civico,@PathVariable String cap,@PathVariable String prov, @PathVariable String nomeLuogo,
			@PathVariable String idCat,@PathVariable String idUtente) {
		if(id==null || nome==null || aaaa==null || mm==null || gg==null || HH==null || MM==null || min==null ||
			max==null || descr==null || durata==null || nomeLuogo==null || citta==null || indirizzo==null ||
			civico==null || cap==null || prov==null || idCat==null || idUtente==null ) throw new NullPointerException("elemento nullo");
		else if(id=="" || nome.equals("")|| aaaa.equals("")|| mm.equals("")|| gg.equals("")|| HH.equals("")|| MM.equals("")|| min.equals("")||
			max.equals("")|| descr.equals("")|| durata.equals("")|| nomeLuogo.equals("")|| citta.equals("")|| indirizzo.equals("")||
			civico.equals("")|| cap.equals("")|| prov.equals("")|| idCat.equals("")|| idUtente.equals("")) throw new IllegalArgumentException("elemento nullo");
		
		String data=aaaa+"-"+mm+"-"+gg;
		String ora=HH+":"+MM;
		//prima di memorizzare un nuovo luogo riferito all'evento viene controllato se già esiste sul database
		int idLuogo = controllaLuogo(citta, indirizzo, civico, cap, prov, nomeLuogo);

		//Avendo utilizzato una sola chiamata REST sia per la memorizzazione che per la modifica dell'evento
		//viene effettuato un controllo per scegliere se salvare o modificare un evento
		//Dal lato client, per far sapere se salvare vien inserito -1 sull'id dell'evento, per modificare viene inserito l'id corretto dell'evento 
		return (Integer.parseInt(id)== -1) ?
			 salvaEvento(nome,data,ora,Integer.parseInt(min),Integer.parseInt(max),descr,Integer.parseInt(durata),idLuogo,Integer.parseInt(idCat),Integer.parseInt(idUtente))
			: modificaEvento(id,nome,data,ora,Integer.parseInt(min),Integer.parseInt(max),descr,Integer.parseInt(durata),idLuogo,Integer.parseInt(idCat),Integer.parseInt(idUtente));
			
	}
	
	/**
	 * 
	 * Consente la modifica di un evento sul database
	 *
	 * @param nome : Nome dell'evento
	 * @param data : la data di quando si svolge l'evento
	 * @param ora : Orario dell'evento
	 * @param min : Minino numero di partecipanti che prendno parte all'evento
	 * @param max : Massimo numero di partecipanti che prendno parte all'evento
	 * @param descr : Descrizione dell'evento
	 * @param durata : Durata in in ore dell'evento
	 * @param idLuogo : l'identificativo del luogo dove si svolge l'evento
	 * @param idCat : un id numerico della categoria scelta per questo particolare evento
	 * @param idUtente : un id numerico in relazione all'utente
	 *
	 * @return true se la modifica dell'evento è andto a buon fine, altrimenti false;
	 */
	private boolean modificaEvento(String id, String nome, String data, String ora, int min, int max,
			String descr, int durata, int idLuogo, int idCat, int idUtente) {

		//Viene comunque effettuato un controllo per verificare un eventuale evengo identico già presente
		boolean esiste = controllaEvento(nome,data,ora,min,max,durata,idLuogo,idCat);
				
		if(!esiste) {
			//aggiornamento dell'evento
			String query="UPDATE evento SET "
					+ "nome='"+nome+"', data= '"+data+"',orario='"+ora+"',min="+min+",max="+min+",descrizione='"+descr+"',"
					+ "durata="+durata+",idLuogo="+idLuogo+",idCategoria="+idCat+" "
					+ "WHERE id="+Integer.parseInt(id)+";";
			try {
								
				return DBConnection.getInstance().insertData(query);
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		return false;
		
	}

	/**
	 * 
	 * Consente il salvataggio di un evento sul database
	 *
	 * @param nome : Nome dell'evento
	 * @param data : la data di quando si svolge l'evento
	 * @param ora : Orario dell'evento
	 * @param min : Minino numero di partecipanti che prendno parte all'evento
	 * @param max : Massimo numero di partecipanti che prendno parte all'evento
	 * @param descr : Descrizione dell'evento
	 * @param durata : Durata in in ore dell'evento
	 * @param idLuogo : l'identificativo del luogo dove si svolge l'evento
	 * @param idCat : un id numerico della categoria scelta per questo particolare evento
	 * @param idUtente : un id numerico in relazione all'utente
	 *
	 * @return true se l'inserimento del nuovo evento è andto a buon fine, altrimenti false;
	 */
	private boolean salvaEvento(String nome,String data, String ora, int min,int max,String descr,int durata,int idLuogo,int idCat, int idUtente) {
		
		//viene controllato se esiste un evento nello stesso periodo,con lo stesso nome
		boolean esiste = controllaEvento(nome,data,ora,min,max,durata,idLuogo,idCat);
		

		// se si non e' possibile salvare l'evento
		//altrimenti viene salvato
		if(!esiste) {
			String query="INSERT into evento(nome,data,orario,min,max,descrizione,durata,idUtente,idLuogo,idCategoria)"
					+ "VALUES('"+nome+"','"+data+"','"+ora+"',"+min+","+max+",'"+descr+"',"+durata+","+idUtente+","+idLuogo+","+idCat+")";
						
			try {
				return DBConnection.getInstance().insertData(query);
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
		} 
		return false;
	}
	
	/**
	 * Consente di contollare se un evento e' gia' stato memorizzato
	 *
	 * @param nome : Nome dell'evento
	 * @param data : la data di quando si svolge l'evento
	 * @param ora : Orario dell'evento
	 * @param min : Minino numero di partecipanti che prendno parte all'evento
	 * @param max : Massimo numero di partecipanti che prendno parte all'evento
	 * @param durata : Durata in in ore dell'evento
	 * @param idLuogo : l'identificativo del luogo dove si svolge l'evento
	 * @param idCategoria : un id numerico della categoria scelta per questo particolare evento
	 *
	 * @return false se l'evento non e' gia' stato memorizzato, altrimenti true se c'e'
	 */
	private boolean controllaEvento(String nome, String data, String ora, int min, int max,
		int durata, int idLuogo, int idCategoria) {		
		String query="SELECT id FROM evento "
				+ "WHERE nome='"+nome+"' "
				+ "AND data='"+data+"' "
				+ "AND orario='"+ora+"' "
				+ "AND min="+min+" "
				+ "AND max="+max+" "
				+ "AND durata="+durata+" "
				+ "AND idLuogo="+idLuogo+" "
				+ "AND idCategoria="+idCategoria+" ";
		try {
			ResultSet result=DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				return true;
			}
			return false;
		}catch(SQLException e) {
			return false;
		}
		
	}

	/**
	 * Consente di capire se il luogo e' presente nei database oppure no
	 * @param citta : Città dov si svolge l'evento
	 * @param indirizzo : Indirizzo dove si svolge l'evento
	 * @param civico : Civico dell'indirizzo di dove si svolge l'evento (accetta elementi del tipi 12/A )
	 * @param cap :  Codice di avviamento postale della città dove si svolge l'evento
	 * @param prov : Provincia della città dove si svolge l'evento
	 * @param nomeLuogo : Un nome per identificare il luogo (bar all'angolo, palestra, piazza del quartiere ecc....)
	 *
	 * @return l'id del luogo presente oppure appena creato
	 */
	private int controllaLuogo(String citta, String indirizzo, String civico, String cap, String prov, String nomeLuogo) {
		
		LuogoController luogoController=new LuogoController();
		//l'id del luogo relativo a questo evento
		return luogoController.postluogo(citta, indirizzo, civico, cap, prov, nomeLuogo);		
	}


	/**
	* Metodo per le chiamate REST get per tutti gli eventi di un singolo utente
	* @param id: l'id dell'utente registrato che utilizza l'applicazione
	* @return una serie di eventi in formato json
	*/
	@GetMapping(value="/eventi/utenti/{id}")
	public String getEventi(@PathVariable String id){
		
		String query="SELECT "
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,"
				+ "L.nome,L.indirizzo,L.civico,L.cap,L.citta,L.provincia,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U "
				+ "WHERE E.idUtente="+Integer.parseInt(id)+" "
				+ "AND E.idLuogo=L.id "
				+ "AND C.id=E.idCategoria "
				+ "group by E.id "
				+ "order by E.data,E.orario";
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	* Metodo per le chiamate REST get per uno specifico evento
	* @param id: l'id  dell'evento da ricercare
	* @return una evento in formato json
	*/
	@GetMapping(value="/eventi/{id}")
	public String getEvento(@PathVariable String id){
		if(id=="" || id==null) throw new IllegalArgumentException("indice non valido");
		String query="SELECT "
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,"
				+ "L.nome,L.indirizzo,L.civico,L.cap,L.citta,L.provincia,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U "
				+ "WHERE E.id="+Integer.parseInt(id)+" "
				+ "AND E.idLuogo=L.id "
				+ "AND C.id=E.idCategoria";
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	
	/**
	* Metodo per le chiamate REST DELETE per uno specifico evento
	* un utente decide di cancellare un evento da lui creato
 	* vengono cancellati anche tutte le partecipazioni degli eventuali altri partecipanti
	* @param id: l'id  dell'evento da ricercare
	* @return una evento in formato json
	*/
	@DeleteMapping(value = "/eventi/cancella/{id}")
	public boolean cancellaEvento(@PathVariable String id) {
		
		if(new PartecipanteController().controllaPartecipanti(id)) {
			//TODO notificare i patecipanti
			String query = "DELETE FROM evento WHERE id = " + Integer.parseInt(id); 
			try {
				DBConnection.getInstance().insertData(query);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
		}
		return false;
	}
	
	/**
	* Metodo per le chiamate REST get per la ricerca di eventi in base ad alcune specifiche dell'utente
	* @param parola: una parola dell'utente
	* @param categoria: una categoria tra quelle disponibili
	* @param citta: una città scelta dall'utente
	* @param provincia: la provincia dove l'utente vuole vedre gli eventi
	* @param inizioAnno: 
	* @param inizioMese: 
	* @param inizioGiorno: 
	* @param fineAnno:
	* @param fineMese: 
	* @param fineGiorno: 
	* @param idUtente: 
	* @return un insieme di eventi in formato json
	*/
	@GetMapping(value = "/cerca/{parola}:{categoria}:{citta}:{provincia}:{inizioAnno}:{inizioMese}:{inizioGiorno}:{fineAnno}:{fineMese}:{fineGiorno}:{idUtente}")
	public String cerca(@PathVariable String parola, @PathVariable String categoria, 
			@PathVariable String citta, @PathVariable String provincia, @PathVariable String inizioAnno, @PathVariable String inizioMese, @PathVariable String inizioGiorno, 
			@PathVariable String fineAnno, @PathVariable String fineMese, @PathVariable String fineGiorno, @PathVariable String idUtente ) {
		
		String inizio = inizioAnno + "-" + inizioMese + "-" + inizioGiorno;
		String fine = fineAnno + "-" + fineMese + "-" + fineGiorno;

		parola = !parola.equals("null")? "OR E.nome LIKE '%" + parola + "%' " : "";
		categoria = !categoria.equals("null")? " C.nome = '" + categoria + "' " : " C.id IN (SELECT idCategoria FROM utentecategoria WHERE idUtente = " + Integer.parseInt(idUtente) + ") ";
		citta = !citta.equals("null")? " L.citta = '" +  citta + "' " : " L.citta IN (SELECT citta FROM utente WHERE id = " + Integer.parseInt(idUtente) + ") ";
		provincia = !provincia.equals("null")? " L.provincia = '" + provincia + "' " : " L.provincia IN (SELECT provincia FROM utente WHERE id = " + Integer.parseInt(idUtente) + ") ";
		String date = " data BETWEEN '" + inizio + "' AND '" + fine + "' ";	
		String query="SELECT "
				+ "E.id, E.nome, E.data, E.orario, E.min, E.max, E.descrizione, E.durata, E.idUtente, "
				+ "L.nome, L.indirizzo, L.civico, L.cap, L.citta, L.provincia, "
				+ "C.id, C.nome, C.descrizione "
				+ "FROM luogo L, evento E, categoria C, utente U "
				+ "WHERE E.idLuogo = L.id "
				+ "AND E.idCategoria = C.id "
				+ "AND E.idUtente <> " + Integer.parseInt(idUtente) + " AND ("
				+  categoria + "OR" + citta + "OR" + provincia + "OR" + date + parola +") "
				+ "AND E.id NOT IN (SELECT idEvento FROM partecipante WHERE idUtente = " + Integer.parseInt(idUtente) + ")"
				+ "GROUP BY E.id;";
				
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	
	/**
	*Parsa gli eventi della query in formato stringa
	*/
	public String resultSetEventiToString(ResultSet result) {
		if(result==null) throw new NullPointerException("elemento nullo");
		String str = "";
		try {
			while(result.next()){
				str+="{";
				for(int i = 1; i < 19 ; i++) {
					try {
						if(i == 3) str += result.getDate(3).toString().replace("-", "/");
						else str+=result.getInt(i);
					}catch(NumberFormatException | DataConversionException | SQLDataException e) {
						str += result.getString(i);
					};
					str += "-";
				}
				str +="};"; 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* Metodo per le REST post riguardo al cambio di organizzatore di un evento
	* L'evento cambia organizzatore e il vecchio organizzatore diventa partecipante e il vecchio partecipante non configura tra i partecipanti;
	* ricordiamo che l'organizzatore non compare tra i partecipanti
	*
	*/
	@PostMapping(value= "/evento/cambiaorganizzatore/{idEvento}:{idOrganizzatore}:{idVecchioOrganizzatore}")
	public boolean cambiaOrganizzatore(@PathVariable String idEvento, @PathVariable String idOrganizzatore,@PathVariable String idVecchioOrganizzatore ){
		if(idEvento==null || idOrganizzatore==null || idVecchioOrganizzatore==null) throw new NullPointerException("elemento nullo");
	else if(idEvento=="" || idOrganizzatore.equals("")|| idVecchioOrganizzatore.equals("")) throw new IllegalArgumentException("elemento nullo");
	try {
		Integer.parseInt(idEvento);
		Integer.parseInt(idOrganizzatore);
		Integer.parseInt(idVecchioOrganizzatore);
	}catch(NumberFormatException e) {
		throw new IllegalArgumentException("numero non valido");
	}
		
		String query = "UPDATE evento SET idUtente = " + idOrganizzatore + " WHERE id = " + idEvento;
		PartecipanteController partContr = new PartecipanteController();
		partContr.disdiciPartecipazione(idEvento, idOrganizzatore);
		partContr.partecipa(idEvento, idVecchioOrganizzatore);
		try {
			return DBConnection.getInstance().insertData(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}