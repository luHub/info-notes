package filetest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import file.ConvertableToJSON;
import file.FileIO;
import file.dto.FileDTO;
import info.INFO_TYPE;
import info.InfoDTO;
import info.InfoLayoutDTO;
import info.InfoLayoutListDTO;
import info.MapInfoDTO;
import workbook.WorkbookIO;


/**
 * Functional Tests for Entire Workbook
 * @author mey
 *
 */
public class FilesTest {
	
	private static final String user = "foo";
	private static final String workbookName = "MyFirstCodeHomeWork";
	private static final String questions = "questions";
	private static final String codeRunnerDirectory = "code";
    private static final String infoDirectory = "info";
    private static final String imageDirectory = "img";
	
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

	/**
	 * Creates the bare bones of a new workbook divided into
	 * question, info, code directories. 
	 * @throws IOException
	 */
	@Test
	public void createDirectoriesForWorkbook() throws IOException {
		WorkbookIO.createWorkbook(user, workbookName);
		Path userPath = Paths.get(user);
		Path workbookPath = Paths.get(user, workbookName);
		Path imagePath = Paths.get(user,workbookName,imageDirectory);
		Path questionPath = Paths.get(user,workbookName,questions);
		Path infoPath = Paths.get(user,workbookName,infoDirectory);
		
		Path codePath = Paths.get(user,workbookName,codeRunnerDirectory);

		FileVisitor<? super Path> visitor = new FileVisitor<Path>() { 

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (dir.getFileName().toString().equals(user)) {
					Path foundUserPath = Paths.get(dir.toString()).normalize();
					assertEquals(foundUserPath.toString(), userPath.toString());
					return FileVisitResult.CONTINUE;
				}
				
				if (dir.getFileName().toString().equals(questions)) {
					Path founDquestionPath = Paths.get(dir.toString()).normalize();
					assertEquals(founDquestionPath.toString(), questionPath.toString());
					return FileVisitResult.CONTINUE;
				}
				
				if (dir.getFileName().toString().equals(infoDirectory)) {
					Path founPath = Paths.get(dir.toString()).normalize();
					assertEquals(founPath.toString(), infoPath.toString());  
					return FileVisitResult.CONTINUE;  
				}  
				
				if (dir.getFileName().toString().equals(codeRunnerDirectory)) {
					Path foundUserPath = Paths.get(dir.toString()).normalize();
					assertEquals(foundUserPath.toString(), codePath.toString());
					return FileVisitResult.CONTINUE;
				}
				
				if (dir.getFileName().toString().equals(imageDirectory)) {
					Path foundUserPath = Paths.get(dir.toString()).normalize();
					assertEquals(foundUserPath.toString(), imagePath.toString());
					return FileVisitResult.CONTINUE;
				}
				
				if (dir.getFileName().toString().equals(workbookName)) {
					Path foundUserPath = Paths.get(dir.toString()).normalize();
					assertEquals(foundUserPath.toString(), workbookPath.toString());
					return FileVisitResult.CONTINUE;
				}
			
				throw new AssertionFailedError(); 
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		};
		
		Files.walkFileTree(userPath, visitor);
	}
	

	/**
	 * Creates an Info File inside info folder
	 * @throws IOException 
	 */
	@Test
	public void createInfoFile() throws IOException{
		//1. Creates a workbook
		WorkbookIO.createWorkbook(user, workbookName);
		//2. Create an Infofile		
		InfoDTO info = new InfoDTO();
		info.setText("Hello Text!");
		info.setType(INFO_TYPE.TEXT);	 
		final Integer fileId=1;
		final Integer position = 1;
		final String ext = "json";
		final MapInfoDTO mapInfoDTO = new MapInfoDTO();
		mapInfoDTO.getMap().put(position, info);
		Path infoPath = Paths.get(user,workbookName,infoDirectory); 
		FileDTO<Integer,ConvertableToJSON> fileDTO = new FileDTO<Integer, ConvertableToJSON>(fileId, infoPath, ext);
		fileDTO.setContend(mapInfoDTO);
		FileIO.createFile(fileDTO);  
		//3. Read Created Info file
		Path infoFilePath = Paths.get(infoPath.toString(), fileId.toString()+"."+ext);
		MapInfoDTO retrievedFile = FileIO.readFile(infoFilePath,MapInfoDTO.class); 
		//4. Asserts
		assertEquals(info.getText(),retrievedFile.getMap().get(position).getText()); 
	}
	
	@Test
	public void getInfoFileListFromWorkbook() throws IOException{
		//1. Creates a workbook
		WorkbookIO.createWorkbook(user, workbookName);
		//2. Creates 10 info files		
		//3. Reads Folder and retrieves expected list
		//4. assert expected list
	}
	
	@Test
	public void readInfoLayoutDTO() throws IOException{
		//1. Creates a workbook
				WorkbookIO.createWorkbook(user, workbookName);
		Path infoPath = Paths.get(user, workbookName,infoDirectory);
		//2. InfoLatoutStuff
		InfoLayoutListDTO illDTO = new InfoLayoutListDTO();
		InfoLayoutDTO infoLayoutDTO = new InfoLayoutDTO();
		infoLayoutDTO.setInfoId(1);
		infoLayoutDTO.setInfoFileId(1);
		illDTO.getInfoLayoutList().add(infoLayoutDTO);
		FileDTO fileDTO = new FileDTO<Integer,InfoLayoutListDTO>(0,infoPath,".json");
		fileDTO.setPath(infoPath);
		fileDTO.setContend(illDTO);
		//InfoIO.createLayoutInfoFile(fileDTO);
		FileIO.createFile(fileDTO);
		
		InfoLayoutListDTO illDTORetrieved =FileIO.readFile(infoPath, InfoLayoutListDTO.class);
		
		assertEquals(illDTORetrieved.getInfoLayoutList().get(0).getInfoId(),infoLayoutDTO.getInfoId());
		
	}	
}


