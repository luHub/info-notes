package workbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//Later implement interface and later factory so we can manage workbooks from
//different backends. 
//Mange workbook unly CRUD Operations no more than that
public class WorkbookIO {

	// TODO Do later! ALL in Capslocks
	private static final String questionDirectory = "questions"; // Directory
	//private static final Integer ID_TEXT = 1; // Directory
																	// for now
																	// only
	private static final String codeRunnerDirectory = "code";
	private static final String imageDirectory = "img"; // Text File
	private static final String infoDirectory = "info"; // Text File

	public static void createWorkbookIfNotExists(String user, String workbookName) throws IOException{
		Path path = Paths.get(user,workbookName);
		if (!Files.exists(path)) {
			createWorkbook(user, workbookName);
		}
	}
	
	public static void createWorkbook(String user, String workbookName) throws IOException {
		 
		//Question Directory
		String questionPathString = user + "/" + workbookName + "/" + questionDirectory;
		Path questionPath = Paths.get(questionPathString);
		Files.createDirectories(questionPath);
		
		//Code Runner
		Path codeRunnerPath = Paths.get(questionPathString, "..", codeRunnerDirectory);
		Files.createDirectories(codeRunnerPath);
		
		//Text File Directory
		Path infoPath = Paths.get(questionPathString, "..", infoDirectory); 
		Files.createDirectories(infoPath);
		
		//Image Directory
		Path imagePath = Paths.get(questionPathString, "..", imageDirectory);
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