package coderunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;



/**
 * Creates the required directories for the App
 * 
 * @author mey
 */
public class CodeRunnerCreator {

	

	//interesting: CreateDirectory wont work if previous directory is not creates but createDirectories will work
	//interesting the difference of "q" and ./q is that ./ will point to root path using Paths.get

	
	
	public static void createCodeRunner(CodeDTO codeDTO) throws IOException{
		Path parentPath = Paths.get(parentPath(codeDTO).toString(),"src");
		Files.createDirectories(parentPath);
		CodeRunnerIO.createCodeFile(parentPath, codeDTO);
	}

	public static CodeDTO readCodeRunner(CodeDTO readDTO) throws IOException {
		Path parentPath = Paths.get(parentPath(readDTO).toString(),"src");
		Path filePath = Paths.get(parentPath.toString(),readDTO.getFileName()+readDTO.getLanguage().getExtension());
		
		CodeDTO codeDTO = CodeRunnerIO.readCodeFile(filePath);
		return codeDTO;
	}
	
	public static void createConfigFile(CodeDTO codeDTO) throws IOException{
		Path parentPath = parentPath(codeDTO);
		Files.createDirectories(parentPath);
		CodeRunnerIO.createConfigFile(parentPath, codeDTO);
	}
	
	
	private static Path parentPath(CodeDTO codeDTO) {
		return Paths.get(codeDTO.getWorkingSpaceInfo().getUserName(),
				codeDTO.getWorkingSpaceInfo().getCurrentWorkbook(), codeDTO.getWorkingSpaceInfo().getCurrentType(),
				codeDTO.getProjectName());
	}

	public static ConfigDTO readConfigFile(CodeDTO codeDTO) throws IOException {
		Path parentPath = Paths.get(parentPath(codeDTO).toString(),"config.json");
		ConfigDTO configDTO = CodeRunnerIO.readConfigFile(parentPath);
		return configDTO;
	}
	
	//TODO 
	public static void createTargetDirectory(){
		
	}
	
	//TODO 
	public static void deleteTargetDirectory(){
		
	}

	
	public void deleteProject(Path path) throws IOException {
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
						deleteProject(subdirPath);	
					}
					if(Files.exists(path)){
						deleteProject(path);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
}