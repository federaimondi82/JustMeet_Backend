package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unicam.trentaEFrode.domain.DBConnection;
import unicam.trentaEFrode.domain.UtenteRegistrato;

/**
 *Controller per le chiamate Rest riferite agli utenti;
 *gestisce le client request interrogando direttamente il DBMS con query SQL.
 * @author feder
 *
 */
@RestController
public class UsersControllers {
	
	
	/**
	 * Consente di avere i dati di un utente dato il suo id
	 * @param id
	 * @return ritorna tutti i dati dell'itente con uno specifico id
	 */
	//@GetMapping(value="/utenti/{id}")
	public UtenteRegistrato getUserData(@PathVariable String id) {
		if(id.equals("")) throw new IllegalArgumentException("Elemento vuoto");
		else if(id==null ) throw new NullPointerException("Elemento nullo");
		
		ResultSet result2=null;
		UtenteRegistrato u=null;
		try {
			result2=DBConnection.getInstance().sendQuery("SELECT * FROM utente WHERE id="+id+"");
			u=new UtenteRegistrato();
			
			while(result2.next()) {
				u.setId(result2.getInt(1));
				u.setNome(result2.getString(2));
				u.setCognome(result2.getString(3));
				u.setEmail(result2.getString(4));
				u.setNickname(result2.getString(5));
				u.setPassword(result2.getString(6));
				u.setRipetiPassword(result2.getString(7));
				u.setDataDiNascita(result2.getString(8));
				u.setCitta(result2.getString(9));
				u.setCap(result2.getString(10));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return u;
	}
	
	
	/**Consente di reperire tutti i dati di tutti gli utenti del database
	 * @return una lista di Utenti
	 * @See List<T>
	 */
	public List<UtenteRegistrato> getUsers() {
		
		ResultSet result2=null;
		List<UtenteRegistrato> list=null;
		try {
			result2=DBConnection.getInstance().sendQuery("SELECT * FROM utente");
			list=new ArrayList<UtenteRegistrato>();
			
			while(result2.next()) {
				UtenteRegistrato u= new UtenteRegistrato();
				u.setId(result2.getInt(1));
				u.setNome(result2.getString(2));
				u.setCognome(result2.getString(3));
				u.setEmail(result2.getString(4));
				u.setNickname(result2.getString(5));
				u.setPassword(result2.getString(6));
				u.setRipetiPassword(result2.getString(7));
				u.setDataDiNascita(result2.getString(8));
				u.setCitta(result2.getString(9));
				u.setCap(result2.getString(10));
				list.add(u);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Invia i dati per una nuova registrazione al server
	 * @param nome
	 * @param cognome
	 * @param email
	 * @param nickname
	 * @param password
	 * @param ripetiPassword
	 * @param dataDiNascita
	 * @param cap
	 * @param citta
	 * @return Ritorna true se la registrazione e' andata a buon fine altriemnti false
	 */
	@PostMapping(value="/utenti/{nome}:{cognome}:{email}:{nickname}:{password}:{ripetiPassword}:{dataDiNascita}:{cap}:{citta}:{interessi}")
	public boolean registraUtente(@PathVariable String nome,@PathVariable String cognome,
			@PathVariable String email,@PathVariable String nickname,@PathVariable String password,
			@PathVariable String ripetiPassword,@PathVariable String dataDiNascita,
			@PathVariable String cap,@PathVariable String citta,@PathVariable String interessi
			) {
		if(nome==null || cognome==null || email==null || nickname==null || password==null || 
				ripetiPassword==null || dataDiNascita==null || cap==null || citta==null || interessi==null)throw new NullPointerException("Elemento nullo");
		else if(nome.equals("") || cognome.equals("") || email.equals("") || nickname.equals("") || password.equals("") || 
				ripetiPassword.equals("") || dataDiNascita.equals("") || cap.equals("") || citta.equals("") || interessi.equals(""))throw new IllegalArgumentException("Elemento vuoto");
		try {
			//viene controllato se l'email o il nickName sono gia' stati usati
			//se il controllo va a buon fine vine fatta una INSERT sul database
		if(utentePresente(email)==false && nickNamePresente(nickname)==false) {
			String query="INSERT INTO utente(nome,cognome,email,nickname,pass,pass2,dataNascita,citta,cap)"
					+ "VALUES('"+nome+"','"+cognome+"','"+email+"','"+nickname+"','"+password+"','"+ripetiPassword+"','20/20/2000','"+citta+"','"+Integer.parseInt(cap)+"')";
				DBConnection.getInstance().insertData(query);
								
				salvaInteressi(interessi,getUserId(email,password));
								
				
				return true;
		}
		else return false;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;			
	}


	/**Consente di memorizza dli interessi dell'utnte sul database
	 * @param interessi una lista sotto form di stringa di id di categorie
	 * @param id l'id dell'utente a cui associare gli interessi
	 */
	private void salvaInteressi(String interessi, int id) {
		
		String[] parser=interessi.split("_");
		List<Integer> list=new ArrayList<Integer>();
		String subQuery="";
		for(int i=0;i<=parser.length-1;i++)  subQuery+="("+id+","+parser[i]+"),";

		
		String query="INSERT INTO utentecategoria(idUtente,idCategoria) VALUES"+subQuery.substring(0, subQuery.length()-1)+";";
		//String queryMigliorata=query.substring(0, query.length()-1)+";";
		try {
			DBConnection.getInstance().insertData(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}


	private int getUserId(String email,String pass) {
		int id=0;
		try {
			
			ResultSet result=DBConnection.getInstance().sendQuery("SELECT id FROM utente WHERE email='"+email+"' AND pass='"+pass+"'");
			
			while(result.next()) id=result.getInt(1); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
		
	}


	
	/**
	 * Permette di capire se un utente e' gia stato salvato nel database
	 * @param email : l'email dell'utente che tenta di registarsi
	 * @return true se l'utente e' presente nel database,altrimanti false
	 */
	@GetMapping(value="utenti/{email}")
	public boolean utentePresente(@PathVariable String email){
		String query="SELECT id from utente where email='"+email+"'";
		ResultSet result=null;
		boolean trovato=false;
		try {
			result = DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				if(result.getInt(1)>0) trovato= true;
				else trovato= false;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return trovato;
		
	}
	
	/**Ritorna la lista degli eventi che ha creato l'utente
	 * @param id
	 * @return
	 */
	@GetMapping(value="/utenti/{id}/events")
	public List<String> getUserEvents(@PathVariable String id){
		
//evento sul db:id,titolo,dataInizio,min,max,descrizion,durata,idUtente,idLuogo,idCategoria
//luogo:id,citta,indirizzo,civico,cap,provincia,nome
		String query2="Select L.id,L.citta,L.indirizzo,L.civico,L.cap,L.provincia,L.nome,"
				+ "E.d,E.titolo,E.dataInizio,E.min,E.max,E.descrizion,E.durata,E.idUtente,E.idLuogo,E.idCategoria,"
				+ "C.id,C.nome,C.descrizione from L luogo, E evento, C categoria"
				+ "where E.idUtente='"+id+"' "
				+ "and E.idLuogo=L.id "
				+ "and E.idCategoria=C.id";
		//String query="select * from evento where idUtente='"+id+"'";		
		ResultSet result=null;
		List<String> list=new ArrayList<String>();
		
		try {
			//result=DBConnection.getInstance().sendQuery(query);
			
			while(result.next()) {
				//list.add(result.getInt(0)+";"+result.getString(1)+";"+result.getInt(2)+";"+result.getInt(3))
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Viene controllato se sul database e' presente il nickname;
	 * @param nickname
	 * @return ritorna true se il nickname e' gia' presente, altriemnti false
	 */
	@GetMapping(value="/utenti/nickname/{nickname}")
	public boolean nickNamePresente(@PathVariable String nickname) {
		
		ResultSet result=null;
		int id=0;
		try {
			result= DBConnection.getInstance().sendQuery("SELECT id FROM utente WHERE nickname='"+nickname+"'");
			while(result.next()) {
				id=result.getInt(1);
			}
			if(id!=0) return true;
			else return false;

		} catch (SQLException e) {
			
			return false;
		}
		
	}

	
	/**Metodo per la get dell'autenticazione, consente di stabilire se le credenziali inviate dal client sono<br>
	 * valide.
	 * @param email l'email dell'utente registrato
	 * @param pass la password dell'utente registrato
	 * @return una istanza di utenteRegistrato (con la rest ritorno comunuqe un json)
	 */
	@GetMapping(value="/utenti/auth/{email}:{pass}")
	public UtenteRegistrato autenticazione(@PathVariable String email,@PathVariable String pass) {
		
		String query="SELECT id FROM utente WHERE email='"+email+"' and pass='"+pass+"'";
		ResultSet result=null;
		int id=0;
		UtenteRegistrato u=null;
		try {
			result=DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				id=result.getInt(1);
			}
			//se un utente con quell'id esiste ritorna i dati
			u=getUserData(String.valueOf(id));
			
			u.setInteressi(getInteressiUtente(id));
			//System.out.println("utente:"+u.toString());
			return u;
		}catch(SQLException e){
			//altrimenti ritorna null;
			return null;
		}
	}


	public String getInteressiUtente(int id) {
		ResultSet result=null;
		String json="{";
		try {
			String query="SELECT C.id,C.nome,C.descrizione "
					+ "FROM Categoria C, utentecategoria U "
					+ "WHERE C.id=U.idCategoria "
					+ "AND U.idUtente="+id+"";
			result=DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				json+=result.getInt(1)+"-"+result.getString(2)+"-"+result.getString(3)+"_";
			}

			return json.substring(0,json.length()-1)+"}";
			
		}catch(SQLException e){
			//altrimenti ritorna null;
			return null;
		}
	}
	
}
