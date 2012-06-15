package org.deri.iris.demo;

public interface IIrisOutputStreamer {

	/**
	 * Connect to the output streamer.
	 */
	public abstract void connect();

	/**
	 * Streams the output.
	 * 
	 * @param output
	 *            The output to be streamed.
	 */
	public abstract void stream(String output);

	/**
	 * Shutdown the output streamer.
	 */
	public abstract boolean shutdown();

}