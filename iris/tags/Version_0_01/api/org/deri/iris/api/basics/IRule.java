package org.deri.iris.api.basics;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 17:21:21
 */

public interface IRule extends IHead, IBody{
	
	public boolean isSafe();
	
	public boolean isRectified();
	
	public boolean isCycled();
	
	public boolean isFact();
	
	public boolean isBuiltIn();
}
