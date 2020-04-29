package de.spinanddrain.libscollection;

import org.bukkit.plugin.java.JavaPlugin;

import de.spinanddrain.logging.Log;
import de.spinanddrain.logging.LogType;
import de.spinanddrain.updater.Updater;

public class SReference extends JavaPlugin {

	protected static SReference instance;
	private Log log;
	
	@Override
	public void onEnable() {
		instance = this;
		log = new Log(getServer().getConsoleSender(), getServer().getLogger(), LogType.RAW);
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

	public Log getLog() {
		return log;
	}
	
}
