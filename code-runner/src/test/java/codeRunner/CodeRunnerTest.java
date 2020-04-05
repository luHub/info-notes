package codeRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import coderunner.CodeDTO;
import coderunner.CodeRunnerCreator;
import coderunner.CompileInstructions;
import coderunner.ConfigDTO;
import coderunner.Language;
import coderunner.RunInstructions;
import coderunner.WorkingSpaceDTO;



public class CodeRunnerTest {
	
	@AfterAll
	public void after() throws IOException{
		Path userPath = Paths.get(user);
		deleteFiles(userPath);
	}

	private void deleteFiles(Path path) throws IOException {
		//Recursively Delete All SubDirectories
		File currentDirectory = path.toFile();
		//Base Case
		if (currentDirectory.list()==null || currentDirectory.list().length == 0) {
			Files.deleteIfExists(path);
		}
		//Go to each subfolder
		else {
			List<File> subDirectories = Arrays.asList(currentDirectory.listFiles());
			subDirectories.stream().forEach((subdirectory) -> {
				Path subdirPath = Paths.get(subdirectory.getPath());
				try {
					if(Files.exists(subdirPath)){
						deleteFiles(subdirPath);	
					}
					if(Files.exists(path)){
						deleteFiles(path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	private static final String workbookName = "MyFirstCodeHomeWork";
	private static final String user = "foo";
	//TODO
	@Test
	public void runJavaProgramTest() throws IOException{
		//Create a CodeRunner Project
		//Run Instructions
		//String compileInstruction = 
		//CodeRunner.excecuteCodeInConsole();
	}
	
	@Test
	public void createCodeRunnerProject(){}

	@Test
	public void createCodeRunnerSource() throws IOException {
		String projectName="myJavaFirstProject";
		CodeDTO javaHelloWorld = new CodeDTO();
		javaHelloWorld.setFileName("HelloWorld");
		Language lang = new Language();
		lang.setName("java");
		lang.setExtension(".java");
		javaHelloWorld.setLanguage(lang);
		
		WorkingSpaceDTO workingSpaceInfo = new WorkingSpaceDTO();
		workingSpaceInfo.setUserName(user);
		workingSpaceInfo.setCurrentWorkbook(workbookName);
		workingSpaceInfo.setCurrentType("codeRunner");
		javaHelloWorld.setWorkingSpaceInfo(workingSpaceInfo);
		javaHelloWorld.setProjectName(projectName);

		javaHelloWorld
				.setCodeText("public static void main(String[] args){" + "System.out.print(\"Hello Wordl!\");" + "}");
		CodeRunnerCreator.createCodeRunner(javaHelloWorld);
		
		//Find File Assert Extension
		CodeDTO readDTO = new CodeDTO();
		readDTO.setFileName("HelloWorld");
		readDTO.setLanguage(lang);
		readDTO.setProjectName(projectName);
		readDTO.setWorkingSpaceInfo(workingSpaceInfo);
		
		CodeDTO readJavaHelloWorld = CodeRunnerCreator.readCodeRunner(readDTO);
		//Assert String
		assertEquals(readJavaHelloWorld.getCodeText(), javaHelloWorld.getCodeText());
		
	}
	
	@Test
	public void createCodeRunnerConfig() throws IOException {

		String projectName = "myJavaFirstProject";

		CodeDTO javaHelloWorld = new CodeDTO();
		javaHelloWorld.setFileName("HelloWorld");
		Language lang = new Language();
		lang.setName("java");
		lang.setExtension(".java");
		javaHelloWorld.setLanguage(lang);

		WorkingSpaceDTO workingSpaceInfo = new WorkingSpaceDTO();
		workingSpaceInfo.setUserName(user);
		workingSpaceInfo.setCurrentWorkbook(workbookName);
		workingSpaceInfo.setCurrentType("codeRunner");
		javaHelloWorld.setWorkingSpaceInfo(workingSpaceInfo);
		javaHelloWorld.setProjectName(projectName);

		// Create Compile and run config file called config.json
		CompileInstructions compileInstructions = new CompileInstructions();
		compileInstructions
				.setInstructions("javac " + javaHelloWorld.getFileName() + javaHelloWorld.getLanguage().getExtension());
		RunInstructions runInstructions = new RunInstructions();
		runInstructions.setRunInstructions(
				"java " + javaHelloWorld.getFileName() + javaHelloWorld.getLanguage().getExtension());
		javaHelloWorld.setCompileInstructions(compileInstructions);
		javaHelloWorld.setRunInstruccions(runInstructions);

		CodeRunnerCreator.createConfigFile(javaHelloWorld);
		CodeDTO readConfigDTO = new CodeDTO();

		readConfigDTO.setProjectName(projectName);
		readConfigDTO.setWorkingSpaceInfo(workingSpaceInfo);
		ConfigDTO configDTO = CodeRunnerCreator.readConfigFile(readConfigDTO);
		ConfigDTO sentConfigDTO = javaHelloWorld.getConfigDTO();

		assertEquals(configDTO, sentConfigDTO);

	} 
	
	//TODO
	@Test
	public void editCode(){
		
	}
	
	//TODO
	@Test
	public void deleteProject(){
		
	}
	
	
}
