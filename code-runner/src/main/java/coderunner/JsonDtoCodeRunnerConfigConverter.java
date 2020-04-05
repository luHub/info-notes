package coderunner;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonDtoCodeRunnerConfigConverter {

	public static ConfigDTO convertToObject(String jsonString) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		ConfigDTO compileInstructions = mapper.readValue(jsonString, ConfigDTO.class);
		return compileInstructions;
	}
	
	public static String convertCompileInstructiosnToJson(ConfigDTO compileInstructions) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(compileInstructions);
		return jsonInString;
	}
	
	
	
	
}