package model.ops;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
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
	private ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap;
	
	private String infoCover = "infoCover";

	public InfoCoverFileOps(InfoManager infoManager, List<InfoInList> infoForList,
			ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap) {
		this.infoManager = infoManager;
		this.infoMap = infoMap;

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
		final FileDTO<Integer, MapInfoDTO> fileDTO = new FileDTO(0, this.infoManager.getInfoFrontCoverDirectory(),"json");
		try {
			FileIO.deleteFile(fileDTO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.infoManager.updateUI(this.infoCoverMapDTO);
	}

	private void update() {
		delete();
		create();
	}

	private void read() {
		this.infoCoverMapDTO.getInfoCovers().clear();
		// TODO Refactor this part because it is horrible:
		// Reads info index (this is the info that will be displayed in the list such like title and order
		if (Files.exists(this.infoManager.getInfoFrontCoverDirectory())) {
			try {
				final FileDTO<String, InfoCoverMapDTO> fileDTO = new FileDTO<>(this.infoCover,this.infoManager.getInfoFrontCoverDirectory(),"json");
				this.infoCoverMapDTO = FileIO.readFile(fileDTO.getFilePath(),
						InfoCoverMapDTO.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// TODO In case the file does not exist create the list from
			// infoMap.
			
			//Read all files from folder
			Map<Integer, FileDTO<Integer, MapInfoDTO>> files;
			try {
				files = FileIO.readAllInfoFiles(infoManager.getInfoDirectory());
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
			final FileDTO<String, InfoCoverMapDTO> fileDTO = new FileDTO<>(this.infoCover,this.infoManager.getInfoFrontCoverDirectory(),"json");
			fileDTO.setContend(this.infoCoverMapDTO);
			FileIO.createFile(fileDTO);
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