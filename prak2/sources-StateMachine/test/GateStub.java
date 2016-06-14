package test;

import boundaryclasses.IGate;

public class GateStub implements IGate {
	boolean gateClosed = false;

	@Override
	public void sendCloseGate() {
		System.out.println("Gate is closing...");
		gateClosed = true;

	}

	@Override
	public void sendOpenGate() {
		System.out.println("Gate is opening...");
		gateClosed = false;

	}

	@Override
	public boolean receivedGateClosed() {
		return gateClosed;
	}

	@Override
	public boolean receivedGateOpen() {
		return !gateClosed;
	}

}
