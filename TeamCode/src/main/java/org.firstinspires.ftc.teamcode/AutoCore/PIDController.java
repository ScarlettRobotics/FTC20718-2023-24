package org.firstinspires.ftc.teamcode.AutoCore;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** PIDController
 * Class used when you want to move a DcMotor to a specific encoder value. Uses control theory to achieve this.
 * If you are using this class, ensure the motor always starts in the same position for consistency.
 * See https://www.ctrlaltftc.com/the-pid-controller for info.
 * Use overridePower() if you want to set raw powers to motors. */
public class PIDController extends PIDControllerSimple {
    private final DcMotor motor;

    /** @param motorName Name of motor when getting from hardwareMap.
     * @param Kp Proportional coefficient (P in PID). Input >0
     * @param Ki Integral coefficient (I in PID). Input 0-1
     * @param Kd Derivative coefficient (D in PID). Input 0-1
     * @param powerCap Maximum power that motor can run at. Input 0-1 */
    public PIDController(HardwareMap hardwareMap, String motorName, double Kp, double Ki, double Kd, double powerCap) {
        super(motorName, Kp, Ki, Kd, powerCap);
        // Initialize motor
        motor = hardwareMap.get(DcMotor.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // encoder doesn't normally reset to zero
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /** Returns the encoder value of the DcMotor. */
    public int getEncoderPosition() {
        return motor.getCurrentPosition();
    }

    /** Changes the motor direction of the DcMotor. */
    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    /** Overrides the set power from update() to the inputted power.
     * Run this code after update(), or overridePower() will do nothing. */
    public void overridePower(double power) {
        motor.setPower(power);
    }

    /** Moves the controller towards goalPosition encoder location.
     * If the PIDController has already reached goalPosition, no code is executed. */
    public void update(int currentPosition) {
        super.update(currentPosition); // PID calc
        motor.setPower(getPower());
    }

    /** Moves the controller towards goalPosition encoder location.
     * If the PIDController has already reached goalPosition, no code is executed. */
    public void update() {
        super.update(motor.getCurrentPosition()); // PID calc
        motor.setPower(getPower());
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData(name + " targetPosition", getTargetPosition());
        telemetry.addData(name + " currentPosition", motor.getCurrentPosition());
        telemetry.addData(name + " power", motor.getPower());
    }
}