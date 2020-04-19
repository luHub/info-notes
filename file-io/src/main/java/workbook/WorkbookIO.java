package workbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//Later implement interface and later factory so we can manage workbooks from
//different backends. 
//Mange workbook unly CRUD Operations no more than that
public class WorkbookIO {

	private static final String QUESTIONS_DIRECTORY = "questions"; // Directory
	private static final String CODE_RUNNER_DIRECTORY = "code";
	private static final String IMAGE_DIRECTORY = "img"; // Text File
	private static final String INFO_DIRECTORY = "info"; // Text File

	public static void createWorkbookIfNotExists(String user, String workbookName) throws IOException{
		Path path = Paths.get(user,workbookName);
		if (!Files.exists(path)) {
			createWorkbook(user, workbookName);
		}
	}
	
	public static void createWorkbook(String user, String workbookName) throws IOException {
		 
		//Question Directory
		String questionPathString = user + "/" + workbookName + "/" + QUESTIONS_DIRECTORY;
		Path questionPath = Paths.get(questionPathString);
		Files.createDirectories(questionPath);
		
		//Code Runner
		Path codeRunnerPath = Paths.get(questionPathString, "..", CODE_RUNNER_DIRECTORY);
		Files.createDirectories(codeRunnerPath);
		
		//Text File Directory
		Path infoPath = Paths.get(questionPathString, "..", INFO_DIRECTORY); 
		Files.createDirectories(infoPath);
		
		//Image Directory
		Path imagePath = Paths.get(questionPathString, "..", IMAGE_DIRECTORY);
		Files.createDirectories(imagePath);
	}

	/**
	 * Scans Workbook folder
	 * 
	 * @param user
	 * @param workbookName
	 */
	public void readWorkbook(String user, String workbookName) {
	}
	
	
}