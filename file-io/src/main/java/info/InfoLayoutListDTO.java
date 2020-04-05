package info;

import java.util.ArrayList;
import java.util.List;

import file.ConvertableToJSON;

public class InfoLayoutListDTO implements ConvertableToJSON {

	private List<InfoLayoutDTO> infoLayoutList = new ArrayList<>();

	public List<InfoLayoutDTO> getInfoLayoutList() {
		return infoLayoutList;
	}

	public void setInfoLayoutList(List<InfoLayoutDTO> infoLayoutList) {
		this.infoLayoutList = infoLayoutList;
	}
	
}
