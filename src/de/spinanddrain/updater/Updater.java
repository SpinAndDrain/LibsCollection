package de.spinanddrain.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonSyntaxException;

import de.spinanddrain.updater.exception.HttpRequestException;
import de.spinanddrain.updater.exception.UpdateException;
import de.spinanddrain.updater.requests.RequestHelper;
import de.spinanddrain.updater.requests.provider.ExecutionProvider;

public class Updater {

	private final String version;
	private final int resource;

	/**
	 * Creates a new instance with the specified resource id and version string.
	 * 
	 * @param resource id
	 * @param version version string
	 */
	public Updater(int resource, String version) {
		this.version = version;
		this.resource = resource;
	}

	/**
	 * 
	 * @return true if a newer version is available (relation of this version with the 'latest' == OLDER)
	 */
	public boolean isAvailable() {
		String newest = this.getLatestVersion();
		if (VersionPattern.isDefault(version) && VersionPattern.isDefault(newest))
			return new VersionPattern(version).isOlderThan(newest);
		else
			return !version.equals(newest);
	}

	/**
	 * 
	 * @return the latest version of this resource
	 */
	public String getLatestVersion() {
		try {
			return new BufferedReader(new InputStreamReader(
					new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openStream()))
							.readLine();
		} catch (IOException e) {
			throw new HttpRequestException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param versionString the version
	 * @param versionSize the number of total versions of this resource ever published
	 * @return the id of the specified version string of this resource
	 */
	public long getVersionId(String versionString, int versionSize) {
		try {
			Object response = RequestHelper
					.of("https://api.spiget.org/v2/resources/" + resource + "/versions?size=" + versionSize)
					.sendRequest();
			JSONArray array = (JSONArray) response;
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = (JSONObject) array.get(i);
				if (o.get("name").equals(versionString))
					return (long) o.get("id");
			}
		} catch (JsonSyntaxException | IOException e) {
			throw new HttpRequestException(e.getMessage());
		}
		return -1;
	}
	
	/**
	 * The version size is set to <code>Integer#MAX_VALUE</code>.
	 * (This confirms that all versions have been retrieved properly)
	 * 
	 * @param versionString the version
	 * @return the id of the specified version string of this resource
	 */
	public long getVersionId(String versionString) {
		return this.getVersionId(versionString, Integer.MAX_VALUE);
	}
	
	/**
	 * Downloads the latest version of this resource with the help of the specified
	 * <code>ExecutionProvider</code>.
	 * 
	 * @param provider
	 */
	public void installLatestVersion(ExecutionProvider provider) {
		try {
			String v = this.getLatestVersion();
			File file = new File("plugins/" + provider.preparation() + v + ".jar");
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			HttpURLConnection connection = (HttpURLConnection) new URL(
					"http://api.spiget.org/v2/resources/" + resource + "/download").openConnection();
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", RequestHelper.AGENT);
			int code = connection.getResponseCode();
			if (code != 200) {
				throw new UpdateException("Download returned status #" + connection.getResponseCode());
			}
			ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
			FileOutputStream output = new FileOutputStream(file);
			output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			output.flush();
			output.close();
			provider.postProcessing(file);
		} catch(IOException e) {
			throw new UpdateException(e.getMessage());
		}
	}

	/**
	 * Downloads the specified version of this resource with the help of the specified
	 * <code>ExecutionProvider</code>.
	 * 
	 * @param version the version to download
	 * @param provider
	 * 
	 * @note This method seems not to work sometimes (server returns response code 503)
	 */
	public void install(String version, ExecutionProvider provider) {
		try {
			boolean latest = version.equals("latest");
			String v = latest ? this.getLatestVersion() : version,
					id = latest ? version : String.valueOf(this.getVersionId(v));
			File file = new File("plugins/" + provider.preparation() + v + ".jar");
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			HttpURLConnection connection = (HttpURLConnection) new URL("http://api.spiget.org/v2/resources/" + resource
					+ "/versions/" + id + "/download").openConnection();
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", RequestHelper.AGENT);
			connection.setInstanceFollowRedirects(true);
			int code = connection.getResponseCode();
			if (code != 302) {
				throw new UpdateException("Download returned status #" + connection.getResponseCode());
			}
			String newUrl = connection.getHeaderField("Location");
			connection = (HttpURLConnection) new URL(newUrl).openConnection();
			connection.setRequestProperty("User-Agent", RequestHelper.AGENT);
			ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
			FileOutputStream output = new FileOutputStream(file);
			output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			output.flush();
			output.close();
			provider.postProcessing(file);
		} catch (IOException e) {
			throw new UpdateException(e.getMessage());
		}
	}

}
