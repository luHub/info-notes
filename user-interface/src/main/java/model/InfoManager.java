package model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import file.ConvertableToJSON;
import file.dto.FileDTO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import info.INFO_TYPE;
import info.InfoCoverMapDTO;
import info.InfoDTO;
import info.InfoIndexDTO;
import info.InfoLayoutDTO;
import info.InfoLayoutListDTO;
import info.InfoMainLayoutDTO;
import ui.InfoPanelController;
import info.MapInfoDTO;

//TODO Split into 3 different managers
//One called UI just to manage the UI layout
//Other called User just to to manage user
public class InfoManager {

	private UserManager userManager = new UserManager();
	private WebModel webModel = new WebModel();
	private final EditMode editMode = new EditMode(this);
	private ListView<InfoInList> infoListView;
	private String ext = "json";

	private InfoPanelController infoPanelController;

	// Creates a concurrent Map and be careful
	private final InfoService<?> infoService = new InfoService<FileDTO<Integer, ? extends ConvertableToJSON>>();
	private Integer lastInfoIdSelected;
	private InfoInList lastInfoInListSelected;

	public InfoManager(InfoPanelController infoPanelController) {
		this.infoPanelController = infoPanelController;
		this.userManager.initUser();
		initializeInfoService();
	}

	public void setInfoListView(ListView<InfoInList> infoView) {
		this.infoListView = infoView;
	}

	public ListView<InfoInList> getInfoListView() {
		return this.infoListView;
	}

	private ChangeListener<InfoInList> managerListener = new ChangeListener<InfoInList>() {
		public void changed(ObservableValue<? extends InfoInList> observable, InfoInList oldValue,
				InfoInList newValue) {
			// Switch List views
			// 1. Get Selected Info
			// TODO Check this ugly cast!
			FileDTO<Integer, MapInfoDTO> currentInfoFile = (FileDTO<Integer, MapInfoDTO>) infoService.getInfoMap()
					.get(newValue.getId());
			// 2. Display it on the Info Pane
			// TODO Add EDIT and READ MODE
			InfoManager.this.editMode.setCurrentInfo(currentInfoFile);
		}
	};

	// TODO Write Test for this method!
	public void createNewInfoFile() {
		// 1 Create a new Info File
		// 2 Get File Last Max ID and add 1
		Set<InfoInList> iif = infoService.getInfoForList();
		OptionalInt maxId = iif.stream().mapToInt(w -> w.getId()).max();
		int fileId = 0;
		if (maxId.isPresent()) {
			fileId = maxId.getAsInt() + 1;
		}
		// 3 Config File
		final Path INFO_PATH = Paths.get(this.userManager.getUserPath().toString(), "info");
		var mapInfoDTO = new MapInfoDTO();
		var infoDTO = new InfoDTO();

		infoDTO.setText("Insert Text Here");
		infoDTO.setType(INFO_TYPE.TEXT);
		mapInfoDTO.getMap().put(0, infoDTO);
		mapInfoDTO.setTitle("INFO");
		var fileDTO = new FileDTO<Integer, ConvertableToJSON>(fileId, INFO_PATH, ext);
		fileDTO.setContend(mapInfoDTO);
		infoService.addInfoFileToSave(fileDTO, false);
	}

	// TODO move to initialization class (a class to create)
	private void initializeInfoService() {
		this.infoService.setInfoManager(this);
		this.infoService.start();
		this.infoService.getInfoFromFiles();
		this.infoService.getInfoCovers();
		this.infoService.getLayoutFromFile();
	}

	public Path getInfoDirectory() {
		return Paths.get(this.userManager.getUserPath().toString(), "info");
	}

	public Path getLayoutConfigDirectory() {
		return Paths.get(this.userManager.getUserPath().toString());
	}

	public Path getInfoFrontCoverDirectory() {
		return Paths.get(this.userManager.getUserPath().toString());
	}

	public InfoPanelController getInfoPanelController() {
		return infoPanelController;
	}

	public void createNewText() {
		Platform.runLater(() -> {
			// 1. Get CurrentInfoDTO
			var infoInList = this.infoListView.getSelectionModel().getSelectedItem();
			// 2. Append a new Key Pair to its end
			FileDTO<Integer, MapInfoDTO> currentInfoFile = infoService.getInfoMap().get(infoInList.getId());
			// TODO a List would be better than a map, do the refactor!
			int mapLastId = currentInfoFile.getContend().getMap().keySet().size();
			var infoDTO = new InfoDTO();
			infoDTO.setType(INFO_TYPE.TEXT);
			// TODO to Regionalization no crazy Strings!
			infoDTO.setText("Add text here");
			currentInfoFile.getContend().getMap().put(mapLastId + 1, infoDTO);
			// TODO Check this part used UPDATE_IO instead of DELETE/CREATE
			// 3. Save File
			this.infoService.deleteFile(currentInfoFile, true);
			this.infoService.addInfoFileToSave(currentInfoFile, true);
			// 4. UpdateDisplay
			this.editMode.setCurrentInfo(currentInfoFile);
		});
	}

