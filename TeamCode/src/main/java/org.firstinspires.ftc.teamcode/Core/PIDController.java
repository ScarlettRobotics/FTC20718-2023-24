package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** PIDController
 * Class used when you want to move a DcMotor to a specific encoder value. Uses control theory to achieve this.
 * If you are using this class, ensure the motor always starts in the same position for consistency.
 * See https://www.ctrlaltftc.com/the-pid-controller for info.
 * Use overridePower() if you want to set raw powers to motors. */
public class PIDController {
    private final DcMotor motor;
    private final String motorName;
    // PID vars
    private final double Kp;
    private final double Ki;
    private final double Kd;
    private final double integralSumMax;
    private int targetPosition, pTargetPosition;
    // currentPosition could be here but only is used in update()
    private int pError; // error could be here but only is used in update()
    private double integralSum; // derivative could be here but only is used in update()
    private final double powerCap;
    // Measures time passed in millis
    ElapsedTime timer;

    /** @param motorName Name of motor when getting from hardwareMap.
     * @param Kp Proportional coefficient (P in PID). Input >0
     * @param Ki Integral coefficient (I in PID). Input 0-1
     * @param Kd Derivative coefficient (D in PID). Input 0-1
     * @param powerCap Maximum power that motor can run at. Input 0-1 */
    PIDController(HardwareMap hardwareMap, String motorName, double Kp, double Ki, double Kd, double powerCap) {
        this.motorName = motorName;
        // Initialize PID variables
        timer = new ElapsedTime();
        targetPosition = 0;
        pTargetPosition = 0;
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        integralSumMax = (Ki == 0) ? 0.25 : 0.25/Ki;
        this.powerCap = powerCap;
        // Initialize motor
        motor = hardwareMap.get(DcMotor.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // encoder doesn't normally reset to zero
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** Sets a new target position for the PIDController to move towards. */
    protected void setTargetPosition(int targetPosition) {
        pTargetPosition = this.targetPosition;
        this.targetPosition = targetPosition;
        // Reset PID variables
        pError = 0;
        integralSum = 0;
    }

    /** Sets a new target position based on the current position, moving by the input. */
    protected void moveByEncoder(int encoder) {
        pTargetPosition = targetPosition;
        targetPosition += encoder;
        // Reset PID variables
        pError = 0;
        integralSum = 0;
    }

    /** Changes the motor direction of the DcMotor. */
    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    /** Overrides the set power from update() to the inputted power.
     * Run this code after update(), or overridePower() will do nothing. */
    protected void overridePower(double power) {
        motor.setPower(power);
    }

    /** Moves the controller towards goalPosition encoder location.
     * If the PIDController has already reached goalPosition, no code is executed. */
    protected void update(int currentPosition) {
        // Exit if already at goalPosition
        if (targetPosition == currentPosition) {
            return;
        }
        // Distance between goal and current
        // currentPosition could be here but only is used in update()
        int error = targetPosition - currentPosition;

        // sum of all errors over time
        // timer.seconds() is time passed since last run
        integralSum += error * timer.seconds();
        // prevent integralSum from increasing by too much
        if (integralSum > integralSumMax) integralSum = integralSumMax;

        // rate of change of error
        // timer.seconds() is time passed since last run
        double derivative = (error - pError) / timer.seconds();

        double power = Kp * error +
                Ki * integralSum +
                Kd * derivative;

        // set power of motor based on powerCap
        if (power < -powerCap) motor.setPower(-powerCap); // more powerful than -powerCap
        else if (power > powerCap) motor.setPower(powerCap); // more powerful than +powerCap
        else motor.setPower(power); // moving normally

        pError = error;

        // reset timer for next instance
        timer.reset();
    }

    /** Returns the difference between the current targetPosition and previous targetPosition. */
    protected int getDeltaTargetPosition() {
        return targetPosition - pTargetPosition;
    }

    /** Returns the current power of the DcMotor. */
    public double getPower() {
        return motor.getPower();
    }

    /** Returns the encoder value of the DcMotor. */
    public int getEncoder() {
        return motor.getCurrentPosition();
    }

    /** Telemetry */
    protected void telemetry(Telemetry telemetry) {
        telemetry.addData(motorName + " targetPosition", targetPosition);
        telemetry.addData(motorName + " currentPosition", motor.getCurrentPosition());
        telemetry.addData(motorName + " power", motor.getPower());
    }
}