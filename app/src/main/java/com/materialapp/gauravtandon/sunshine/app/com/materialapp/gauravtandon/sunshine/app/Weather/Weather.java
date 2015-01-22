package com.materialapp.gauravtandon.sunshine.app.com.materialapp.gauravtandon.sunshine.app.Weather;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gaurav Tandon on 1/01/2015.
 */
public class Weather {

    public static String getWeatherJSONResponse(String urlStr){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJSONStr = null;
        try{
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream==null){
                //Nothing to do.
                forecastJSONStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine())!=null) {
                buffer.append(line + "\n");
            }
            if(buffer.length()==0){
                //Stream was empty. no point parsing.
                forecastJSONStr = null;
            }
            forecastJSONStr = buffer.toString();
        }
        catch (IOException e){
            Log.e("ForecastFragment", "Error ", e);
            e.printStackTrace();
            forecastJSONStr = null;
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){
                try{
                    reader.close();
                }
                catch (IOException e){
                    Log.e("ForecastFragment", "Error closing stream", e);
                    e.printStackTrace();;
                }
            }
        }
        return forecastJSONStr;
    }

    public static void main(String[] args){
        String jsonResponseStr = getWeatherJSONResponse("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
        System.out.println("JSON output starts *****");
        System.out.print(jsonResponseStr);
        System.out.println("JSON output ends *****");
    }
}
