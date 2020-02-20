package unicam.trentaEFrode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import unicam.trentaEFrode.domain.CategorieLoader;


/**
 * Riceve le chiamate Rest dall'applicazione client.
 * E' l'interfaccia verso l'esterno dell'applicazione lato backend
 * @author feder
 *
 */
@SpringBootApplication
public class ServerInterface {

	public static void main(String[] args) {
		SpringApplication.run(ServerInterface.class, args);
		CategorieLoader.getInstance().loader();
	}

}
