package com.deepshiftlabs.nerrvana;

public enum UserMessageLevel {
	TRACE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAL(6);
	private final int value;

	UserMessageLevel(int x) {
		this.value = x;
	}

	public int value() {
		return value;
	}

	public static UserMessageLevel parseThreshold(String s) {
		UserMessageLevel result = null;
		if (s != null) {
			if (s.equalsIgnoreCase("TRACE"))
				result = UserMessageLevel.TRACE;
			else if (s.equalsIgnoreCase("DEBUG"))
				result = UserMessageLevel.DEBUG;
			else if (s.equalsIgnoreCase("INFO"))
				result = UserMessageLevel.INFO;
			else if (s.equalsIgnoreCase("WARN"))
				result = UserMessageLevel.WARN;
			else if (s.equalsIgnoreCase("ERROR"))
				result = UserMessageLevel.ERROR;
			else if (s.equalsIgnoreCase("FATAL"))
				result = UserMessageLevel.FATAL;
		}
		if (result == null)
			result = UserMessageLevel.ERROR;
		return result;
	}
}
