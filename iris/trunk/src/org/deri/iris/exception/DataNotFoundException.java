package org.deri.iris.exception;

/**
 * Interface or class description
 *
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   07.12.2005 
 */

public class DataNotFoundException extends java.lang.RuntimeException{
	public DataNotFoundException(){
		super();
	};
	public DataNotFoundException(java.lang.String message){
		super(message);
	};
	public DataNotFoundException(String message, Throwable cause){
		super(message, cause);
	}; 
	public DataNotFoundException(java.lang.Throwable cause){
		super(cause);
	};
}
