package de.spinanddrain.updater.requests.provider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

import de.spinanddrain.updater.exception.RareException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Bungee implements ExecutionProvider {

	private Plugin plugin;
	private File me;
	private String name;
	
	/**
	 * Creates a new instance of the default provider for downloading
	 * plugins on a BungeeCord proxy with the specified affected plugin
	 * and the plugins current file name prefix.
	 * 
	 * @param plugin the affected plugin (that gets updated)
	 */
	public Bungee(Plugin plugin) {
		this(plugin, plugin.getDescription().getName());
	}
	
	/**
	 * Creates a new instance of the default provider for downloading
	 * plugins on a BungeeCord proxy with the specified affected plugin
	 * and the specified new file name prefix.
	 * 
	 * @param plugin the affected plugin (that gets updated)
	 */
	public Bungee(Plugin plugin, String name) {
		try {
			me = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			this.name = name;
			this.plugin = plugin;
		} catch (URISyntaxException e) {
			throw new RareException();
		}
	}
	
	@Override
	public String preparation() {
		PluginManager pm = ProxyServer.getInstance().getPluginManager();
		pm.unregisterCommands(plugin);
		pm.unregisterListeners(plugin);
		URLClassLoader loader = (URLClassLoader) plugin.getClass().getClassLoader();
		try {
			loader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		me.delete();
		return name;
	}
	
	@Override
	public void postProcessing(File f) {
	}
	
}
