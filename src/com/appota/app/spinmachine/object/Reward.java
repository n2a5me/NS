package com.appota.app.spinmachine.object;

import java.util.ArrayList;

import com.appota.app.spinmachine.widget.GiftItem;

public class Reward {

	private int id;
	private String description;
	private String image;
	private String gametype;
	private String type;
	private int value;
	private int new_purple_tym;
	private int new_green_tym;
	private int new_yellow_tym;
	private ArrayList<GiftItem> gifts;
	
	public ArrayList<GiftItem> getGifts() {
		return gifts;
	}

	public void setGifts(ArrayList<GiftItem> gifts) {
		this.gifts = gifts;
	}

	public Reward(int id,String description,String image, String gametype,int new_purple_tym,int new_green_tym)
	{
		this.id=id;
		this.description=description;
		this.image=image;
		this.gametype=gametype;
		this.new_green_tym=new_green_tym;
		this.new_purple_tym=new_green_tym;
	}
	
	public Reward(String description, String type, int value,
			int new_purple_tym, int new_green_tym, int new_yellow_tym) {
		super();
		this.description = description;
		this.type = type;
		this.value = value;
		this.new_purple_tym = new_purple_tym;
		this.new_green_tym = new_green_tym;
		this.new_yellow_tym = new_yellow_tym;
	}

	public int getNew_yellow_tym() {
		return new_yellow_tym;
	}

	public void setNew_yellow_tym(int new_yellow_tym) {
		this.new_yellow_tym = new_yellow_tym;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public int getNew_purple_tym() {
		return new_purple_tym;
	}
	public void setNew_purple_tym(int new_purple_tym) {
		this.new_purple_tym = new_purple_tym;
	}
	public int getNew_green_tym() {
		return new_green_tym;
	}
	public void setNew_green_tym(int new_green_tym) {
		this.new_green_tym = new_green_tym;
	}
	
	
	
	
}
