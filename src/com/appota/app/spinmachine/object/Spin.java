package com.appota.app.spinmachine.object;


public class Spin {

	private boolean free_spin_status,silver_spin_status,gold_spin_status;
	private int free_spin_time_available;
	private int silver_spin_purple_tym,silver_spin_bet_purple_tym,gold_spin_green_tym,gold_spin_bet_green_tym,gold_spin_gold_ticket,yellow_tym;
	private Ads ads;
	private String game_token;
	
	public String getGame_token() {
		return game_token;
	}
	public void setGame_token(String game_token) {
		this.game_token = game_token;
	}
	public boolean isFree_spin_status() {
		return free_spin_status;
	}
	public void setFree_spin_status(boolean free_spin_status) {
		this.free_spin_status = free_spin_status;
	}
	public boolean isSilver_spin_status() {
		return silver_spin_status;
	}
	public void setSilver_spin_status(boolean silver_spin_status) {
		this.silver_spin_status = silver_spin_status;
	}
	public boolean isGold_spin_status() {
		return gold_spin_status;
	}
	public void setGold_spin_status(boolean gold_spin_status) {
		this.gold_spin_status = gold_spin_status;
	}
	public int getFree_spin_time_available() {
		return free_spin_time_available;
	}
	public void setFree_spin_time_available(int free_spin_time_available) {
		this.free_spin_time_available = free_spin_time_available;
	}
	public int getSilver_spin_purple_tym() {
		return silver_spin_purple_tym;
	}
	public void setSilver_spin_purple_tym(int silver_spin_purple_tym) {
		this.silver_spin_purple_tym = silver_spin_purple_tym;
	}
	public int getSilver_spin_bet_purple_tym() {
		return silver_spin_bet_purple_tym;
	}
	public void setSilver_spin_bet_purple_tym(int silver_spin_bet_purple_tym) {
		this.silver_spin_bet_purple_tym = silver_spin_bet_purple_tym;
	}
	public int getGold_spin_green_tym() {
		return gold_spin_green_tym;
	}
	public void setGold_spin_green_tym(int gold_spin_green_tym) {
		this.gold_spin_green_tym = gold_spin_green_tym;
	}
	public int getGold_spin_bet_green_tym() {
		return gold_spin_bet_green_tym;
	}
	public void setGold_spin_bet_green_tym(int gold_spin_bet_green_tym) {
		this.gold_spin_bet_green_tym = gold_spin_bet_green_tym;
	}
	public int getGold_spin_gold_ticket() {
		return gold_spin_gold_ticket;
	}
	public void setGold_spin_gold_ticket(int gold_spin_gold_ticket) {
		this.gold_spin_gold_ticket = gold_spin_gold_ticket;
	}
	public int getYellow_tym() {
		return yellow_tym;
	}
	public void setYellow_tym(int yellow_tym) {
		this.yellow_tym = yellow_tym;
	}
	public Ads getAds() {
		return ads;
	}
	public void setAds(Ads ads) {
		this.ads = ads;
	}
	

}
