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
package org.deri.iris.evaluation.topdown.oldt;

/**
 * Represents the state of a node in a top-down proof tree.
 * 
 * This enum could be reduced to PAUSED_N, PAUSED_C and DONE,
 * but for the sake of better debugging output it includes
 * all possible node states.
 * 
 * INITIALIZED
 * EVALUATING
 * PAUSED_N		this is a paused node
 * PAUSED_C		node contains at least one paused child
 * SUCCESS
 * FAILURE
 * DONE			node does not contain paused nodes
 *  
 * @author gigi
 * 
 */
public enum NodeState {
    INITIALIZED, EVALUATING, PAUSED_N, PAUSED_C, SUCCESS, FAILURE, DONE
}