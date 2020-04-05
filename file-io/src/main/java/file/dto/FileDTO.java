package file.dto;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDTO<T, U> {

	private T id;
	private U contend;
	private Path directory;
	private String ext;

	
	public FileDTO(){}
	
	//Includes Extension
	public FileDTO(T id,Path directory, String ext){
		this.directory = directory;
		this.id=id;
		this.ext = ext;
	}
	
	
	//public FileDTO(T id,Path path, String ext){
	//	this.setPath(Paths.get(path.toString(),id.toString()+"."+ext.toString()));
	//	this.id=id;
	//}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public U getContend() {
		return contend;
	}

	public void setContend(U contend) {
		this.contend = contend;
	}

	public Path getDirectory() {
		return directory;
	}
	
	public Path getPath(){
		return Paths.get(this.directory.toString(), this.id+this.ext);
	}

	public void setPath(Path directory) {
		this.directory = directory;
	}

	
}