package info;

public class InfoLayoutDTO  {

	private Integer infoId;
	private Integer infoFileId;
	private Double height;
	private Double width;
	
	
	
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double d) {
		this.height = d;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	
	public Integer getInfoId() {
		return infoId;
	}
	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}
	public Integer getInfoFileId() {
		return infoFileId;
	}
	public void setInfoFileId(Integer infoFileId) {
		this.infoFileId = infoFileId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((infoFileId == null) ? 0 : infoFileId.hashCode());
		result = prime * result + ((infoId == null) ? 0 : infoId.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InfoLayoutDTO))
			return false;
		InfoLayoutDTO other = (InfoLayoutDTO) obj;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (infoFileId == null) {
			if (other.infoFileId != null)
				return false;
		} else if (!infoFileId.equals(other.infoFileId))
			return false;
		if (infoId == null) {
			if (other.infoId != null)
				return false;
		} else if (!infoId.equals(other.infoId))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}
	
	
}