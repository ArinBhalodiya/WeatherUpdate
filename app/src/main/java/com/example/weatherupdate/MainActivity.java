package com.example.weatherupdate;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {



    EditText eCity,eCountry;
    TextView eResult;

    Button btngets;

    private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private  final String appid = "0e9485eeaf5402a64eb0d81dcf780ef1";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eCity = findViewById(R.id.eCity);
        eCountry = findViewById(R.id.eCountry);
        eResult = findViewById(R.id.eResults);

    }

    public void getWeatherDetails(View view){
        String tempurl = "";
        String city = eCity.getText().toString().trim();
        String country = eCountry.getText().toString().trim();
        if (city.equals("")) {
            eResult.setText("city field can not be empty!");
        } else {
            if (!country.equals("")) {
                tempurl = url + "+?" + city + "," + country + "&appid=" + appid;
            } else {
                tempurl = url + "+?" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    // Log.d("response",response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");

                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        int cloud = jsonObjectMain.getInt("cloud");

                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");


                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");

                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        eResult.setTextColor(Color.rgb(68, 134, 199));

                        output += "Current weather of" + cityName + "(" + countryName + ")"
                                + "\n Temp: " + df.format(temp) + "°C"
                                + "\n Feels Like :" + df.format(feelslike) + "°C"
                                + "\n Humidity :" + humidity + "%"
                                + "\n Description :" + description
                                + "\n Wind Speed :" + wind + "m/s (meter per second)"
                                + "\n Cloudiness :" + cloud + "%"
                                + "\n Pressure :" + pressure + "hPa";

                        eResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }
}


