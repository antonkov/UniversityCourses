package ru.ifmo.mobdev.androidenglishlearning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageURLSearcher {
	private final static int countURLs = 10;
	private final static String key = "AIzaSyBwNuVoAqQjlVpbbRfLfwn10AozeTj9b1Q";
	private final static String id = "016864660453193580113:_u2iuy2zjjg";

	public static URL[] getURLs(String request) {
		URL[] res = new URL[countURLs];
		try {
			URL url = new URL("https://www.googleapis.com/customsearch/v1?key="
					+ key + "&cx=" + id + "&q=" + request + "&searchType=image"
					+ "&imgSize=medium" + "&alt=json" + "&num=10");
			URLConnection connection = url.openConnection();
			String s;
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			JSONObject json = new JSONObject(sb.toString());
			JSONArray jsonResult = json.getJSONArray("items");
			for (int i = 0; i < jsonResult.length(); i++) {
				res[i] = new URL(jsonResult.getJSONObject(i).getString("link"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
}
