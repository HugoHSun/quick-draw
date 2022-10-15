package nz.ac.auckland.se206.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.user.User;

public class JsonReader {

  /**
   * This method reads the local user Json file and return them as a list
   *
   * @return a list of User
   * @throws IOException
   */
  public static List<User> getUsers() throws IOException {
    // Get all users from the users Json file by Gson library
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // construct Type that tells Gson about the generic type
    Type userListType = new TypeToken<List<User>>() {}.getType();
    FileReader fr = new FileReader(App.usersFileName);
    List<User> users = gson.fromJson(fr, userListType);
    fr.close();
    return users;
  }

  /**
   * This method reads the local user Json file and return their names as a list
   *
   * @return a List of user names String
   * @throws IOException
   */
  public static List<String> getUserNames() throws IOException {
    // Get the name field from all the users
    List<User> users = JsonReader.getUsers();
    List<String> userNames = new ArrayList<String>();
    for (User user : users) {
      userNames.add(user.getName());
    }
    return userNames;
  }
}
