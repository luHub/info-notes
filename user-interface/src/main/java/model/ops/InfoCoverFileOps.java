package model.ops;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import file.ConvertableToJSON;
import file.FileIO;
import file.dto.FileDTO;
import model.InfoInList;
import model.InfoManager;
import info.InfoCoverMapDTO;
import info.InfoIndexDTO;
import info.MapInfoDTO;

public class InfoCoverFileOps implements InfoQueuebale {

	private enum CRUD {
		CREATE, READ, UPDATE, DELETE,
	}
	private CRUD crud = CRUD.READ;
	private InfoManager infoManager;
	private InfoCoverMapDTO infoCoverMapDTO = new InfoCoverMapDTO(); 
	
	private String infoCover = "infoCover";

	public InfoCoverFileOps(InfoManager infoManager, Set<InfoInList> infoForList,
			ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap) {
		this.infoManager = infoManager;

		if(!infoForList.isEmpty())
		for(InfoInList infoInList:infoForList){
			this.infoCoverMapDTO.getInfoCovers().put(infoInList.getId(), infoInList.getInfoIndexDTO());
		}
		
	}

	public void setInfoFromList(List<InfoInList> infoForList) {
		this.infoCoverMapDTO.getInfoCovers().clear();
		if(!infoForList.isEmpty())
			for(InfoInList infoInList:infoForList){
				this.infoCoverMapDTO.getInfoCovers().put(infoInList.getId(), infoInList.getInfoIndexDTO());
			}
	}

	@Override
	public void run() {

		switch (crud) {
		case CREATE:
			delete();
			create();
			read();
			this.infoManager.updateUI(this.infoCoverMapDTO);
			break;
		case READ:
			read();
			this.infoManager.updateUI(this.infoCoverMapDTO);
			break;
		case UPDATE:
			update();
			break;
		case DELETE:
			delete();
			create();
			read();
			this.infoManager.updateUI(this.infoCoverMapDTO);
			break;
		}

	}

	private void delete() {
		final var fileDTO = new FileDTO<Integer, MapInfoDTO>(0, this.infoManager.getInfoFrontCoverDirectory(),"json");
		try {
			FileIO.deleteFile(fileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.infoManager.updateUI(this.infoCoverMapDTO);
	}

	private void update() {
		//TODO do this more granular
		delete();
		create();
	}

	private void read() {
		this.infoCoverMapDTO.getInfoCovers().clear();
		final var coverFile = new FileDTO<>(this.infoCover,this.infoManager.getInfoFrontCoverDirectory(),"json");
		if (Files.exists(coverFile.getFilePath())) {
			try {
				this.infoCoverMapDTO = FileIO.readFile(coverFile.getFilePath(),
						InfoCoverMapDTO.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//Read all files from folder
			Map<Integer, FileDTO<Integer, MapInfoDTO>> files;
			try {
				files = FileIO.readAllInfoFiles(coverFile.getDirectory());
				files.forEach((k, v) -> {
					InfoIndexDTO infoListItem = new InfoIndexDTO(v.getId());
					infoListItem.setTitle("INFO");
					this.infoCoverMapDTO.getInfoCovers().put(v.getId(), infoListItem);
				});
				create();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void create() {
		try {
			final var coverFile = new FileDTO<String, InfoCoverMapDTO>(this.infoCover,this.infoManager.getInfoFrontCoverDirectory(),"json");
			coverFile.setContend(this.infoCoverMapDTO);
			FileIO.createFile(coverFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteCover(FileDTO<Integer,? extends ConvertableToJSON> fileDTO) {
		//Remove from List
		this.infoCoverMapDTO.getInfoCovers().remove(fileDTO.getId());
		//UpdateFile		
		this.crud = CRUD.DELETE;
	}

	public void readCover() {
		this.crud = CRUD.READ;
	}

	public void updateCover() {
		this.crud = CRUD.UPDATE;
	}

	public void createCover(FileDTO<Integer, ? extends ConvertableToJSON> fileDTO) {
		InfoIndexDTO infoListItem = new InfoIndexDTO(fileDTO.getId());
		infoListItem.setTitle("INFO");
		this.infoCoverMapDTO.getInfoCovers().put(fileDTO.getId(),infoListItem);
		this.crud = CRUD.CREATE;
	}

}