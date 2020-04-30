package de.spinanddrain.updater.requests.provider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import de.spinanddrain.updater.exception.RareException;
import de.spinanddrain.updater.exception.UpdateException;

public class Spigot implements ExecutionProvider {
	
	private Plugin plugin;
	private String name;
	private File me;
	
	/**
	 * Creates a new instance of the default provider for downloading
	 * plugins on a Spigot server with the specified affected plugin
	 * and the plugins current file name prefix.
	 * 
	 * @param plugin the affected plugin (that gets updated)
	 */
	public Spigot(Plugin plugin) {
		this(plugin, plugin.getDescription().getName());
	}
	
	/**
	 * Creates a new instance of the default provider for downloading
	 * plugins on a Spigot server with the specified affected plugin
	 * and the specified new file name prefix.
	 * 
	 * @param plugin the affected plugin (that gets updated)
	 */
	public Spigot(Plugin plugin, String name) {
		try {
			me = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			this.plugin = plugin;
			this.name = name;
		} catch (URISyntaxException e) {
			throw new RareException();
		}
	}
	
	@Override
	public String preparation() {
		Bukkit.getPluginManager().disablePlugin(plugin);
		URLClassLoader loader = (URLClassLoader) plugin.getClass().getClassLoader();
		try {
//			((JarFile) loader.getClass().getField("jar").get(loader)).close();
			loader.close();
		} catch (IllegalArgumentException | SecurityException
				| IOException e) {
			e.printStackTrace();
		}
		me.delete();
		return name;
	}
	
	@Override
	public void postProcessing(File f) {
		try {
			plugin = Bukkit.getPluginManager().loadPlugin(f);
			Bukkit.getPluginManager().enablePlugin(plugin);
		} catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e) {
			throw new UpdateException(e.getMessage());
		}
	}
	
}
