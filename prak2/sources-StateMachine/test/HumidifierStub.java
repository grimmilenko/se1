package test;

import boundaryclasses.IHumidifier;

public class HumidifierStub implements IHumidifier {
	boolean sprayOn = false;

	@Override
	public void sendSprayOn() {
		sprayOn = true;
		System.out.println("Spray on");

	}

	@Override
	public void sendSprayOff() {
		sprayOn = false;
		System.out.println("Spray off");

	}

	@Override
	public boolean isSprayOn() {
		return sprayOn;
	}

}