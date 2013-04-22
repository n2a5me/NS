package com.appota.app.spinmachine.widget;

public class GiftItem {
private String giftName;
private int src;
private String type;
private String description,value;
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public GiftItem(){}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getGiftName() {
	return giftName;
}
public void setGiftName(String giftName) {
	this.giftName = giftName;
}
public int getSrc() {
	return src;
}
public void setSrc(int src) {
	this.src = src;
}
public GiftItem(String giftName, int src) {
	super();
	this.giftName = giftName;
	this.src = src;
}

}
