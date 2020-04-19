package model;

import file.dto.FileDTO;
import info.INFO_TYPE;
import info.InfoDTO;
import info.MapInfoDTO;
import javafx.application.Platform;
import javafx.scene.control.ListView;

public class WebModel {

	public void createNewWebView(ListView<InfoInList> infoListView, InfoService<?> infoService, EditMode editMode) {
		// TODO Auto-generated method stub
		// 1. Get CurrentInfoDTO
		InfoInList infoInList = infoListView.getSelectionModel().getSelectedItem();
		// 2. Append a new Key Pair to its end
		FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
				.get(infoInList.getId());
		// TODO a List would be better than a map, do the refactor!
		int mapLastId = currentInfoFile.getContend().getMap().keySet().size();
		InfoDTO infoDTO = new InfoDTO();
		infoDTO.setType(INFO_TYPE.WEB);
		// TODO add a default no connection web page
		infoDTO.setText("https://www.google.com");
		currentInfoFile.getContend().getMap().put(mapLastId + 1, infoDTO);
		// TODO Check this part used UPDATE_IO instead of DELETE/CREATE
		// 3. Save File
		infoService.deleteFile(currentInfoFile, true);
		infoService.addInfoFileToSave(currentInfoFile, true);
		// 4. UpdateDisplay
		Platform.runLater(() -> {
			editMode.setCurrentInfo(currentInfoFile);
		});
	}
}
