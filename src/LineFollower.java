public class LineFollower {

    private static final int BLACK_MAX = 40;
    private static final int WHITE_MAX = 50;
    private static final float SLOPE = -0.2f;
    private static final int MOTOR_MAX_SPEED = 500;
    private static final int MOTOR_MIN_SPEED = 0;
    private static final int MOTOR_MEDIUM_SPEED = (MOTOR_MAX_SPEED + MOTOR_MIN_SPEED) / 2;

    public static void main(String[] args) {
        float middle_color = (BLACK_MAX + WHITE_MAX) / 2;

        float sensorColor = 45;
        float diference = sensorColor - middle_color;
        float turn = SLOPE * diference;
        float motorASpeed = (1 - turn) * MOTOR_MEDIUM_SPEED;
        float motorBSpeed = (1 - (turn * -1)) * MOTOR_MEDIUM_SPEED;

        System.out.println(String.format("[Motor A: %s] [Motor B: %s] [Turn: %s]", motorASpeed, motorBSpeed, turn));

    }
}
