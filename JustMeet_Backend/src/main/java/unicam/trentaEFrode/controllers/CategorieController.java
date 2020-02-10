package unicam.trentaEFrode.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import unicam.trentaEFrode.domain.DBConnection;
import unicam.trentaEFrode.domain.UtenteRegistrato;

@RestController
public class CategorieController {

	@GetMapping(value="/cat")
	public  String postCategorie() {
		
		String json="";
		ResultSet result=null;
		try {
			result=DBConnection.getInstance().sendQuery("SELECT * FROM categoria");
			
			while(result.next()) {
				json+=result.getInt(1)+":"+result.getString(2)+":"+result.getString(3)+"_";
			}
			
			json=json.substring(0, json.length()-1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//list.stream().forEach(ele->ele.toString());
		return json;
	}
}
