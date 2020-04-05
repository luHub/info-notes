package coderunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class CodeRunnerIO {

	
	public static void createCodeFile(Path parentPath, CodeDTO codeDTO) throws IOException {
		Path path = Paths.get(parentPath.toString(),codeDTO.getFileName()+codeDTO.getLanguage().getExtension());
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(path, charset);
		writer.write(codeDTO.getCodeText(), 0, codeDTO.getCodeText().length());
		writer.flush();
		writer.close();
	}
	
	public static CodeDTO readCodeFile(Path path) throws IOException {
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		StringBuilder stringFromFile = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringFromFile.append(line);
		}
		reader.close();
		CodeDTO codeDTO = new CodeDTO();
		codeDTO.setCodeText(stringFromFile.toString());
		return codeDTO;
	}
	
	
	public static void createConfigFile(Path parentPath, CodeDTO codeDTO) throws IOException{
		String codeRunnerConfig = JsonDtoCodeRunnerConfigConverter
				.convertCompileInstructiosnToJson(codeDTO.getConfigDTO());
		Path path = Paths.get(parentPath.toString(),"config.json");
		if (Files.exists(path)) {
			Files.delete(path);
		}
		Files.createFile(path);
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(path, charset);
		writer.write(codeRunnerConfig, 0, codeRunnerConfig.length());
		writer.flush();
		writer.close();
	}
	
	public static ConfigDTO readConfigFile(Path path) throws IOException {
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		StringBuilder stringFromFile = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringFromFile.append(line);
		}
		reader.close();
		ConfigDTO configDTO = JsonDtoCodeRunnerConfigConverter.convertToObject(stringFromFile.toString());
		return configDTO;
	}
	
	
}