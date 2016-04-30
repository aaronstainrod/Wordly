package com.aaronstainrod.app.wordly;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 4/29/16.
 */
public class Interpreter {

    public static final String definition = "DEFINITION";
    public static final String synonym = "SYNONYM";
    public static final String antonym = "ANTONYM";
    public static final String rhymes = "RHYMES";
    public static final String sentence = "SENTENCE";
    public static final String syllable = "SYLLABLES";

    public Interpreter() {}

    public String getDefinition(String string) throws JSONException  {

        try {
            JSONObject jo = new JSONObject(string);
            return jo.getString("definitions");
        } catch (JSONException e) {}

        return "Error obtaining definition";
    }

    public String getSynonym(String string) throws JSONException {

        JSONObject jo = new JSONObject(string);

        return jo.getString("synonyms");
    }

    public String getAntonym(String string) throws JSONException {

        JSONObject jo = new JSONObject(string);

        return jo.getString("antonyms");
    }

    public String getRhymes(String string) throws JSONException {

        JSONObject jo = new JSONObject(string);

        return jo.getString("definitions");
    }

    public String getSentence(String string) throws JSONException {

        JSONObject jo = new JSONObject(string);

        return jo.getString("sentences");
    }

    public String getSyllables(String string) throws JSONException {

        JSONObject jo = new JSONObject(string);

        return jo.getString("syllables");
    }
}
