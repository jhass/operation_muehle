package de.hshannover.operation_muehle.ai;

public enum Status {

	MYSTONE, FOREIGNSTONE, EMPTY, INVALID;

	public boolean isStone() {
		boolean result = false;

		switch (this) {
		case MYSTONE:
		case FOREIGNSTONE:
			result = true;
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		String result = " ";
		switch (this) {
		case MYSTONE:
			result = "W";
			break;
		case FOREIGNSTONE:
			result = "B";
			break;
		case INVALID:
			result = "I";
			break;
		}
		return result;
	}
}
