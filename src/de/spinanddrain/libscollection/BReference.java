package de.spinanddrain.libscollection;

import de.spinanddrain.logging.Log;
import de.spinanddrain.logging.LogType;
import de.spinanddrain.updater.Updater;
import net.md_5.bungee.api.plugin.Plugin;

public class BReference extends Plugin {
	
	protected static BReference instance;
	private Log log;
	
	@Override
	public void onEnable() {
		instance = this;
		log = new Log(getProxy().getConsole(), getProxy().getLogger(), LogType.RAW);
		Updater u = Libraries.getPluginUpdaterFor(getDescription().getVersion());
		log.log("§7[§6LibsCollection§7] §eChecking §efor §eupdates...");
		try {
			if(u.isAvailable())
				log.log("§7[§6LibsCollection§7] §eA §enewer §eversion §eis §eavailable: §b" + u.getLatestVersion());
			else
				log.log("§7[§6LibsCollection§7] §eNo §eupdates §efound. §eYou §eare §erunning §ethe §elatest §eversion.");
		} catch(Exception e) {
			log.log("§7[§6LibsCollection§7] §cAn §cerror §coccurred §c(" + e.getMessage() + ")");
		}
	}

	public Log getLog() {
		return log;
	}
	
}
