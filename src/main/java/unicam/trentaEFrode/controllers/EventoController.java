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
	 * @param nome
	 * @param aaaa
	 * @param mm
	 * @param gg
	 * @param HH
	 * @param MM
	 * @param min
	 * @param max
	 * @param descr
	 * @param durata
	 * @param nomeLuogo
	 * @param citta
	 * @param indirizzo
	 * @param civico
	 * @param cap
	 * @param prov
	 * @param idCat
	 * @param idUtente
	 * 
	 * @throws IllegalArgumentException se uno degli elementi è nullo
	 * @return
	 */
	//http://localhost:8080/eventi/nuovo/3:2134:2020:3:13:15:38:10:100:festa:5:Ascoli:AP:viaTuring:1800:63100:bar:2:2

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
		int idLuogo = controllaLuogo(citta, indirizzo, civico, cap, prov, nomeLuogo);

		//controllo per scegliere se salvare o modificare un evento
		return (Integer.parseInt(id)== -1) ?
			 salvaEvento(nome,data,ora,Integer.parseInt(min),Integer.parseInt(max),descr,Integer.parseInt(durata),idLuogo,Integer.parseInt(idCat),Integer.parseInt(idUtente))
			: modificaEvento(id,nome,data,ora,Integer.parseInt(min),Integer.parseInt(max),descr,Integer.parseInt(durata),idLuogo,Integer.parseInt(idCat),Integer.parseInt(idUtente));
			
	}
	
	private boolean modificaEvento(String id, String nome, String data, String ora, int min, int max,
			String descr, int durata, int idLuogo, int idCat, int idUtente) {
		boolean esiste = controllaEvento(nome,data,ora,min,max,durata,idLuogo,idCat);
				
		if(!esiste) {
			
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

	private boolean salvaEvento(String nome,String data, String ora, int min,int max,String descr,int durata,int idLuogo,int idCat, int idUtente) {
		
		//viene controllato se esiste un evento nello stesso periodo,con lo stesso nome
		boolean esiste = controllaEvento(nome,data,ora,min,max,durata,idLuogo,idCat);
		

		// se si non e' possibile salvare l'evento
		//altrimenti viene salvato
		if(!esiste) {
			String query="INSERT into evento(nome,data,orario,min,max,descrizione,durata,idUtente,idLuogo,idCategoria)"
					+ "VALUES('"+nome+"','"+data+"','"+ora+"',"+min+","+max+",'"+descr+"',"+durata+","+idUtente+","+idLuogo+","+idCat+")";
			
			System.out.println("query evento controller = " + query);
			
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
	 * @param nome
	 * @param data
	 * @param ora
	 * @param parseInt
	 * @param parseInt2
	 * @param descr
	 * @param parseInt3
	 * @param parseInt4
	 * @param idLuogo
	 * @param parseInt5
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
	 * @param citta
	 * @param indirizzo
	 * @param civico
	 * @param cap
	 * @param prov
	 * @param nomeLuogo
	 * @return l'id del luogo presente oppure appena creato
	 */
	private int controllaLuogo(String citta, String indirizzo, String civico, String cap, String prov, String nomeLuogo) {
		
		LuogoController luogoController=new LuogoController();
		//l'id del luogo relativo a questo evento
		return luogoController.postluogo(citta, indirizzo, civico, cap, prov, nomeLuogo);		
	}


	@GetMapping(value="/eventi/utenti/{id}")
	public String getEventi(@PathVariable String id){
		
		String query="SELECT "
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,"
				+ "L.nome,L.indirizzo,L.civico,L.cap,L.citta,L.provincia,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U "
				+ "where E.idUtente="+Integer.parseInt(id)+" "
				+ "and E.idLuogo=L.id "
				+ "and C.id=E.idCategoria "
				+ "group by E.id "
				+ "order by E.data,E.orario";
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	@GetMapping(value="/eventi/{id}")
	public String getEvento(@PathVariable String id){
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
	
	@GetMapping(value="/eventi")
	public String getEventi(){
		String query="SELECT "
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,"
				+ "L.nome,L.indirizzo,L.civico,L.cap,L.citta,L.provincia,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U "
				+ "WHERE E.idLuogo=L.id "
				+ "AND C.id=E.idCategoria";
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	@DeleteMapping(value = "/eventi/cancella/{id}")
	public String cancellaEvento(@PathVariable String id) {
		String query = "DELETE FROM eventi WHERE id = " + Integer.parseInt(id); 
		try {
			DBConnection.getInstance().insertData(query);
			return "true";
		} catch (SQLException e) {
			e.printStackTrace();
			return "false";
		}
	}
	
	@GetMapping(value = "/cerca/{parola}:{categoria}:{citta}:{provincia}:{inizioAnno}:{inizioMese}:{inizioGiorno}:{fineAnno}:{fineMese}:{fineGiorno}:{idUtente}")
	public String cerca(@PathVariable String parola, @PathVariable String categoria, 
			@PathVariable String citta, @PathVariable String provincia, @PathVariable String inizioAnno, @PathVariable String inizioMese, @PathVariable String inizioGiorno, 
			@PathVariable String fineAnno, @PathVariable String fineMese, @PathVariable String fineGiorno, @PathVariable String idUtente ) {
		
		System.out.println("eventoCOntroller 255 parola = " + parola + " \n parola == null =" + (parola == null) + " \n parola == 'null'" + (parola.equals("null")) );
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
				+ "GROUP BY E.id;";
		
		System.out.println("query di ricerca 269 evento controller = \n" + query);
		
		try {
			return resultSetEventiToString(DBConnection.getInstance().sendQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	
	
	public String resultSetEventiToString(ResultSet result) {
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
	
}