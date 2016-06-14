package test;

import boundaryclasses.IOpticalSignals;

public class OpticalSignalsStub implements IOpticalSignals {

	@Override
	public void switchLampAOn() {
		System.out.println("Light A on...");
	}

	@Override
	public void switchLampAOff() {
		System.out.println("Light A off...");
	}

	@Override
	public void switchLampBOn() {
		System.out.println("Light B on...");
	}

	@Override
	public void switchLampBOff() {
		System.out.println("Light B off...");
	}

}
