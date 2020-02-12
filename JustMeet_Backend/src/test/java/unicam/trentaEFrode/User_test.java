package unicam.trentaEFrode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import unicam.trentaEFrode.controllers.UsersController;

class User_test {

	private static final UsersController cont=new UsersController();

	@Test
	void test() {
		System.out.println(cont.getInteressiUtente(23));
	}

}
