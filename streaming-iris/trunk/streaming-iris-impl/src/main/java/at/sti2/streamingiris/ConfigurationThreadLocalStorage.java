package at.sti2.streamingiris;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility to allocate a configuration object to the current thread.
 */
public class ConfigurationThreadLocalStorage {
	/**
	 * Set this thread's configuration object.
	 * 
	 * @param configuration
	 *            The knowledge-base configuration object.
	 */
	public static void setConfiguration(Configuration configuration) {
		synchronized (mConfigMap) {
			mConfigMap.put(Thread.currentThread(), configuration);
		}
	}

	/**
	 * Get this thread's configuration object.
	 * 
	 * @return The knowledge-base configuration object.
	 */
	public static Configuration getConfiguration() {
		synchronized (mConfigMap) {
			return mConfigMap.get(Thread.currentThread());
		}
	}

	/** The thread to configuration map. */
	private static Map<Thread, Configuration> mConfigMap = new HashMap<Thread, Configuration>();
}
