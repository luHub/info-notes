package info;

public class InfoIndexDTO {

	private Integer infoFileId;
	private String position;
	private String title;

	public InfoIndexDTO(){}

	public String getTitle() {
		return title;
	}
	
	public InfoIndexDTO(Integer fileId){
		this.infoFileId=fileId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getInfoFileId() {
		return infoFileId;
	}

	public void setInfoFileId(Integer infoFileId) {
		this.infoFileId = infoFileId;
	}
}