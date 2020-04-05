package coderunner;

public class ConfigDTO {

	private String projectName;
	private Language language;
	private CompileInstructions compileInstructions;
	private RunInstructions runInstruccions;
	private String fileName;

	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public CompileInstructions getCompileInstructions() {
		return compileInstructions;
	}
	public void setCompileInstructions(CompileInstructions compileInstructions) {
		this.compileInstructions = compileInstructions;
	}
	public RunInstructions getRunInstruccions() {
		return runInstruccions;
	}
	public void setRunInstruccions(RunInstructions runInstruccions) {
		this.runInstruccions = runInstruccions;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compileInstructions == null) ? 0 : compileInstructions.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + ((runInstruccions == null) ? 0 : runInstruccions.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ConfigDTO))
			return false;
		ConfigDTO other = (ConfigDTO) obj;
		if (compileInstructions == null) {
			if (other.compileInstructions != null)
				return false;
		} else if (!compileInstructions.equals(other.compileInstructions))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (runInstruccions == null) {
			if (other.runInstruccions != null)
				return false;
		} else if (!runInstruccions.equals(other.runInstruccions))
			return false;
		return true;
	}
}
