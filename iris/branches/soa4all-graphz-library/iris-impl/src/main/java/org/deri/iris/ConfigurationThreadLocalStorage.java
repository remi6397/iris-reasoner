/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility to allocate a configuration object to the current thread.
 */
public class ConfigurationThreadLocalStorage
{
	/**
	 * Set this thread's configuration object.
	 * @param configuration The knowledge-base configuration object.
	 */
	public static void setConfiguration( Configuration configuration )
	{
		synchronized( mConfigMap )
        {
	        mConfigMap.put( Thread.currentThread(), configuration );
        }
	}
	
	/**
	 * Get this thread's configuration object.
	 * @return The knowledge-base configuration object.
	 */
	public static Configuration getConfiguration()
	{
		synchronized( mConfigMap )
        {
	        return mConfigMap.get( Thread.currentThread() );
        }
	}
	
	/** The thread to configuration map. */
	private static Map<Thread, Configuration> mConfigMap = new HashMap<Thread, Configuration>();
}
