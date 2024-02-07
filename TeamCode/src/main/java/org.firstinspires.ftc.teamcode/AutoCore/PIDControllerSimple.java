package org.firstinspires.ftc.teamcode.AutoCore;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** PIDControllerSimple
 * Uses control theory to move an inputted currentPosition to an inputted targetPosition.
 * Similar to PIDController, but instead is NOT based on a DcMotor.
 * This class only handles theoretical movement to a goal.
 * If you are using this class, ensure the relevant part always starts in the same position for consistency.
 * See https://www.ctrlaltftc.com/the-pid-controller for info. */
public class PIDControllerSimple {
    protected final String name;
    // PID vars
    protected final double Kp;
    protected final double Ki;
    protected final double Kd;
    protected final double integralSumMax;
    protected double targetPosition, pTargetPosition;
    protected double currentPosition;
    protected double pError; // error could be here but only is used in update()
    protected double integralSum; // derivative could be here but only is used in update()
    protected final double powerCap;
    private double power;
    // Measures time passed in millis
    ElapsedTime timer;

    /** @param name Name of motor when getting from hardwareMap.
     * @param Kp Proportional coefficient (P in PID). Input >0
     * @param Ki Integral coefficient (I in PID). Input 0-1
     * @param Kd Derivative coefficient (D in PID). Input 0-1
     * @param powerCap Maximum power that motor can run at. Input 0-1 */
    public PIDControllerSimple(String name, double Kp, double Ki, double Kd, double powerCap) {
        this.name = name;
        // Initialize PID variables
        timer = new ElapsedTime();
        targetPosition = 0;
        pTargetPosition = 0;
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        integralSumMax = (Ki == 0) ? 0.25 : 0.25/Ki;
        this.powerCap = powerCap;
    }

    /** Returns the difference between the current targetPosition and previous targetPosition. */
    public double getDeltaTargetPosition() {
        return targetPosition - pTargetPosition;
    }

    public double getPower() {
        return power;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    /** Sets a new target position for the PIDController to move towards. */
    public void setTargetPosition(double targetPosition) {
        pTargetPosition = this.targetPosition;
        this.targetPosition = targetPosition;
        // Reset PID variables
        pError = 0;
        integralSum = 0;
    }

    /** Reassigns targetPosition based on pTargetPosition, treating pTargetPosition as the starting point. */
    public void moveByEncoder(double encoder) {
        pTargetPosition = targetPosition;
        targetPosition += encoder;
        // Reset PID variables
        pError = 0;
        integralSum = 0;
    }

    /** Changes power to move towards targetPosition.
     * If the PIDController has already reached targetPosition, no code is executed. */
    public void update(double currentPosition) {
        this.currentPosition = currentPosition;
        // Exit if already at goalPosition
        if (targetPosition == currentPosition) {
            return;
        }
        // Distance between goal and current
        // currentPosition could be here but only is used in update()
        double error = targetPosition - currentPosition;

        // sum of all errors over time
        // timer.seconds() is time passed since last run
        integralSum += error * timer.seconds();
        // prevent integralSum from increasing by too much
        if (integralSum > integralSumMax) integralSum = integralSumMax;

        // rate of change of error
        // timer.seconds() is time passed since last run
        double derivative = (error - pError) / timer.seconds();

        power = Kp * error +
                Ki * integralSum +
                Kd * derivative;

        // set power based on powerCap
        if (power < -powerCap) power = -powerCap; // more powerful than -powerCap
        if (power > powerCap) power = powerCap; // more powerful than +powerCap

        pError = error;

        // reset timer for next instance
        timer.reset();
    }

    /** Telemetry */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData(name + " targetPosition", getTargetPosition());
        telemetry.addData(name + " currentPosition", getCurrentPosition());
        telemetry.addData(name + " power", getPower());
    }
}