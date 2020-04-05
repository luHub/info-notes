package workspace;

public class WorkingSpaceDTO {
	
	private String userName;
	private String currentSpace;
	private String type;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCurrentWorkbook() {
		return currentSpace;
	}

	public void setCurrentWorkbook(String currentSpace) {
		this.currentSpace = currentSpace;
	}

	public void setCurrentType(String type) {
		this.type=type;
	}
	
	public String getCurrentType() {
		return this.type;
	}
}