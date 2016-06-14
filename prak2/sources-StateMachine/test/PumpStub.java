package test;

import boundaryclasses.IPump;

public class PumpStub implements IPump {
	boolean pumpActivated = false;

	@Override
	public void sendActivate(long ms) {
		System.out.println("Trying to activate the Pump...");
		new Thread() {
			public void run() {
				waitActivation(ms);
			}
		}.start();
		System.out.println("Pump successfully started!");
	}

	@Override
	public void sendDeactivate() {
		System.out.println("Deactivating Pump...");
		pumpActivated = false;
	}

	@Override
	public boolean receivedActivated() {
		return pumpActivated;
	}

	private void waitActivation(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
		pumpActivated = true;
	}
}
