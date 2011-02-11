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
package org.deri.iris.evaluation.topdown;

import org.deri.iris.EvaluationException;

/**
 * Exception to be thrown at a certain nesting level. Used by top-down
 * evaluation strategies (SLD, SLDNF) to break out of infinite/very big
 * loops.
 * 
 * @author gigi
 *
 */
public class MaximumRecursionDepthReachedException extends EvaluationException {

	public MaximumRecursionDepthReachedException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 6986765471629696124L;

}
