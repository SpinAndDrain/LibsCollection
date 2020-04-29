package de.spinanddrain.updater.requests;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonParser;

import de.spinanddrain.updater.exception.HttpRequestException;

public class RequestHelper {

	public static final String AGENT = "Sadreqh";

	private URL url;
	
	/**
	 * Creates a new instance with the specified URL.
	 * 
	 * @param url base for http connection
	 */
	private RequestHelper(URL url) {
		this.url = url;
	}
	
	/**
	 * Sends a new request based on the specified URL.
	 * 
	 * @return a parsed JSON value of the responded content
	 * @throws IOException
	 */
	public Object sendRequest() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", AGENT);
		if(connection.getResponseCode() == 200)
			return new JsonParser().parse(new InputStreamReader(connection.getInputStream()));
		throw new HttpRequestException("request failed");
	}
	
	/**
	 * Creates and converts a new instance with the specified URL as string.
	 * 
	 * @param url the URL as string
	 * @return the new instance
	 */
	public static RequestHelper of(String url) {
		try {
			return new RequestHelper(new URL(url));
		} catch (MalformedURLException e) {
			throw new HttpRequestException(e.getMessage());
		}
	}
	
}
