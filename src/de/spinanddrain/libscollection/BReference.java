package de.spinanddrain.libscollection;

import java.io.File;
import java.io.IOException;

import de.spinanddrain.logging.Log;
import de.spinanddrain.logging.LogType;
import de.spinanddrain.updater.Updater;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BReference extends Plugin {
	
	protected static BReference instance;
	private Log log;
	
	private boolean check;
	
	@Override
	public void onEnable() {
		instance = this;
		log = new Log(getProxy().getConsole(), getProxy().getLogger(), LogType.RAW);
		try {
			io();
		} catch (IOException e1) {
			log.log("§7[§6LibsCollection§7] §cAn §cerror §coccurred §c(" + e1.getMessage() + ")");
		}
		if(check) {
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
	}

	public Log getLog() {
		return log;
	}
	
	private void io() throws IOException {
		File f = new File("plugins/LibsCollection/config.yml");
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		f.createNewFile();
		Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
		if(!cfg.contains("updater")) {
			cfg.set("updater", true);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, f);
		}
		this.check = cfg.getBoolean("updater");
	}
	
}
