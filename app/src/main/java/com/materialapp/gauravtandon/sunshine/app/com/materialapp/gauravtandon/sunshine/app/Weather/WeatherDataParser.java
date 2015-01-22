package com.materialapp.gauravtandon.sunshine.app.com.materialapp.gauravtandon.sunshine.app.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gaurav Tandon on 5/01/2015.
 */
public class WeatherDataParser {
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {
        JSONObject reader = new JSONObject(weatherJsonStr);
        JSONArray list = reader.getJSONArray("list");

        JSONObject daysJSONObject = list.getJSONObject(dayIndex);
        JSONObject tempDetailObject = daysJSONObject.getJSONObject("temp");
        Double maxTemp = tempDetailObject.getDouble("max");
        return maxTemp;
    }
}
