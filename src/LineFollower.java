import lejos.nxt.ColorSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class LineFollower {

    private static final int DISTANCE_MIN = 24;
    //pista 2
//	private static final int BLACK_MAX = 45;
//    private static final int WHITE_MAX = 80;
    //pista 1
    private static final int BLACK_MAX = 47;
    private static final int WHITE_MAX = 95;
    private static final boolean SKIP_CALIBRATION = true;
    private static final float SLOPE_ADJUST = 3;
    private static final int MOTOR_MAX_SPEED = 600;
    private static final int MOTOR_MIN_SPEED = -50;
    private static final int MOTOR_MEDIUM_SPEED = (MOTOR_MAX_SPEED + MOTOR_MIN_SPEED) / 2;

    public static void main(String[] args) throws InterruptedException {
    	ColorSensor lightSensor = new ColorSensor(SensorPort.S1);
    	TouchSensor touchSensor = new TouchSensor(SensorPort.S2);
    	UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
    	
    	float blackColor = BLACK_MAX;
    	float whiteColor = WHITE_MAX;
    	
    	if (!SKIP_CALIBRATION) {
	    	blackColor = getColor(lightSensor, touchSensor, "PRETA");
	    	
	    	whiteColor = getColor(lightSensor, touchSensor, "BRANCA");
    	}
    	
        float middle_color = (blackColor + whiteColor) / 2;
        float slope = (2 / ((blackColor - middle_color) * 2)) * SLOPE_ADJUST;

        while (lightSensor.getColorID() != 1 && !touchSensor.isPressed()) {
		    float sensorColor = lightSensor.getLightValue();
		    float diference = sensorColor - middle_color;
		    float turn = slope * diference;
		    float motorASpeed = (1 - (turn)) * MOTOR_MEDIUM_SPEED;
		    float motorBSpeed = (1 - (turn * -1)) * MOTOR_MEDIUM_SPEED;
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
		
		    System.out.println(sensorColor);
		    Motor.A.setSpeed(motorASpeed);
		    Motor.B.setSpeed(motorBSpeed);
		    if (motorASpeed < 0) {
		    	Motor.A.forward();
		    	Motor.A.setSpeed(motorASpeed * -1);
		    } else {
		    	Motor.A.backward();
		    }
		    if (motorBSpeed < 0) {
		    	Motor.B.forward();
		    	Motor.B.setSpeed(motorBSpeed * -1);
		    } else {
		    	Motor.B.backward();
		    }
        }
        int distance = DISTANCE_MIN;
        boolean turned = false;
        long currentTime = System.currentTimeMillis();
        while (lightSensor.getColorID() != 0 && !touchSensor.isPressed()) {
        	if (ultrasonicSensor.getDistance() > distance) {
        		Motor.A.setSpeed(MOTOR_MAX_SPEED * 2);
        		Motor.B.setSpeed(MOTOR_MAX_SPEED * 2);
        		Motor.A.backward();
    		    Motor.B.backward();        		
        	} else {
        		if (System.currentTimeMillis() - currentTime < 700) {
        			Motor.A.stop(true);
	        		Motor.B.stop();
	        		Motor.A.rotate(300, true);
	        		Motor.B.rotate(300);
        		}
        		turned = true;
        		Motor.A.stop(true);
        		Motor.B.stop();
        		Motor.A.rotate(100, true);
        		Motor.B.rotate(-640);
        		currentTime = System.currentTimeMillis();
        		
        	}
        	if (lightSensor.getColorID() == 1 && turned) {
        		Motor.A.stop(true);
        		Motor.B.stop();
        		Motor.A.rotate(720, true);
        		Motor.B.rotate(720);
        		
        		Motor.A.rotate(720, true);
        		Motor.B.rotate(-720);
        	}
        }
    }

	private static float getColor(ColorSensor lightSensor, TouchSensor touchSensor, String colorName) throws InterruptedException {
		System.out.println("Pressione para iniciar a leitura da cor " + colorName);
		while (!touchSensor.isPressed()) {
		}
		
		Thread.sleep(1000);
		
		float color = 0;
		while (!touchSensor.isPressed()) {
			color = lightSensor.getLightValue();
			System.out.println("Cor atual " + color);
		}
		
		Thread.sleep(1000);
		return color;
	}
}
