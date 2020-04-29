package de.spinanddrain.logging.translate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class KeyTranslator {
	
	public static final Map<String, String> KEYS = new StandardKeyTranslations();
	private static final StandardKeyTranslations $ = new StandardKeyTranslations().reversed();
	
	public static final String INFO = $.get(KEYS.get("info")),
			WARN = $.get(KEYS.get("warn")),
			SEVERE = $.get(KEYS.get("severe")),
			BLACK = KEYS.get("black"),
			DARK_BLUE = KEYS.get("darkblue"),
			DARK_GREEN = KEYS.get("darkgreen"),
			DARK_PURPLE = KEYS.get("darkpurple"),
			GOLD = KEYS.get("gold"),
			GRAY = KEYS.get("gray"),
			DARK_GRAY = KEYS.get("darkgray"),
			BLUE = KEYS.get("blue"),
			GREEN = KEYS.get("green"),
			AQUA = KEYS.get("aqua"),
			RED = KEYS.get("red"),
			LIGHT_PURPLE = KEYS.get("lightpurple"),
			YELLOW = KEYS.get("yellow"),
			WHITE = KEYS.get("white");
	
	/**
	 * The message may not change in the console.
	 */
	@Deprecated
	public static final String VEILED = KEYS.get("veiled"),
			BOLD = KEYS.get("bold"),
			STRIKETHROUGH = KEYS.get("strikethrough"),
			UNDERLINED = KEYS.get("underlined"),
			ITALIC = KEYS.get("italic");
	
	public static final String RESET = KEYS.get("reset"),
			NEW_LINE = KEYS.get("nl");
	
	private String message;
	
	/**
	 * 
	 * @param message
	 */
	public KeyTranslator(String message) {
		this.message = message;
	}
	
	/**
	 * 
	 * @return the message, maybe modified
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Filters and removes the level out of the message string.
	 * 
	 * @return Level of message
	 */
	public Level getLevel() {
		Level l = null;
		String[] args = message.split(":");
		switch(args[0].toLowerCase()) {
		case "info":
		default:
			l = Level.INFO;
			break;
		case "warn":
			l = Level.WARNING;
			break;
		case "severe":
			l = Level.SEVERE;
			break;
		}
		message = message.replaceFirst(args[0] + ":", new String());
		return l;
	}
	
	/**
	 * Translates according to the default map. {@link KeyTranslator#KEYS}
	 * 
	 * @return a modified copy of the native message
	 * @see {@link KeyTranslator#translate(Map)}
	 */
	public String translateAll() {
		return this.translate(KEYS);
	}
	
	/**
	 * Translates each key in the message with the specified value.
	 * ('key: Hey' ==> 'value Hey')
	 * 
	 * @param keys map of keys and associated values
	 * @return a modified copy of the native message
	 */
	public String translate(Map<String, String> keys) {
		String res = message;
		for(String k : keys.keySet()) {
			res = res.replace(k + ":", keys.get(k));
		}
		return res;
	}
	
	private static final class StandardKeyTranslations extends HashMap<String, String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private StandardKeyTranslations() {
			put("info", "<?$rpk[info]");
			put("warn", "<?$rpk[warn]");
			put("severe", "<?$rpk[severe]");
			put("black", "§0");
			put("darkblue", "§1");
			put("darkgreen", "§2");
			put("darkaqua", "§3");
			put("darkred", "§4");
			put("darkpurple", "§5");
			put("gold", "§6");
			put("gray", "§7");
			put("darkgray", "§8");
			put("blue", "§9");
			put("green", "§a");
			put("aqua", "§b");
			put("red", "§c");
			put("lightpurple", "§d");
			put("yellow", "§e");
			put("white", "§f");
			put("veiled", "§k");
			put("bold", "§l");
			put("strikethrough", "§m");
			put("underlined", "§n");
			put("italic", "§o");
			put("reset", "§r");
			put("nl", "\n");
		}
		
		private StandardKeyTranslations reversed() {
			clear();
			put("<?$rpk[info]", "info:");
			put("<?$rpk[warn]", "warn:");
			put("<?$rpk[severe]", "severe:");
			return this;
		}
		
	}
	
}
