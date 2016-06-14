package test;

import boundaryclasses.IHumiditySensor;

public class HumiditySensorStub implements IHumiditySensor {

	@Override
	public double setHumidity(double data) {
		return data;
	}

}
