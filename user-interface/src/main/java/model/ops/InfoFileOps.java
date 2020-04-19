package model.ops;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import file.ConvertableToJSON;
import file.FileIO;
import file.dto.FileDTO;
import model.InfoManager;
import info.MapInfoDTO;

public class InfoFileOps implements InfoQueuebale {

	private enum SAVE {
		YES, NO, DELETE
	}
	private ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap;
	private FileDTO<Integer, ? extends ConvertableToJSON> info;
	private SAVE save = SAVE.NO;
	private InfoManager infoManager;

	// TODO Check Why is not accepting Override Annotation???
	public void run() {
		if (save.equals(SAVE.YES)) {
			save = SAVE.NO;
			saveInfo();
		}
		if (save.equals(SAVE.DELETE)) {
			save = SAVE.NO;
			deleteInfo();
		}
		updateAllInfoFromFiles();

	}

	// This methods Updates and initialize the IU.
	// TODO Do this method more granular, it is inefficient but it works ok.
	private void updateAllInfoFromFiles() {
		try {
			// Reads Directory and get all InfoFile:
			final Map<Integer, FileDTO<Integer, MapInfoDTO>> infoMap = FileIO
					.readAllInfoFiles(this.infoManager.getInfoDirectory());
			this.infoMap.putAll(infoMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveInfo() {
		try {
			FileIO.createFile(this.info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	private void deleteInfo() {
		try {
			FileIO.deleteFile(this.info);
			deleteInfoCover(this.info.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteInfoCover(Integer id) {
		FileDTO<Integer, ConvertableToJSON> fileDTO = new FileDTO<>(id,this.infoManager.getInfoFrontCoverDirectory(),"json");
		try {
			FileIO.deleteFile(fileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager = infoManager;
	}

	public void setInfoMap(ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap) {
		this.infoMap = infoMap;
	}

	public void updateInfoToFile(FileDTO<Integer, ? extends ConvertableToJSON> fileDTO) {
		save = save.YES;
		this.info = fileDTO;
	}

	// TODO: Add to Ops Interface
	public void setFileToDelete(FileDTO<Integer,? extends ConvertableToJSON> fileDTO) {
		save = SAVE.DELETE;
		this.info = fileDTO;
	}

	public void saveCover() {
		// TODO Auto-generated method stub
		save = save.YES;
	}


}