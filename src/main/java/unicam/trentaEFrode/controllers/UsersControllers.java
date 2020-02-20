package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
				u.setDataDiNascita(result2.getDate(8).toString());
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
	@PostMapping(value="/utenti/{nome}:{cognome}:{email}:{nickname}:{password}:{ripetiPassword}:{dataDiNascita}:{cap}:{citta}:{provincia}:{interessi}")
	public boolean registraUtente(@PathVariable String nome,@PathVariable String cognome,
			@PathVariable String email,@PathVariable String nickname,@PathVariable String password,
			@PathVariable String ripetiPassword,@PathVariable String dataDiNascita,
			@PathVariable String cap,@PathVariable String citta, @PathVariable String provincia, @PathVariable String interessi
			) {
		if(nome==null || cognome==null || email==null || nickname==null || password==null || 
				ripetiPassword==null || dataDiNascita==null || cap==null || citta==null || provincia==null ||interessi==null)throw new NullPointerException("Elemento nullo");
		else if(nome.equals("") || cognome.equals("") || email.equals("") || nickname.equals("") || password.equals("") || 
				ripetiPassword.equals("") || dataDiNascita.equals("") || cap.equals("") || citta.equals("") || provincia.equals("") ||interessi.equals(""))throw new IllegalArgumentException("Elemento vuoto");
		try {
			//viene controllato se l'email o il nickName sono gia' stati usati
			//se il controllo va a buon fine vine fatta una INSERT sul database
			boolean esisteEmail = utentePresente(email);
			boolean esisteNickname = nickNamePresente(nickname);
			if(!esisteEmail && !esisteNickname) {
				String query="INSERT INTO utente(nome,cognome,email,nickname,pass,pass2,dataNascita,citta,cap, provincia)"
						+ "VALUES('"+nome+"','"+cognome+"','"+email+"','"+nickname+"','"+password+"','"+ripetiPassword+"','" + dataDiNascita + "','"+citta+"','"+Integer.parseInt(cap)+"', '" + provincia + "')";
				boolean inserito = DBConnection.getInstance().insertData(query);
				if(inserito) return salvaInteressi(interessi, getUserId(email,password));								
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;			
	}


	/**Consente di memorizza dli interessi dell'utnte sul database
	 * @param interessi una lista sotto form di stringa di id di categorie
	 * @param id l'id dell'utente a cui associare gli interessi
	 */
	private boolean salvaInteressi(String interessi, int id) {
		if(interessi == "" | interessi == null) return false;
		String[] parser=interessi.split("_");
		String subQuery="";
		for(int i=0;i<=parser.length-1;i++)  subQuery+="("+id+","+parser[i]+"),";
		String query="INSERT INTO utentecategoria(idUtente,idCategoria) VALUES"+subQuery.substring(0, subQuery.length()-1)+";";
		try {
			return DBConnection.getInstance().insertData(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
	@GetMapping(value="/utenti/{email}")
	public boolean utentePresente(@PathVariable String email){
		if(email == "") throw new IllegalArgumentException();
		if(email == null) throw new NullPointerException();
		String query="SELECT id from utente where email='"+email+"'";
		try {
			ResultSet result = DBConnection.getInstance().sendQuery(query);
			return result.next();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Viene controllato se sul database e' presente il nickname;
	 * @param nickname
	 * @return ritorna true se il nickname e' gia' presente, altriemnti false
	 */
	@GetMapping(value="/utenti/nickname/{nickname}")
	public boolean nickNamePresente(@PathVariable String nickname) {
		if(nickname == "") throw new IllegalArgumentException();
		if(nickname == null) throw new NullPointerException();
		try {
			ResultSet result= DBConnection.getInstance().sendQuery("SELECT id FROM utente WHERE nickname='"+nickname+"'");
			return result.next();
		} catch (SQLException e) {
			return false;
		}
	}

	
	/**Metodo per la get dell'autenticazione, consente di stabilire se le credenziali inviate dal client sono<br>
	 * valide.
	 * @param email l'email dell'utente registrato
	 * @param pass la password dell'utente registrato
	 * @return le informazioni dell'utente in formato stringa, se l'autenticazione va a buon fine, altrimenti una stringa vuota.
	 */
	@GetMapping(value="/utenti/auth/{email}:{pass}")
	public String autenticazione(@PathVariable String email,@PathVariable String pass) {
		
		if(email == "" || pass == "") throw new IllegalArgumentException();
		if(email == null || pass == null) throw new NullPointerException();
		String query="SELECT * FROM utente WHERE email='"+email+"' AND pass='"+pass+"';";
		
		String str= "";
		try {
			ResultSet result=DBConnection.getInstance().sendQuery(query);
			
			while(result.next()) {
				for(int i = 1; i<= 11; i++) {
					switch(i) {
						case 1 : str+= result.getString(i); break;
						case 8 : str += result.getDate(i); break;
						default : str += result.getString(i);
					}
				str+=":";
				}	
			}
			return str;
		} catch (SQLException e) {
			e.printStackTrace();		
		}
		return "";
	}


	/**
	*Permette di conoscere gli interessi di uno specifico utente
	* @return 
	*/
	public String getInteressiUtente(int id) {
		String str="";
		try {
			String query="SELECT C.id,C.nome,C.descrizione "
					+ "FROM Categoria C, utentecategoria U "
					+ "WHERE C.id=U.idCategoria "
					+ "AND U.idUtente="+id+"";
			ResultSet result=DBConnection.getInstance().sendQuery(query);
			while(result.next()) {
				str+=result.getInt(1)+"-"+result.getString(2)+"-"+result.getString(3)+"_";
			}
		}catch(SQLException e){}
		return str;
	}
	
}
