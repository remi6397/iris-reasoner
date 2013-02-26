package at.sti2.streamingiris;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profiler {

	private static String resultsPath = "D:\\workspaces\\workspace_iris\\results\\";
	private static Map<UUID, Long> startTimeMap = new HashMap<UUID, Long>();
	private static File outputFile;
	private static FileWriter fileWriter;

	public static void addStartTime(UUID uuid, long startTime) {
		startTimeMap.put(uuid, new Long(startTime));
	}

	public static void addEndTime(UUID uuid, long endTime) {
		try {
			fileWriter = new FileWriter(outputFile, true);

			long time = endTime - startTimeMap.get(uuid);
			fileWriter.append(time + ",");

			fileWriter.flush();
			fileWriter.close();

			startTimeMap.remove(uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(String datalogProgramFileName,
			String inputStreamFileName, long delay, long timeWindow) {
		try {
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
			fileWriter.append("\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
