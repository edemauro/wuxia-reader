package com.example.ericdemauro.wuxiareader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public final class QueryUtils {
    private static final String LOG_TAG = "utils";

    private QueryUtils() {}

    public static List<Entry> fetchEntries(String webUrl) {
        URL url = createUrl(webUrl);

        String xmlResponse = "";

        try {
            xmlResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlToJson xmlToJson = new XmlToJson.Builder(xmlResponse).build();

        JSONObject jsonObject = xmlToJson.toJson();
        JSONArray entries = null;

        List<Entry> entriesList = new ArrayList<>();

        try {
            entries = jsonObject.getJSONObject("rss")
                    .getJSONObject("channel")
                    .getJSONArray("item");

            for(int i = 0; i < entries.length(); i++) {
                JSONObject entryData = entries.getJSONObject(i);

                Entry tmp = new Entry(entryData.getString("title"),
                        entryData.getString("link"));

                entriesList.add(tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return entriesList;
    }

    private static URL createUrl(String webUrl) {
        URL url =  null;

        try {
            url = new URL(webUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String xmlResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            xmlResponse = readFromStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }

            if(inputStream != null) {
                inputStream.close();
            }
        }

        return xmlResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }
}
