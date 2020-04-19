package model;

import java.io.IOException;
import java.nio.file.Path;

import user.User;
import workbook.WorkbookIO;

public class UserManager {
	
	private User user;
	private boolean isModule = true;

	public void initUser() {
		if (isModule) {
			this.user = new User();
			this.user.setUserName("user");
			this.user.setCurrentWorkbook("workbook");
			try {
				WorkbookIO.createWorkbookIfNotExists("user", "workbook");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// TODO: Reference User Object from Workbook. This is a dangerous
			// part so be careful with references and stuff
			// @UserFromWorkBook (We could create this nice annotation and a
			// custom object container)
			// The idea is be able to use "new" and not put all our app inside
			// not Java Core APIs.
		}
	}
	
	public Path getUserPath() {
		return user.path();
	}

	public void setUser(User user) {
		this.user = user;
	}
}
