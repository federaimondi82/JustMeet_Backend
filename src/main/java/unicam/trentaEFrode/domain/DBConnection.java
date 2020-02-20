package unicam.trentaEFrode.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Fornisce una interfaccia unificata per inviare e ricevere informazioni al/dal database
 *
 */
public class DBConnection {
	
	private static DBConnection instance;
	private String url;
	private String dbName;
	private String user;
	private String pass;
	private Connection con;
	
	
	private DBConnection() throws SQLException {
		this.url="localhost:3306";
		this.dbName="justmeet";
		this.user="root";
		this.pass="";
		this.con=DriverManager.getConnection("jdbc:mysql://"+url+"/"+dbName+"",user,pass); 
		
	}
	
	public static DBConnection getInstance() throws SQLException {
		if(instance==null) instance=new DBConnection();
		return instance;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the con
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * @param con the con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}
	
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**Permette di reperire i dati dal database tramite una query di "select"
	 * @param una query di select
	 * @return ritorna il resultSet della query;
	 */
	public ResultSet sendQuery(String query) {
		Statement stat=null;
		ResultSet result = null;
		try {
			stat=con.createStatement();
			result= stat.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * Permette di eseguire query di inserimento e update sui dati
	 * @param query
	 * @return ritorna true se la query e' andata a buon fine
	 */
	public boolean insertData(String query) {
		Statement stat=null;
		try {
			stat=con.createStatement();
			stat.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

}
