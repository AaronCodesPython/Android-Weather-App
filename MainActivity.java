package com.example.realweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView temp;
    TextView status;
    ConstraintLayout constraintLayout;


    public void CallApi(String locName){
        Thread newThread = new Thread(() ->{
            try {
                if(locName.contains(" ")){
                    locName.replace(" ", "%20");
                }
                Log.i("System.out2", locName);
                String URLLINK = String.format("http://api.weatherstack.com/current?access_key=YOURKEY&query=%s",locName);
                URL url = new URL(URLLINK);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                //Check if connect is made
                int responseCode = conn.getResponseCode();

                // 200 OK
                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responseCode);
                } else {

                    StringBuilder informationString = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());

                    while (scanner.hasNext()) {
                        informationString.append(scanner.nextLine());
                    }
                    //Close the scanner
                    scanner.close();

                    //System.out.println(informationString);


                    //JSON simple library Setup with Maven is used to convert strings to JSON
                    JSONParser parse = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parse.parse(informationString.toString());
                    JSONObject CurrentJSON = (JSONObject) jsonObject.get("current");

                    String partString = jsonObject.get("current").toString();
                    String weather_descriptions = CurrentJSON.get("weather_descriptions").toString();
                    String temperature = CurrentJSON.get("temperature").toString();
                    //temperature.replaceAll("\"", " ");
                    weather_descriptions = weather_descriptions.replace("\"","");

                    weather_descriptions = weather_descriptions.replaceAll(Pattern.quote("["), "");
                    weather_descriptions = weather_descriptions.replaceAll(Pattern.quote("]"), "");
                   // weather_descriptions = weather_descriptions.replace("\"\\]","");

                    Log.i("System.out2", jsonObject.toString());
                    Log.i("System.out2", partString);
                    Log.i("System.out2", weather_descriptions);
                    Log.i("System.out2", temperature);

                    temp.setText(temperature+"°C");
                    status.setText(weather_descriptions);



                }

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        newThread.start();

    }

    public void onClick(View view){
        String inputText = editText.getText().toString();
        CallApi(inputText);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build());
        setContentView(R.layout.activity_main);
        constraintLayout = (ConstraintLayout) findViewById(R.id.ContraintLayout);
        editText = findViewById(R.id.editTextTextPersonName);
        temp = (TextView) findViewById(R.id.tempView);
        status = (TextView) findViewById(R.id.statusView);

        int min=0;
        int max=2;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        Log.i("randomNumber", String.valueOf(random_int));
        switch (random_int){
            case 0: constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rain));
            break;
            case 1: constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.sun));
            break;
            case 2: constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.storm));
            break;
        }



    }
}