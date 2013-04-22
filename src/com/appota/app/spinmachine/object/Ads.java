package com.appota.app.spinmachine.object;

public class Ads {
	private String description;
	private String image;
	private String type;
	private String store;
	private String uri;

	public Ads(String description, String image, String type, String store,
			String uri) {
		this.description = description;
		this.image = image;
		this.type = type;
		this.store = store;
		this.uri = uri;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	

}
