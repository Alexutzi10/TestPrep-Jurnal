package com.example.jurnal.network;

import android.util.Log;

import com.example.jurnal.data.DateConverter;
import com.example.jurnal.data.Jurnal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONParser {
    public static List<Jurnal> getFromJson(String json) {
        List<Jurnal> jurnale = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONObject details = root.getJSONObject("details");
            JSONArray datasets = details.getJSONArray("datasets");

            for (int i = 0; i < datasets.length(); i++) {
                JSONObject array = datasets.getJSONObject(i);
                JSONObject object = array.getJSONObject("jurnal");

                int expense = object.getInt("expenses");
                String destination = object.getString("destination");
                String dateString = object.getString("departureDate");

                Date date;
                try {
                    date = DateConverter.toDate(dateString);
                } catch (Exception ex) {
                    Log.e("parser", "Error when trying to parse the date");
                    continue;
                }

                Jurnal jurnal = new Jurnal(expense, destination, date);
                jurnale.add(jurnal);
            }
            return jurnale;
        } catch (JSONException ex) {
            Log.e("parser", "Error when trying to parse the json");
        }
        return new ArrayList<>();
    }
}
