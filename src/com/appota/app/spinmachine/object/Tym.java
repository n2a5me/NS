package com.appota.app.spinmachine.object;

public class Tym {

	private boolean status;
	private int value;
	private int type;

	public class TypeTym {
		public static final int Purple = 1;
		public static final int Green = 2;
		public static final int Gold = 3;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
