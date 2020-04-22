package model;

import java.util.HashMap;

import file.dto.FileDTO;
import info.InfoDTO;
import info.InfoIndexDTO;
import ui.InfoPanelController;
import ui.InfoTextAreaController;
import ui.Loader;
import info.MapInfoDTO;
import ui.WebViewAreaController;


public class EditMode { 
	
	private InfoManager infoManager;
	
	public EditMode(InfoManager infoManager) {
		this.infoManager = infoManager;
	}

	public void setCurrentInfo(FileDTO<Integer, MapInfoDTO> currentInfoFile) {
		//1. Clear Info Pane
		clearInfoPane();
		//2. Display Information File
		InfoInList infoInList = new InfoInList(new InfoIndexDTO(currentInfoFile.getId()));
		addInfoToPane(currentInfoFile.getContend(),infoInList);
	}
	
	private void clearInfoPane(){
		this.infoManager.getInfoPanelController().getDisplayVBox().getChildren().clear();
	}
	
	private void addInfoToPane(MapInfoDTO mapInfoDTO,InfoInList infoInList){
		mapInfoDTO.getMap().entrySet().forEach(es->{
			addSection(es.getKey(),infoInList,es.getValue());
		});
	}
	
	private void addSection(Integer id, InfoInList infoInList, InfoDTO infoDTO){
		switch(infoDTO.getType()){
		case TEXT:
			loadTextPane(id,infoInList,infoDTO);
			break;
		case IMG:
			//Add Img:
			break;
		case WEB:
			//Add Web:
			loadWebPane(id,infoInList,infoDTO);
			break;
		}
	}

	private void loadWebPane(Integer id, InfoInList infoInList, InfoDTO infoDTO) {
		Loader loader = new Loader();
		WebViewAreaController itac = loader.addWebArea(this.infoManager.getInfoPanelController().getDisplayVBox(),infoManager);
		itac.setInfoDTO(id,infoInList, infoDTO);
	}

	private void loadTextPane(Integer id, InfoInList infoInList,final InfoDTO infoDTO) {
		Loader loader = new Loader();
		InfoTextAreaController itac = loader.addTextArea(this.infoManager.getInfoPanelController().getDisplayVBox(),infoManager);
		itac.setInfoDTO(id,infoInList, infoDTO);
	}		
}