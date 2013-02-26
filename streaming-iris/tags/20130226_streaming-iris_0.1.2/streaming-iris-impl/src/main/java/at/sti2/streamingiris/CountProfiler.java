package at.sti2.streamingiris;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CountProfiler {

	private static String resultsPath = "D:\\workspaces\\workspace_iris\\results\\";
	private static long count;
	private static File outputFile;
	private static FileWriter fileWriter;

	public static void receivedResult() {
		count++;
	}

	public static void createFile(String datalogProgramFileName,
			String inputStreamFileName, long delay, long timeWindow) {
		try {
			count = 0;
			String fileName = resultsPath + "results_"
					+ System.currentTimeMillis() + ".csv";
			outputFile = new File(fileName);

			outputFile.createNewFile();

			fileWriter = new FileWriter(outputFile);

			fileWriter.append("Ontology," + datalogProgramFileName + ",\n");
			fileWriter.append("Stream," + inputStreamFileName + ",\n");
			fileWriter.append("Delay," + delay + ",\n");
			fileWriter.append("Time window," + timeWindow + ",\n\n");

			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void nextRun() {
		try {
			fileWriter = new FileWriter(outputFile, true);
			fileWriter.append("Results, " + count);
			fileWriter.append("\n");
			fileWriter.flush();
			fileWriter.close();
			count = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
