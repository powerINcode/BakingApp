package com.example.powerincode.bakingapp.utils;

import com.example.powerincode.bakingapp.network.models.Recipe;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */
public class NetworkUtil {

    public static ArrayList<Recipe> getRecipeList()  {
        try {
            ArrayList<Recipe> recipes = new ArrayList<>();
            String response = NetworkUtil.getRequest("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            JSONArray data = new JSONArray(response);

            for (int i = 0; i < data.length(); i++) {
                recipes.add(new Recipe(data.getJSONObject(i)));
            }

            return recipes;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRequest(String url) throws IOException {
        BufferedReader in = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        } finally {
            in.close();
        }
    }
}
