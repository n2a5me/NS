package com.appota.app.spinmachine.object;

public class PurpleTym extends Tym {
	private int bet;
	public PurpleTym(int value , boolean status,int bet)
	{
		this.bet=bet;
		setStatus(status);
		setValue(value);
		setType(Tym.TypeTym.Purple);
	}
	
}
