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
 * Used to classify nodes.
 * 
 * LINK		A link node which is a special form of a memo node.
 * 			A link node is not evaluated by resolution, but 
 * 			previously computed answers are used to expand the node.
 * 			By trying to expand the link node using resolution,
 * 			the node gets paused.
 * 			
 * ANSWER	A answer node which is a special form of a memo node.
 * 			A answer node is evaluated by resolution. The answers
 * 			are stored in the corresponding entry in the memo table.
 * 
 * NORMAL	A ordinary node (not a memo node).
 * 
 * @author gigi
 *
 */
public enum NodeType {
	LINK, ANSWER, NORMAL
}
