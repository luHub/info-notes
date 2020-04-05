package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import file.ConvertableToJSON;
import file.dto.FileDTO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.ops.InfoCoverFileOps;
import model.ops.InfoFileOps;
import model.ops.InfoLayoutFileOps;
import model.ops.InfoMainLayoutOps;
import model.ops.InfoQueuebale;
import info.InfoLayoutDTO;
import info.InfoLayoutListDTO;
import info.InfoMainLayoutDTO;
import info.MapInfoDTO;



//TODO Refactor to generic 
//TODO Change Lists to Sets
//TODO Reduce Code as much as possible to make it generic as possible
public class InfoService<V> extends Service<V> {

	private final ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> infoMap = new ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>>();
	
	private final FileDTO<Integer, InfoLayoutListDTO> layoutFile =  new FileDTO<Integer, InfoLayoutListDTO>();
	
	private final FileDTO<Integer, InfoMainLayoutDTO> mainLayoutFile = new FileDTO<Integer,InfoMainLayoutDTO>(); 
	
	protected final Queue<InfoQueuebale> queue = new ConcurrentLinkedQueue();
	
	//REmove this list is no longer needed
	@Deprecated
	private final List<InfoInList> infoForList = new ArrayList();
	private InfoManager infoManager;

	@Override
	protected Task<V> createTask() {
		// TODO Auto-generated method stub
		return new Task<V>() {

			@Override
			protected V call() throws Exception {
				while (true) {
					try { 
						InfoQueuebale questionFileOps = null;
						synchronized (queue) {
							while (queue.isEmpty()) {
								queue.wait();
							}
							// Get the next work item off of the queue
							questionFileOps = queue.remove();
							// Process the work item
							// Prepare with common data to work
							questionFileOps.run();
						}
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						break; // Terminate
					}
				}
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Done!");
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelled!");
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Failed!");
			}
		};
	}

	public void setInfoManager(InfoManager infoManager) {
		this.infoManager = infoManager;
	}

	public List<InfoInList> getInfoForList() {
		return this.infoForList;
	}

	//TODO REFACTOR FOR UPDATE, DELETE AND CREATE
	//TODO REMOVE infoForList
	public void addInfoFileToSave(FileDTO<Integer,? extends ConvertableToJSON> fileDTO, boolean isUpdate) {
		
		//TODO List Should Only Update on FileDelete or Create and not
		//For InfoUpdate
		synchronized (queue) {
			// Add work to the queue
			InfoFileOps infoFileOps = new InfoFileOps();
			//infoFileOps.setInfoFromList(this.getInfoForList());
			infoFileOps.setInfoMap(this.infoMap);
			infoFileOps.setInfoManager(this.infoManager);
			//infoFileOps.setInfoFromList(infoForList);
			infoFileOps.updateInfoToFile(fileDTO);
			queue.add(infoFileOps);
			// Notify the monitor object that all the threads
			// are waiting on. This will awaken just one to
			// begin processing work from the queue
			queue.notify();
		} 
		//TODO Refactor dirty fix
		if(!isUpdate){
		synchronized (queue) {
			InfoCoverFileOps infoCoverFileOps = new InfoCoverFileOps(infoManager, infoForList, infoMap);
			infoCoverFileOps.createCover(fileDTO);
			queue.add(infoCoverFileOps);
			queue.notify();
		}
		}
	}
	
	public void addInfoLayoutToSave(InfoLayoutDTO infoLayotDTO) {
		synchronized (queue) {
			//TODO FIX ALL THIS
			InfoLayoutFileOps infoLayoutFileOps = prepareLayoutFileOps();
			
			final List<InfoLayoutDTO> layoutDTO = this.layoutFile.getContend().getInfoLayoutList().stream()
					.filter(x -> (x.getInfoFileId().equals(infoLayotDTO.getInfoFileId())
				&& x.getInfoId().equals(infoLayotDTO.getInfoId()))).collect(Collectors.toList());
			
			this.layoutFile.getContend().getInfoLayoutList().removeAll(layoutDTO);
			this.layoutFile.getContend().getInfoLayoutList().add(infoLayotDTO);
			infoLayoutFileOps.updateInfoToFile(this.layoutFile);
			queue.add(infoLayoutFileOps);
			queue.notify();
		}
	}

	// TODO Get Info From Files
	public void getInfoFromFiles() {
		synchronized (queue) {
			InfoFileOps infoFileOps = prepateFileOps();
			queue.add(infoFileOps);
			// Notify the monitor object that all the threads
			// are waiting on. This will awaken just one to
			// begin processing work from the queue
			queue.notify();
		}
	}
	
	public void getLayoutFromFile(){
		synchronized (queue) {
			InfoLayoutFileOps infoFileOps = prepareLayoutFileOps();
			queue.add(infoFileOps);
			queue.notify();
		}
	}

	public boolean isQueueEmpty() {
		// TODO Auto-generated method stub
		return queue.isEmpty();
	}

	// TODO: Delete infoFile
	public void deleteFile(FileDTO<Integer,? extends ConvertableToJSON> fileDTO, boolean isUpdate) {
		synchronized (queue) {
			//1. Prepare delete Operation
			InfoFileOps infoFileOps = prepateFileOps();
			infoFileOps.setFileToDelete(fileDTO);
			//2. Add to service queue 
			queue.add(infoFileOps);
			//3. Notify  
			queue.notify();
		}
		//TODO refactor this ugly fix
		if(!isUpdate)
		synchronized (queue) {
			InfoCoverFileOps infoCoverFileOps = new InfoCoverFileOps(infoManager, infoForList, infoMap);
			infoCoverFileOps.deleteCover(fileDTO);
			queue.add(infoCoverFileOps);
			queue.notify();
		}
	}

	private InfoFileOps prepateFileOps() {
		InfoFileOps infoFileOps = new InfoFileOps();
		//infoFileOps.setInfoFromList(this.infoForList);
		infoFileOps.setInfoMap(this.infoMap);
		infoFileOps.setInfoManager(this.infoManager);
		return infoFileOps;
	} 
	
	private InfoLayoutFileOps prepareLayoutFileOps(){
		InfoLayoutFileOps infoLayoutFileOps = new InfoLayoutFileOps();
		infoLayoutFileOps.setInfoManager(this.infoManager);
		infoLayoutFileOps.setLayoutFile(this.layoutFile);
		return infoLayoutFileOps; 
			
	}
	
	//TODO CONTINUE WORKING HERE!!!
	private InfoMainLayoutOps prepareMainLayoutFileOps(){
		return null;
	}

	public ConcurrentHashMap<Integer, FileDTO<Integer, MapInfoDTO>> getInfoMap() {
		return infoMap;
	}

	public FileDTO<Integer, InfoLayoutListDTO> getLayoutInfo() {
		return this.layoutFile;
	}

	public void updateCover() { 
		synchronized (queue) { 
			InfoCoverFileOps infoCoverFileOps = new InfoCoverFileOps(infoManager, infoForList, infoMap);
			infoCoverFileOps.updateCover();
			//infoFileOps.set
			queue.add(infoCoverFileOps);
			//3. Notify
			queue.notify();
		}
	}

	public void getInfoCovers() {
		synchronized (queue) { 
			InfoCoverFileOps infoCoverFileOps = new InfoCoverFileOps(infoManager, infoForList, infoMap);
			queue.add(infoCoverFileOps);
			queue.notify();
		}
	}

	public FileDTO<Integer, InfoMainLayoutDTO> getMainLayoutInfo() {
		return this.mainLayoutFile;
	}
}