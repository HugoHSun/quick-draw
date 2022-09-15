package nz.ac.auckland.se206.user;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import nz.ac.auckland.se206.CategorySelector;

public class UserManager {
	private List<User> users;
	
	
	public UserManager() throws URISyntaxException, IOException, CsvException {
		users = new ArrayList<User>();

	    // Read all the content in the file
	    File fileName = new File(CategorySelector.class.getResource("/user_data.csv").toURI());
	    try (var fr = new FileReader(fileName, StandardCharsets.UTF_8);
	        var reader = new CSVReader(fr)) {
	      List<String[]> lines = reader.readAll();
	      // Add categories with the chosen difficulty to the output
	      for (String[] line : lines) {
	        User user = new User(line[0],line[1]);
	        users.add(user);
	      }
	    }
	    
	    
	    
	}
	
	public List<String> getUsers() {
		List<String> usernames = new ArrayList<String>();
		for (User user : users) {
			usernames.add(user.getName());
		}
		
		return usernames;
	}
	
	public void addUser()
}
