package unicam.trentaEFrode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import unicam.trentaEFrode.domain.CategorieLoader;


/**
 * E' l'interfaccia input/output dell'applicazione lato backend
 * Riceve le chiamate Rest dall'applicazione client.
 * 
 *
 */
@SpringBootApplication
public class ServerInterface {

	public static void main(String[] args) {
		SpringApplication.run(ServerInterface.class, args);

//Ad ogni avvio vengono caricate le nuove categorie definite dl team di sviluppo
		CategorieLoader.getInstance().loader();
	}

}
