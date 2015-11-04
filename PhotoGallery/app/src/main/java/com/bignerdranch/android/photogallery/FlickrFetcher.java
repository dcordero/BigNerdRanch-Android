package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetcher {

    private static final String TAG = "FlickrFetcher";

    private static final String API_KEY = "SECRET_KEY_HERE";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {

        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();

            String jsonString = null;

            jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch: " + e);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON: " + e);
        }

        return items;
    }

    public void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {

        JSONObject photosJSONObject = jsonBody.getJSONObject("photos");
        JSONArray photosJSONArray = photosJSONObject.getJSONArray("photo");

        for (int i = 0; i < photosJSONArray.length(); i++) {
            JSONObject photoJSONObject = photosJSONArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJSONObject.getString("id"));
            item.setCaption(photoJSONObject.getString("title"));

            if (!photoJSONObject.has("url_s")) {
                continue;
            }
            item.setUrl(photoJSONObject.getString("url_s"));
            items.add(item);
        }
    }
}