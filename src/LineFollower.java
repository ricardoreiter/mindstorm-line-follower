import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class LineFollower {

    private static final int BLACK_MAX = 50;
    private static final int WHITE_MAX = 110;
    private static final float SLOPE = -0.2f;
    private static final int MOTOR_MAX_SPEED = 500;
    private static final int MOTOR_MIN_SPEED = 5;
    private static final int MOTOR_MEDIUM_SPEED = (MOTOR_MAX_SPEED + MOTOR_MIN_SPEED) / 2;

    public static void main(String[] args) throws InterruptedException {
        float middle_color = (BLACK_MAX + WHITE_MAX) / 2;

        ColorSensor lightSensor = new ColorSensor(SensorPort.S1);
        TouchSensor touchSensor = new TouchSensor(SensorPort.S2);
        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
        while (lightSensor.getColorID() != 1 && !touchSensor.isPressed()) {
		    float sensorColor = lightSensor.getLightValue();
		    float diference = sensorColor - middle_color;
		    float turn = SLOPE * diference;
		    float motorASpeed = (1 - (turn * -1)) * MOTOR_MEDIUM_SPEED;
		    float motorBSpeed = (1 - turn) * MOTOR_MEDIUM_SPEED;
		    if (motorASpeed < MOTOR_MIN_SPEED) {
		    	motorASpeed = MOTOR_MIN_SPEED;
		    }
		    if (motorBSpeed < MOTOR_MIN_SPEED) {
		    	motorBSpeed = MOTOR_MIN_SPEED;
		    }
		    
		    if (motorASpeed > MOTOR_MAX_SPEED) {
		    	motorASpeed = MOTOR_MAX_SPEED;
		    }
		    if (motorBSpeed > MOTOR_MAX_SPEED) {
		    	motorBSpeed = MOTOR_MAX_SPEED;
		    }
		
		    System.out.println(sensorColor + " - " + motorASpeed + " - " + motorBSpeed);
		    Motor.A.setSpeed(motorASpeed);
		    Motor.B.setSpeed(motorBSpeed);
		    Motor.A.backward();
		    Motor.B.backward();
        }
        int distance = 20;
        int walls = 0;
        while (lightSensor.getColorID() != 0 && !touchSensor.isPressed()) {
        	if (ultrasonicSensor.getDistance() > distance) {
        		Motor.A.setSpeed(MOTOR_MAX_SPEED * 2);
        		Motor.B.setSpeed(MOTOR_MAX_SPEED * 2);
        		Motor.A.backward();
    		    Motor.B.backward();        		
        	} else {
        		walls++;
        		if (walls >= 3) {
        			distance += 10;
        			walls = 0;
        		}
        		Motor.A.stop(true);
        		Motor.B.stop();
        		Motor.A.rotate(355, true);
        		Motor.B.rotate(-355);
        	}
        }
    }
}
