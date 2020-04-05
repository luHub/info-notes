package coderunner;

public class CodeDTO {

	private String codeText;
	private WorkingSpaceDTO workingSpaceInfo;
	private ConfigDTO configDTO = new ConfigDTO();
	
	public String getCodeText() {
		return codeText;
	}
	
	public void setCodeText(String codeText) {
		this.codeText = codeText;
	}
	
	public Language getLanguage() {
		return configDTO.getLanguage();
	}
	
	public void setLanguage(Language language) {
		this.configDTO.setLanguage(language);;
	}
	
	public CompileInstructions getCompileInstructions() {
		return this.configDTO.getCompileInstructions();
	}
	
	public void setCompileInstructions(CompileInstructions compileInstructions) {
		this.configDTO.setCompileInstructions(compileInstructions);
	}
	
	public RunInstructions getRunInstruccions() {
		return this.configDTO.getRunInstruccions();
	}
	 
	public void setRunInstruccions(RunInstructions runInstruccions) {
		this.configDTO.setRunInstruccions(runInstruccions);
	}

	public String getFileName() {
		return this.configDTO.getFileName();
	}

	public void setFileName(String fileName) {
		this.configDTO.setFileName(fileName);
	}

	public WorkingSpaceDTO getWorkingSpaceInfo() {
		return workingSpaceInfo;
	}

	public void setWorkingSpaceInfo(WorkingSpaceDTO workingSpaceInfo) {
		this.workingSpaceInfo = workingSpaceInfo;
	}

	public String getProjectName() {
		return this.configDTO.getProjectName();
	}

	public void setProjectName(String projectName) {
		this.configDTO.setProjectName(projectName);
	}

	public ConfigDTO getConfigDTO() {
		return configDTO;
	}

	public void setConfigDTO(ConfigDTO configDTO) {
		this.configDTO = configDTO;
	}
}