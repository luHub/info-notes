package model.ops;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import file.FileIO;
import file.dto.FileDTO;
import model.InfoInList;
import model.InfoManager;
import info.InfoLayoutListDTO;


//This will be called on selection of each INFO DTO
public class InfoLayoutFileOps implements InfoQueuebale {
	
	private enum SAVE{ YES , NO,DELETE}	
	private FileDTO<String,InfoLayoutListDTO> layoutFile; 
	private List<InfoInList> infoForList;
	private SAVE save=SAVE.NO;
	private InfoManager infoManager;
	
	@Override
	public void run() {
		//Updated the layout of Current INFO File
		//Manager->updateLayout->UpdateLayoutDTO (Controller only update from file
		//on start-up and writes to file to during programm execution)
		if(save.equals(SAVE.YES)){
			save=save.NO;
			//Save Layout
			save();
		}
		
			updateAllInfoFromFiles();
	
	}

	private void updateAllInfoFromFiles()  {
		//Get all from direcotry
		try {
			InfoLayoutListDTO updatedLayoutDTO;
			this.layoutFile.setId("layoutConfig");
			this.layoutFile.setExt("json");
			this.layoutFile.setDirectory(this.infoManager.getLayoutConfigDirectory());
			if (Files.exists(this.infoManager.getLayoutConfigDirectory())){
			updatedLayoutDTO = FileIO.readFile(this.layoutFile.getFilePath(),InfoLayoutListDTO.class);
			//layoutFile Reference is Updated
			this.layoutFile.setContend(updatedLayoutDTO);
			}
			else
			{
				this.layoutFile.setContend(new InfoLayoutListDTO());
				FileIO.createFile(this.layoutFile);
			}
				
			//Fix: IOException, JsonParseException, JsonMappingException
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager=infoManager;
	}

	public void setLayoutFile(FileDTO<String, InfoLayoutListDTO> layoutFile) {
		this.layoutFile=layoutFile;
	}
	
	
	public void updateInfoToFile(FileDTO<String, InfoLayoutListDTO> layoutFile) {
		save=save.YES;
		this.layoutFile=layoutFile;
	}

	
	//TODO Redo This
	private void save(){
		try {
			FileIO.deleteFile(this.layoutFile);
			FileIO.createFile(this.layoutFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}