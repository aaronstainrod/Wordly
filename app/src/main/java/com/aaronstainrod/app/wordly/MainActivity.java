package com.aaronstainrod.app.wordly;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Variables
    private String[] activities;

    //Views
    private ListView main_drawer_list;
    private DrawerLayout main_drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up navigation drawer
        activities = getResources().getStringArray(R.array.activities);
        main_drawer_list = (ListView) findViewById(R.id.main_left_drawer);
        main_drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, activities));
        main_drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        //Navigation Drawer
        main_drawer_layout = (DrawerLayout) findViewById(R.id.main_drawer_layout);}

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switchActivities(position);
        }
    };

    public void switchActivities(int position){
        Intent intent;

        switch(position) {
            case 1:
                intent = new Intent(this, SynonymsActivity.class);
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(this, AntonymsActivity.class);
                startActivity(intent);
            break;

            case 3:
                intent = new Intent(this, RhymesActivity.class);
                startActivity(intent);
                break;

            case 4:
                intent = new Intent(this, SentencesActivity.class);
                startActivity(intent);
                break;

            case 5:
                intent = new Intent(this, SyllablesActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }
    public void findTheDefinition() {
        EditText userInput = (EditText) findViewById(R.id.main_user_input);
        String userText = userInput.getText().toString();
        //String userTextFormatted = "&sentence=" + userText.replaceAll(" ", "+") + "&json=true";
       // new FetchDefinition().execute(userTextFormatted);
        main_drawer_layout.closeDrawer(main_drawer_list);
    }

    /*
    private class FetchDefinition extends AsyncTask<String,Void,String> {

        public String yodaSpeakString;

        @Override
        protected String doInBackground(String...params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the Words Query
                URL url = new URL("https://yoda.p.mashape.com/yoda?mashape-key=iGYZQBB98omshsfPQkJTcL5Px2v7p1MUqdmjsnRP5Zs0muNedo" + params[0]);
                // Create the request to open YodaSpeak, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) { return null; }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    System.out.println(buffer);
                }
                if (buffer.length() == 0) { return null; }
                //Response from Yoda Speak API
                yodaSpeakString = buffer.toString();
                Log.i(LOG_TAG, yodaSpeakString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error " + e.getMessage(), e);
                return null;
            } finally {
                if (urlConnection != null) { urlConnection.disconnect(); }
                if (reader != null) {
                    try { reader.close(); } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            if (yodaSpeakString == null) { Log.e(LOG_TAG, "Error retrieving YodaSpeak"); }
            return yodaSpeakString;
        }

        protected void onPostExecute(String result) {
            output = result;
            if (listSelection) {
                Fragment fragment = new YodaFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
                listSelection = false;
            } else {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra(ResultActivity.translation, result);
                startActivity(intent);
            }
        }
    }
    */



}
