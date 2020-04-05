package coderunner;

public class RunInstructions {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((runInstructions == null) ? 0 : runInstructions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RunInstructions))
			return false;
		RunInstructions other = (RunInstructions) obj;
		if (runInstructions == null) {
			if (other.runInstructions != null)
				return false;
		} else if (!runInstructions.equals(other.runInstructions))
			return false;
		return true;
	}

	private String runInstructions;

	public String getRunInstructions() {
		return runInstructions;
	}

	public void setRunInstructions(String runInstructions) {
		this.runInstructions = runInstructions;
	}
}