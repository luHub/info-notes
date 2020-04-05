package user;

import java.nio.file.Path;
import java.nio.file.Paths;

public class User {

	private String userName;
	private String workbook;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCurrentWorkbook() {
		return workbook;
	}
	
	public void setCurrentWorkbook(String currentWorkbook) {
		this.workbook = currentWorkbook;
	}
	
	public Path path(){
		return Paths.get(userName,workbook);
	}
} 