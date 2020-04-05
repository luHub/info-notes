package coderunner;

public class CompileInstructions {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instructions == null) ? 0 : instructions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CompileInstructions))
			return false;
		CompileInstructions other = (CompileInstructions) obj;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		return true;
	}

	private String instructions;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}