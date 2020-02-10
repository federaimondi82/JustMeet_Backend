package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.UsersControllers;

class User_test {

	private static final UsersControllers cont=new UsersControllers();

	@Test
	void test() {
		System.out.println(cont.getInteressiUtente(23));
	}

}
