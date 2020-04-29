package de.spinanddrain.libscollection;

import de.spinanddrain.prid.ResourceIdParser;
import de.spinanddrain.updater.Updater;

public class Libraries {
	
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
	
	protected static Updater getPluginUpdaterFor(String version) {
		return new Updater(ResourceIdParser.defaultPrid().getResourceIdByKey("de.spinanddrain.libscollection"), version);
	}
	
}
