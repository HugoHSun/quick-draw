package nz.ac.auckland.se206.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionaryLookup {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  /**
   * This method queries a definition request of the input string to dictionary API and returns
   * entries containing its part of speech and definitions. Adapted from UoA SE206 Lab 2 exercise.
   *
   * @param query the word to get information on
   * @return a WordInfo instance containing a list of entries including the word's part of speech
   *     and corresponding definitions
   * @throws IOException {@inheritDoc}
   * @throws WordNotFoundException {@inheritDoc}
   */
  public static WordInfo searchWordInfo(String query) throws IOException, WordNotFoundException {
    // Make a request to the API through OkHttpClient and get the response
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + query).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    String jsonString = responseBody.string();

    // Check if the request was success
    try {
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
      String title = jsonObj.getString("title");
      String subMessage = jsonObj.getString("message");
      throw new WordNotFoundException(query, title, subMessage);
    } catch (ClassCastException e) {
    }

    JSONArray jArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    List<WordEntry> entries = new ArrayList<WordEntry>();

    // Add all the definitions and part of speech to the word entry
    for (int e = 0; e < jArray.length(); e++) {
      JSONObject jsonEntryObj = jArray.getJSONObject(e);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

      String partOfSpeech = "[not specified]";
      List<String> definitions = new ArrayList<String>();

      // Adding part of speech
      for (int m = 0; m < jsonMeanings.length(); m++) {
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
        String pos = jsonMeaningObj.getString("partOfSpeech");

        if (!pos.isEmpty()) {
          partOfSpeech = pos;
        }

        // Adding definitions
        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
        for (int d = 0; d < jsonDefinitions.length(); d++) {
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);

          String definition = jsonDefinitionObj.getString("definition");
          if (!definition.isEmpty()) {
            definitions.add(definition);
          }
        }
      }

      // Note that the entries are a list of WordEntry
      WordEntry wordEntry = new WordEntry(partOfSpeech, definitions);
      entries.add(wordEntry);
    }

    return new WordInfo(query, entries);
  }
}
