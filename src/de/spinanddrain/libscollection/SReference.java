package de.spinanddrain.libscollection;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.spinanddrain.logging.Log;
import de.spinanddrain.logging.LogType;
import de.spinanddrain.updater.Updater;

public class SReference extends JavaPlugin {

	protected static SReference instance;
	private Log log;
	
	private boolean check;
	
	@Override
	public void onEnable() {
		instance = this;
		log = new Log(getServer().getConsoleSender(), getServer().getLogger(), LogType.RAW);
		try {
			io();
		} catch (IOException e1) {
			log.log("§7[§6LibsCollection§7] §cAn error occurred (" + e1.getMessage() + ")");
		}
		if(check) {
			Updater u = Libraries.getPluginUpdaterFor(getDescription().getVersion());
			log.log("§7[§6LibsCollection§7] §eChecking for updates...");
			try {
				if(u.isAvailable())
					log.log("§7[§6LibsCollection§7] §eA newer version is available: §b" + u.getLatestVersion());
				else
					log.log("§7[§6LibsCollection§7] §eNo updates found. You are running the latest version.");
			} catch(Exception e) {
				log.log("§7[§6LibsCollection§7] §cAn error occurred (" + e.getMessage() + ")");
			}
		}
	}

	public Log getLog() {
		return log;
	}
	
	private void io() throws IOException {
		File f = new File("plugins/LibsCollection/config.yml");
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		f.createNewFile();
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.options().copyDefaults(true);
		cfg.addDefault("updater", true);
		cfg.save(f);
		this.check = cfg.getBoolean("updater");
	}
	
}
