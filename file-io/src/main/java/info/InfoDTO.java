package info;

import file.ConvertableToJSON;

//Create a Content Map
//Text 
//Web
//Id
public class InfoDTO implements ConvertableToJSON {
	
	private String text;
	private String url;
	private String imagePath;
	private INFO_TYPE info_type;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public INFO_TYPE getType() {
		return info_type;
	}
	public void setType(INFO_TYPE infoType) {
		this.info_type = infoType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}