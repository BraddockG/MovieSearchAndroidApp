package com.gmbprods.gerome.moviesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gerome on 09/02/2017.
 */

public class FetchMovies {

    private static final String TAG = "FetchMovies";
    private static final String API_KEY = "8783017f";
    private static Uri ENDPOINT = Uri.parse("http://www.omdbapi.com/?")
            .buildUpon()

            .build();


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url =  new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public Movie fetchMovie(String query) {
        String url = buildUrl(query);
        return downloadMovie(url);
    }

    private Movie downloadMovie(String url) {

        Movie item = new Movie();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            item = parseMovie(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return item;
    }

    private String buildUrl(String query) {
        Uri.Builder uriBuiler = ENDPOINT.buildUpon()
                .appendQueryParameter("t", query)
                .appendQueryParameter("y", "")
                .appendQueryParameter("plot", "short")
                .appendQueryParameter("r", "json");
        return uriBuiler.build().toString();
    }

    private Movie parseMovie(JSONObject jsonBody ) throws IOException, JSONException {
        Movie item = new Movie();
        item.setTitle(jsonBody.getString("Title"));
        item.setPlot(jsonBody.getString("Plot"));
        item.setID(jsonBody.getString("imdbID"));
        item.setPosterUrl(jsonBody.getString("Poster"));
        item.setPosterBitmap(downloadPoster(item.getPosterUrl()));
        return item;
    }

    private Bitmap downloadPoster(String posterUrl) {
        try {
            if (posterUrl == null) {
                return null;
            }
            byte[] bitmapBytes = new FetchMovies().getUrlBytes(posterUrl);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            return bitmap;
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
        return null;
    }
}
