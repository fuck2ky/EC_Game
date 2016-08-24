package io.element.room;

public class RoomConfig {

	private int chooseGapTime;
	
	private int responseGapTime;
	
	public void setChooseGapTime(int chooseGapTime) {
		this.chooseGapTime = chooseGapTime;
	}
	
	public int getChooseGapTime() {
		return chooseGapTime;
	}
	
	public void setResponseGapTime(int perfectGapTime) {
		this.responseGapTime = perfectGapTime;
	}
	
	public int getResponseGapTime() {
		return responseGapTime;
	}
	
}
