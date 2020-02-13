package unicam.trentaEFrode.domain;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * La classe per registrare le nuove categorie disponibili nel database
 * Ad ogni avvio del server viene controllata la presenza delle categorie, scelte dal team,
 * sul database, se non ci sono le salva(nel caso in cui il server non sia mai stati avviato)
 * @author feder
 *
 */
public class CategorieLoader {
	
	private static CategorieLoader instance;
	
	private CategorieLoader() {
		
	}
	
	public static CategorieLoader getInstance() {
		if(instance==null) instance=new CategorieLoader();
		return instance;
	}
	
	/**
	 * Carica a tempo di compilazione delle stringhe con le categorie definite dal team di sviluppo
	 * @return un array di url; gli url di una cartella dove e' possibile caricare file di testo o plugin
	 * @throws MalformedURLException
	 */
	/*
	public URL[] loadUrls() throws MalformedURLException {
		String folder=System.getProperty("user.dir");
		File f=new File(folder+"\\plugin\\");

		URL[] urls= {f.toURI().toURL()};
		
		return urls;
	}
	*/
	
	/**
	 * 
	 */
	public void loader() {
		//carica il file con le categorie (nome e descrizione)
		File fileCategorie=new File(System.getProperty("user.dir")+"\\plugin\\categorie.txt");
		List<String> listCat=null;
		HashMap<String,String> mapCat=new HashMap<String,String>();
		
		try {
			//legge il file e per ogni linea memorizza i dati in un generic String String
			listCat=Files.readAllLines(fileCategorie.toPath());
			
			initCategorie(listCat);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void initCategorie(List<String> listCat) {
		
		listCat.stream().forEach(ele->{
			String[] s=ele.split(";");
			try {
				DBConnection.getInstance().insertData("insert into categoria(id,nome,descrizione) values('"+s[0]+"','"+s[1]+"','"+s[2]+"')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		
	}
	
	
}
