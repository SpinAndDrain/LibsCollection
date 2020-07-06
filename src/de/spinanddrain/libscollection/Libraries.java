package de.spinanddrain.libscollection;

import java.io.IOException;

import de.spinanddrain.logging.Log;
import de.spinanddrain.prid.ResourceIdParser;
import de.spinanddrain.updater.Updater;

public class Libraries {
	
	public static final int LOCAL_RESOURCE_ID = 78115;
	
	private Libraries() {
	}
	
	public static SReference getSpigotReference() {
		if(SReference.instance == null)
			throw new NullPointerException("Plugin was not initialized yet");
		return SReference.instance;
	}
	
	public static BReference getBungeeReference() {
		if(BReference.instance == null)
			throw new NullPointerException("Plugin was not initialized yet");
		return BReference.instance;
	}
	
	protected static Updater getPluginUpdaterFor(String version, boolean forceLRID, Log out) {
		int id = LOCAL_RESOURCE_ID;
		if(!forceLRID) {
			try {
				id = ResourceIdParser.defaultPrid().getResourceIdByKey("de.spinanddrain.libscollection");
			} catch(IOException e) {
				out.log("§7[§6LibsCollection§7] §cCould §cnot §cfetch §cresource §cID: §c" + e.getMessage() + "§c; §cUsing §cLRID §cnow.");
			}
		}
		return new Updater(id, version);
	}
	
}
