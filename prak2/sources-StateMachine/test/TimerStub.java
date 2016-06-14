package test;

import boundaryclasses.ITimer;

public class TimerStub implements ITimer {
	private long timeStart;
	private double s;

	@Override
	public void startTime(double seconds) {
		timeStart = System.currentTimeMillis();
		s = seconds * 1000;
	}

	@Override
	public boolean isTimerExpired() {
		return (System.currentTimeMillis() - timeStart >= s) ? true : false;
	}

}
