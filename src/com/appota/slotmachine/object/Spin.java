package com.appota.slotmachine.object;

import java.util.ArrayList;

public class Spin {

	public PurpleTym purpleTym;
	public GreenTym greenTym;
	public String game_token;

	public Spin(PurpleTym purpleTym, GreenTym greenTym,
			ArrayList<Reward> rewards, boolean isFree) {
		super();
		this.purpleTym = purpleTym;
		this.greenTym = greenTym;
		this.rewards = rewards;
		this.isFree = isFree;
	}

	public Spin(PurpleTym purpleTym, GreenTym greenTym, String game_token,
			boolean isFree) {
		super();
		this.purpleTym = purpleTym;
		this.greenTym = greenTym;
		this.game_token = game_token;
		this.isFree = isFree;
	}

	public ArrayList<Reward> rewards;
	public boolean isFree;

}
