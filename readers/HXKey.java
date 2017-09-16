package readers;

import java.util.HashMap;
import java.util.Map;

public class HXKey {
	
	// ADD NEW KEYS TO THIS LIST = = = = = = = = = = = = = =...
	private static String[] TRACK_THESE_KEYS = {
			"r",
			"d",
			"f",
			"g",
			"b",
			"q",
			"w",
			"t"
	};
	// ... = = = = = = = = = = = = = = = = = = = = = = = = = =
	
	// Hash map with <KEY_AS_STRING, keyPressedState>	
	public static Map<String, Boolean> KEYS = new HashMap<String, Boolean>() {
	   {
		   for (String s : TRACK_THESE_KEYS)
			   put(s, false);
	   }
	};
	
	/**
	 * Returns true if the state of the given key is pressed down.
	 * <p>
	 * @param key - Lowercase letter as a string. Returns false if the key is not mapped.
	 * Example: if (HXKey.isPressed("a")) {// execute };
	 */
	public static Boolean isPressed(String key) {
		if (KEYS.containsKey(key)) {
			return KEYS.get(key);
		}
		return false;
	}
	
	/**
	 * Used to simulate key typed by forcing key pressed to false
	 * <p>
	 * @param key - Lowercase letter as a string.
	 * Example: HXKey.setReleased("a")
	 */
	public static void setReleased(String key) {
		if (KEYS.containsKey(key)) {
			KEYS.put(key, false);
		}
	}
}