import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

public class Calibration {
	
	public static void main(String[] args) throws InterruptedException {
		ColorSensor sensor = new ColorSensor(SensorPort.S1);
		while (true) {
			System.out.println(sensor.getLightValue());
			System.out.println(sensor.getColorID());
		}
	}
	
}
