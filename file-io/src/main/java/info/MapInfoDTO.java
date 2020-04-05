package info;

import java.util.HashMap;
import java.util.Map;

import file.ConvertableToJSON;

public class MapInfoDTO implements ConvertableToJSON {

	private String title;
	private Map<Integer,InfoDTO> infoM = new HashMap<>();

	public Map<Integer,InfoDTO> getMap() {
		return infoM;
	}

	public void setMap(Map<Integer,InfoDTO> map) {
		this.infoM = map;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}