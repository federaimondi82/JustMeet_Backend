package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PostMapping(value="/eventi/nuovo/{nome}:{aaaa}:{mm}:{gg}:{HH}:{MM}:{min}:"
			+ "{max}:{descr}:{durata}:{nomeLuogo}:{citta}:{indirizzo}:{civico}:"
			+ "{cap}:{prov}:{idCat}:{idUtente}")
	public  boolean creaEvento(@PathVariable String nome,@PathVariable String aaaa,
			@PathVariable String mm,@PathVariable String gg,@PathVariable String HH,@PathVariable String MM,
			@PathVariable String min,@PathVariable String max,@PathVariable String descr,
			@PathVariable String durata,@PathVariable String nomeLuogo,@PathVariable String citta,@PathVariable String indirizzo,
			@PathVariable String civico,@PathVariable String cap,@PathVariable String prov,
			@PathVariable String idCat,@PathVariable String idUtente) {
		if(nome==null || aaaa==null || mm==null || gg==null || HH==null || MM==null || min==null ||
			max==null || descr==null || durata==null || nomeLuogo==null || citta==null || indirizzo==null ||
			civico==null || cap==null || prov==null || idCat==null || idUtente==null ) throw new IllegalArgumentException("elemento nullo");
				
		int idLuogo=controllaLuogo(citta, indirizzo, civico, cap, prov, nomeLuogo);
		
		String data=aaaa+"/"+mm+"/"+gg;
		String ora=HH+":"+MM;
		
		//viene controllato se esiste un evento nello stesso periodo,con lo stesso nome
		boolean testEvento=controllaEvento(nome,data,ora,Integer.parseInt(min),Integer.parseInt(max),Integer.parseInt(durata),idLuogo,Integer.parseInt(idCat));
		// se si non e' possibile salvare l'evento
		//altrimenti viene salvato
		if(testEvento==false) {
			String query="INSERT into evento(nome,data,orario,min,max,descrizione,durata,idUtente,idLuogo,idCategoria)"
					+ "VALUES('"+nome+"','"+data+"','"+ora+"',"+Integer.parseInt(min)+","+Integer.parseInt(max)+",'"+descr+"',"+Integer.parseInt(durata)+","+Integer.parseInt(idUtente)+","+idLuogo+","+Integer.parseInt(idCat)+")";
			System.out.println(query);
			try {
				return DBConnection.getInstance().insertData(query);
			}catch(SQLException e) {
				return false;
			}
		}
		else {
			return false;
		}
		
		
		
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
		
		//boolean trovato=false;
		ResultSet result=null;
		
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
			result=DBConnection.getInstance().sendQuery(query);
			
			//se result ritorna un valore vuol dire che un evento simile e' presente
			while(result.next()) {
				return true;
				//System.out.println(result.getInt(1));
			}
			//System.out.println("*************************");
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
	private int controllaLuogo(String citta, String indirizzo, String civico, String cap, String prov,
			String nomeLuogo) {
		
		LuogoController luogoController=new LuogoController();
		//l'id del luogo relativo a questo evento
		return luogoController.postluogo(citta, indirizzo, civico, cap, prov, nomeLuogo);		
	}


	@GetMapping(value="/eventi/utenti/{id}")
	public List<String> getEventi(@PathVariable String idUtente){
		
		
		String query="SELECT L.id,L.citta,L.indirizzo,L.civico,L.cap,L.provincia,L.nome,"
				+ "E.id,E.nome,E.data,E.orario,E.min,E.max,E.descrizione,E.durata,E.idUtente,E.idCategoria,"
				+ "C.id,C.nome,C.descrizione "
				+ "FROM luogo L,evento E,categoria C,utente U "
				+ "where U.id="+Integer.parseInt(idUtente)+" "
				+ "and E.idLuogo=L.id "
				+ "and C.id=E.idCategoria "
				+ "group by E.id "
				+ "order by E.data,E.orario";
		ResultSet result=null;
		try {
			result=DBConnection.getInstance().sendQuery(query);
			
			while(result.next()) {
				//LUOGO
				System.out.println(result.getInt(1));//id
				System.out.println(result.getString(2));//citta
				System.out.println(result.getString(3));//indir
				System.out.println(result.getString(4));//civico
				System.out.println(result.getString(5));//cap
				System.out.println(result.getString(6));//prov
				System.out.println(result.getString(7));//nome
				//EVENTO
				System.out.println(result.getInt(8));//id
				System.out.println(result.getString(9));
				System.out.println(result.getString(10));
				System.out.println(result.getString(11));
				System.out.println(result.getInt(12));
				System.out.println(result.getInt(13));
				System.out.println(result.getString(14));
				System.out.println(result.getInt(15));
				System.out.println(result.getInt(16));//idUtente
				System.out.println(result.getInt(17));//id categoria
				System.out.println(result.getInt(18));//id categoria
				//CATEGORIA
				System.out.println(result.getString(19));
				System.out.println(result.getString(20));
			}
		}catch(SQLException e) {
			//TODO cominciare a scrivere dei log
			System.out.println("");
		}
		
		
		return null;
	}

	@GetMapping(value="/eventi/{id}")
	public String getEvento(@PathVariable String id) {
		
		return null;
		
	}
	
}
