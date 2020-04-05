package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import file.dto.FileDTO;
import info.MapInfoDTO;

public class FileIO {

	//TODO tEst this method!
	public static void createFile(FileDTO<Integer, ? extends ConvertableToJSON> file) throws IOException {
		if (Files.exists(file.getDirectory())) {
			Files.delete(file.getPath());
		}
		Files.createFile(file.getPath());
		Charset charset = Charset.forName("US-ASCII");
		BufferedWriter writer = Files.newBufferedWriter(file.getPath(), charset);
		String jsonInString = JsonConverter.convertToJson(file.getContend());
		writer.write(jsonInString, 0, jsonInString.length());
		writer.flush();
		writer.close();
	}

	public static <T> T readFile(Path path, Class<T> classType)
			throws IOException, JsonParseException, JsonMappingException {
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		String line;
		StringBuilder stringFromFile = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			stringFromFile.append(line);
		}
		reader.close();
		return JsonConverter.convertToObject(stringFromFile.toString(), classType);
	}

	public static Map<Integer, FileDTO<Integer, MapInfoDTO>> readAllInfoFiles(Path path)
			throws JsonParseException, JsonMappingException, IOException {
		Map<Integer, FileDTO<Integer, MapInfoDTO>> allFiles = new HashMap<>();
		DirectoryStream<Path> dirs = Files.newDirectoryStream(path);
		for (Path name : dirs) {
			MapInfoDTO mapInfoDTO = readFile(name, MapInfoDTO.class);
			// Extract Id from FileName:
			Integer id = Integer.valueOf(name.getFileName().toString().replace(".json", ""));
			FileDTO<Integer, MapInfoDTO> fileDTO = new FileDTO<>(id, name,".json");
			fileDTO.setContend(mapInfoDTO);
			allFiles.put(fileDTO.getId(), fileDTO);
		}
		return allFiles;
	}

	public static boolean deleteFile(FileDTO fileDTO) throws IOException {
		Path path = fileDTO.getPath();
		if (Files.exists(path)) {
			Files.delete(path);
			return true;
		}
		return false;
	}
}