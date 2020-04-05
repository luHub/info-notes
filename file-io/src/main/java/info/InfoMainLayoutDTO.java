package info;

import file.ConvertableToJSON;

public class InfoMainLayoutDTO implements ConvertableToJSON
{
	private float splitPanePos;

	public float getSplitPanePos() {
		return splitPanePos;
	}

	public void setSplitPanePos(float splitPanePos) {
		this.splitPanePos = splitPanePos;
	}
}