package ru.ifmo.mobdev.androidenglishlearning;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordTranslate {
	public static String translate(String word) {
		String urlName = "http://translate.yandex.net/api/v1/tr.json/translate?lang=en-ru&text="
				+ word;
		try {
			URL url = new URL(urlName);
			URLConnection connect = url.openConnection();
			InputStream is = connect.getInputStream();
			String ans = new BufferedReader(new InputStreamReader(is))
					.readLine();
			is.close();
			JSONObject json = new JSONObject(ans);
			JSONArray jsonResult = json.getJSONArray("text");
			String res = jsonResult.getString(0);
			return res;
		} catch (Exception e) {
			return "Error!";
		}
	}
}
