package com.example.booksearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

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

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static List<Books> fetchBooksData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Books> books = extractFeatureFromJson(jsonResponse);
        return books;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

        private static List<Books> extractFeatureFromJson(String booksJSON) {
            if (TextUtils.isEmpty(booksJSON)) {
                return null;
            }

            List<Books> total_books = new ArrayList<>();


            try {

                JSONObject baseJsonResponse = new JSONObject(booksJSON);

                JSONArray booksArray = baseJsonResponse.getJSONArray("items");

                for (int i = 0; i < booksArray.length(); i++) {

                    JSONObject currentbook =  booksArray.getJSONObject(i);

                    JSONObject volumeInfo = currentbook.getJSONObject("volumeInfo");
                    String book_title = volumeInfo.getString("title");
                    JSONArray author_array = volumeInfo.getJSONArray("authors");
                    String author = author_array.getString(0);
                    Double rating = volumeInfo.getDouble("averageRating");
                    JSONObject image= volumeInfo.getJSONObject("imageLinks");
                    String image_link = image.getString("smallThumbnail");
                    String description = volumeInfo.getString("description");
                    String book_link = volumeInfo.getString("canonicalVolumeLink");


                    Books books= new Books(image_link,book_title,author,rating,book_link,description);

                    total_books.add(books);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the books JSON results", e);
            }

            // Return the list of earthquakes
            return total_books;

    }

}
