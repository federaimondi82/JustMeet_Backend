package unicam.trentaEFrode.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import unicam.trentaEFrode.controllers.CategorieController;

/**
 * La classe per registrare le nuove categorie disponibili nel database
 * Ad ogni avvio del server viene controllata la presenza delle categorie, scelte dal team,
 * sul database, se non ci sono le salva(nel caso in cui il server non sia mai stati avviato)
 *
 */
public class CategorieLoader {
	
	private static CategorieLoader instance;
	
	private CategorieLoader() {
		
	}
	
/**
*Metodo per accedere al Singleton
*/
	public static CategorieLoader getInstance() {
		if(instance==null) instance=new CategorieLoader();
		return instance;
	}
	
	/**
	 * Carica  delle stringhe lette da un file di testo
	 */
	public void loader() {
		//carica il file con le categorie (nome e descrizione)
		File fileCategorie=new File(System.getProperty("user.dir")+"\\plugin\\categorie.txt");
		List<String> listCat=null;
		
		try {
			//legge il file e per ogni linea memorizza i dati in un generic String String
			listCat=Files.readAllLines(fileCategorie.toPath());
			
			initCategorie(listCat);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 *Permette la memorizzazione di nuove categorie sul database
	 *Memorizza soltanto quelle categorie che non trova sul dataabase
	*/
	private static void initCategorie(List<String> listCat) {
		CategorieController contr = new CategorieController();
		listCat.stream().forEach(ele->{
			String[] s=ele.split(";");
			//memorizza soltanto quelle categorie che non trova sul dataabase
			if(contr.getCategoria(s[0]) == "") contr.insertCategoria(s[1], s[2]);
		});

		
	}
	
	
}
