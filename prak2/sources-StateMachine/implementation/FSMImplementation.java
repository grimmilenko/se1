package implementation;

import boundaryclasses.IGate;
import boundaryclasses.IHumidifier;
import boundaryclasses.IHumiditySensor;
import boundaryclasses.IManualControl;
import boundaryclasses.IOpticalSignals;
import boundaryclasses.IPump;
import boundaryclasses.ITimer;
import fsm.IFSM;
import test.TimerStub;

public class FSMImplementation implements IFSM {
	private FSMState state;
	private IPump pumpA;
	private IPump pumpB;
	private IGate gate;
	private IOpticalSignals signals;
	private IHumiditySensor sensor;
	private IHumidifier humidifier;
	private IManualControl operatorPanel;
	private ITimer timer;
	private final double upperBound;
	private final double lowerBound;
	private double humidity = -1;

	public FSMImplementation(IPump pumpA, IPump pumpB, IGate gate, IOpticalSignals signals, IHumidifier humidifier,
			IHumiditySensor sensor, IManualControl operatorPanel) {
		this.state = FSMState.HumidityOkay;
		this.pumpA = pumpA;
		this.pumpB = pumpB;
		this.gate = gate;
		this.signals = signals;
		this.sensor = sensor;
		this.humidifier = humidifier;
		this.operatorPanel = operatorPanel;
		upperBound = 60;
		lowerBound = 20;
		timer = new TimerStub();
	}

	private void updateState() {
		if (humidity < lowerBound) {
			state = FSMState.HumidityLow;
		} else if (humidity >= lowerBound && humidity <= upperBound) {
			state = FSMState.HumidityOkay;
		} else {
			state = FSMState.HumidityHigh;
		}
	}

	@Override
	public void evaluate(double data, long ms) {

		if (humidity == -1) {
			humidity = sensor.setHumidity(data);
		}

		updateState();

		switch (state) {
		case HumidityLow:
			signals.switchLampAOn();
			humidifier.sendSprayOn();
			while (humidity < lowerBound) {
				humidity += 1;
				System.out.println("Increasing humidity...");
			}
			state = FSMState.HumidityOkay;
			if (humidifier.isSprayOn()) {
				humidifier.sendSprayOff();
				signals.switchLampAOff();
			}
			break;
		case HumidityOkay:

			break;
		case HumidityHigh:
			signals.switchLampBOn();
			gate.sendCloseGate();
			while (!gate.receivedGateClosed()) {
				// Warte auf Rückmeldung vom Tor
			}
			signals.switchLampBOff();

			pumpA.sendActivate(ms);
			pumpB.sendActivate(ms);

			timer.startTime(5);

			while (!timer.isTimerExpired()) {
				if (pumpA.receivedActivated() && pumpB.receivedActivated()) {
					System.out.println("Pumps successfully activated!");
					break;
				}
			}
			if (pumpA.receivedActivated() && pumpB.receivedActivated()) {
				while (humidity > upperBound) {
					humidity -= 1;
					System.out.println("Decreasing humditity...");
				}
				pumpA.sendDeactivate();
				pumpB.sendDeactivate();
				signals.switchLampBOn();
				gate.sendOpenGate();
				while (!gate.receivedGateOpen()) {
					// Warte auf Rückmeldung vom Tor
				}
				signals.switchLampBOff();
				state = FSMState.HumidityOkay;
			} else {
				System.out.println("An error occured while activating pumps...");
				state = FSMState.ERROR;
			}
			break;
		case ERROR:
			while (!operatorPanel.receivedAcknowledgement()) {
				// Warte auf manuelle Bestätigung
			}
			state = FSMState.HumidityOkay;
			break;
		}
		humidity = -1;
	}

	public FSMState getState() {
		return state;
	}

}