	public void deleteInfo() {
		if (lastInfoIdSelected != null && lastInfoInListSelected != null) {
			// 1. Get FileDTO
			FileDTO<Integer, MapInfoDTO> currentInfoFile = infoService.getInfoMap().get(lastInfoInListSelected.getId());
			// 2. Remove Info
			currentInfoFile.getContend().getMap().remove(lastInfoIdSelected);
			// 3. Update FileDTO
			// TODO use a beter way to do this if possible
			this.infoService.deleteFile(currentInfoFile, true);
			this.infoService.addInfoFileToSave(currentInfoFile, true);
			// 4. UpdateDisplay
			Platform.runLater(() -> {
				this.editMode.setCurrentInfo(currentInfoFile);
			});
			this.lastInfoIdSelected = null;
			this.lastInfoInListSelected = null;
		}
	}

	public void deleteFile() {
		// 1. Current File Path
		final Path INFO_PATH = Paths.get(this.userManager.getUserPath().toString(), "info");
		// 2. Id Path
		InfoInList infoInList = this.infoListView.getSelectionModel().getSelectedItem();
		// 3. Create FileDTO
		var fileDTO = new FileDTO<Integer, MapInfoDTO>(infoInList.getId(), INFO_PATH, this.ext);
		this.infoService.deleteFile(fileDTO, false);
	}

	public void setLastInfoSelected(Integer id, InfoInList infoInList) {
		this.lastInfoIdSelected = id;
		this.lastInfoInListSelected = infoInList;
	}

	public void updateInfo(Integer id, InfoInList infoInList, InfoDTO infoDTO) {
		// 1. Get FileDTO
		FileDTO<Integer, MapInfoDTO> currentInfoFile = infoService.getInfoMap().get(infoInList.getId());
		// 2. Remove Info
		currentInfoFile.getContend().getMap().get(id).setText(infoDTO.getText());
		// 3. Update FileDTO
		// TODO use a beter way to do this if possible
		this.infoService.deleteFile(currentInfoFile, true);
		this.infoService.addInfoFileToSave(currentInfoFile, true);
	}

	public void createNewWebView() {
		webModel.createNewWebView(this.infoListView, this.infoService, this.editMode);
	}

	//TODO create laoyut manager Layout Config if this scales to much use another class:
	public InfoLayoutDTO readInfoLayoutDTO(Integer id, InfoInList infoInList) {

		final double heightDefault = 30.0;

		var infoLayoutDTO = new InfoLayoutDTO();
		infoLayoutDTO.setInfoId(id);
		infoLayoutDTO.setInfoFileId(infoInList.getId());
		infoLayoutDTO.setHeight(heightDefault);
		FileDTO<String, InfoLayoutListDTO> layoutFile = infoService.getLayoutInfo();

		Optional<InfoLayoutDTO> optInfoLayoutDTO = layoutFile.getContend().getInfoLayoutList().stream()
				.filter(x -> x.getInfoFileId().equals(infoInList.getId()) && x.getInfoId().equals(id)).findFirst();

		return optInfoLayoutDTO.isPresent() ? optInfoLayoutDTO.get() : infoLayoutDTO;

	}
	//TODO Create layout mananger
	public void updateInfoLayoutDTO(InfoLayoutDTO infoLayotDTO) {
		this.infoService.addInfoLayoutToSave(infoLayotDTO);
	}

	public void updateInfoDetails(FileDTO<Integer, InfoIndexDTO> infoDTO) {

	}

	public void updateCover() {
		this.infoService.updateCover();
	}

	public void updateUI(InfoCoverMapDTO infoCoverMapDTO) {
		Platform.runLater(() -> {
			infoListView.getSelectionModel().selectedItemProperty().removeListener(managerListener);
			infoListView.getItems().clear();
			List<InfoInList> info = new ArrayList<>();
			infoCoverMapDTO.getInfoCovers().forEach((k, v) -> {
				InfoInList infoInList = new InfoInList(v);
				infoInList.setInfoManager(this);
				info.add(infoInList);
			});
			ObservableList<InfoInList> ol = FXCollections.observableList(info);

			if (info.isEmpty()) {
				infoPanelController.getDelteButton().setDisable(true);
				infoPanelController.getDisplayVBox().getChildren().clear();
			}
			this.infoService.getInfoForList().clear();
			this.infoService.getInfoForList().addAll(info);
			infoListView.setItems(ol);
			infoListView.getSelectionModel().selectedItemProperty().addListener(managerListener);
		});
	}
	//TODO move to layout manager
	public InfoMainLayoutDTO readMainLayoutDTO() {
		InfoMainLayoutDTO infoMainLayoutDTO = new InfoMainLayoutDTO();
		FileDTO<Integer, InfoMainLayoutDTO> layoutFile = infoService.getMainLayoutInfo();

		// Check for layout position
		if (layoutFile.getContend() != null && layoutFile.getContend().getSplitPanePos() != 0) {
			infoMainLayoutDTO.setSplitPanePos(layoutFile.getContend().getSplitPanePos());
		} else {
			// TODO Save default values in a other file
			infoMainLayoutDTO.setSplitPanePos(0.20f);
		}
		;
		return infoMainLayoutDTO;
	}

	public void updateDividerPosition(float floatValue) {
		System.out.println("Update divier position!");
	}

}