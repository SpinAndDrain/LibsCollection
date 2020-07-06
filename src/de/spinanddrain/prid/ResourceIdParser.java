package de.spinanddrain.prid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ResourceIdParser {
	
	private URL url;
	
	/**
	 * Creates a new instance with the specified URL.
	 * 
	 * @param url
	 */
	private ResourceIdParser(URL url) {
		this.url = url;
	}
	
	/**
	 * 
	 * @param key
	 * @return the stored resource id by the specified project key
	 * @throws IOException
	 */
	public int getResourceIdByKey(String key) throws IOException {
		return parseId(key, read());
	}
	
	/**
	 * Reads the content of the specified URL file.
	 * 
	 * @return the read content
	 * @throws IOException
	 */
	public String read() throws IOException {
		return new BufferedReader(new InputStreamReader(url.openStream())).readLine();
	}

	public static int parseId(String resourceKey, String content) {
		String[] args = content.split(";");
		for(int i = 0; i < args.length; i++) {
			String[] vp = args[i].split(":");
			if(vp.length != 2)
				throw new IndexOutOfBoundsException();
			if(vp[0].equals(resourceKey))
				return Integer.parseInt(vp[1]);
		}
		return 0;
	}
	
	/**
	 * 
	 * @return the default <code>ResourceIdParser</code> instance
	 */
	public static ResourceIdParser defaultPrid() {
		try {
			return new ResourceIdParser(new URL("http://spinanddrain.bplaced.net/sessions/hotfix/prid"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
