package coderunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class CodeRunner {
	
	//TODO use this code to compile a program
	public static String compileInCommandLine(CodeDTO codeDTO) throws IOException{
		//Open a console
		String instruction = createInstruction(codeDTO);
		
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(instruction); // you might need the full path
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line;

		while ((line = br.readLine()) != null) {
		    System.out.println(line);
		    sb.append(line);
		}
		//Run the code
		return sb.toString();
	}
	
	
	private static String createInstruction(CodeDTO codeDTO){
		throw new RuntimeException("Not implementet yet");
		//String instruction;
		//return instruction;
	}
	
}