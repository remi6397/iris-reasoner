package at.sti2.streamingiris;

public interface IIrisOutputStreamer {

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