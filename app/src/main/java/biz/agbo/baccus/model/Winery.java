package biz.agbo.baccus.model;

import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import biz.agbo.baccus.R;

public class Winery {

    private static final String winesURL = "http://golang.bz/baccus/wines.json";

    private static Winery sInstance = null;

    private List<Wine> mWines = null;

    public static Winery getInstance() {
        if (sInstance == null) {
            try {
                // Permite que se ejecute en el hilo ppal la descarga de vinos a partir de la versión HoneyComb
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ThreadPolicy policy = new ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                sInstance = downloadWines();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sInstance;
    }

    public static boolean isInstanceAvailable() {
        return sInstance != null;
    }

    private static Winery downloadWines() throws IOException, JSONException {
        Winery winery = new Winery();
        winery.mWines = new LinkedList<Wine>();

        URLConnection conn = new URL(winesURL).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONArray wines = new JSONArray(response.toString());

        for (int wineIndex = 0; wineIndex < wines.length(); wineIndex++) {
            String id = null;
            String name = null;
            String type = null;
            String company = null;
            String companyWeb = null;
            String notes = null;
            int rating = 0;
            String origin = null;
            String picture = null;

            JSONObject jsonWine = wines.getJSONObject(wineIndex);
            if (jsonWine.has("name")) {
                id = jsonWine.getString("_id");
                name = jsonWine.getString("name");
                type = jsonWine.getString("type");
                company = jsonWine.getString("company");
                companyWeb = jsonWine.getString("company_web");
                notes = jsonWine.getString("notes");
                rating = jsonWine.getInt("rating");
                origin = jsonWine.getString("origin");
                picture = jsonWine.getString("picture");

                Wine wine = new Wine(id, name, type, picture, company, companyWeb, notes, origin, rating);
                JSONArray jsonGrapes = jsonWine.getJSONArray("grapes");
                for (int grapeIndex = 0; grapeIndex < jsonGrapes.length(); grapeIndex++) {
                    wine.addGrape(jsonGrapes.getJSONObject(grapeIndex).getString("grape"));
                }
                winery.mWines.add(wine);
            }
        }

        return winery;
    }

    public Wine getWine(int index) {
        return mWines.get(index);
    }

    public int getWineCount() {
        return mWines.size();
    }

    public List<Wine> getWineList() {
        return mWines;
    }
}
