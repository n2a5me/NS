package com.appota.app.spinmachine.object;

public class GreenTym extends Tym {

	private int bet;
	public GreenTym(int value , boolean status,int bet)
	{
		this.bet=bet;
		setStatus(status);
		setValue(value);
		setType(Tym.TypeTym.Green);
	}
}
