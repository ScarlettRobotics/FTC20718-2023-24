package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The ArmCore class encapsulates the functionality to control a DC motor representing an arm.
 */
public class ArmCore {
    private DcMotor armMotor;

    /**
     * Constructor: Initializes the ArmCore object with the specified hardware map.
     * @param hardwareMap The hardware map containing the motor configuration.
     */
    public ArmCore(HardwareMap hardwareMap) {
        // Retrieve the DcMotor named "armMotor" from the hardware map.
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");

        // Set the run mode of the arm motor to use encoders for position control.
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Gets the target position of the arm motor.
     * @return The target position of the arm motor.
     */
    public int getTargetPosition() {
        return armMotor.getTargetPosition();
    }

    /**
     * Sets the target position of the arm motor to the specified encoder value.
     * @param encoder The target encoder position for the arm motor.
     */
    public void goToEncoder(int encoder) {
        armMotor.setTargetPosition(encoder);
    }

    /**
     * Moves the arm motor to the specified encoder position.
     * @param encoder The target encoder position for the arm motor.
     */
    public void moveByEncoder(int encoder) {
        // Stop and reset the encoder to ensure accurate position control.
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set the target position for the motor.
        armMotor.setTargetPosition(encoder);

        // Set the run mode to run to the target position.
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Sets the power of the arm motor to move it.
     * @param power The power level (from -1.0 to 1.0) to apply to the arm motor.
     */
    public void setPower(double power) {
        armMotor.setPower(power);
    }

    /**
     * Sends telemetry data about the arm motor to the driver station for debugging.
     * @param telemetry The telemetry object used to send data to the driver station.
     */
    public void updateTelemetry(Telemetry telemetry) {
        // Add telemetry data for the current class name.
        telemetry.addData("CURRENT CLASS", "ArmCore.java");
        // Add telemetry data for the run mode of the arm motor.
        telemetry.addData("Run Mode", armMotor.getMode());
        // Add telemetry data for the target position of the arm motor.
        telemetry.addData("Target Position", armMotor.getTargetPosition());
        // Add telemetry data for the current position of the arm motor.
        telemetry.addData("Current Position", armMotor.getCurrentPosition());
        // Add telemetry data for the power being applied to the arm motor.
        telemetry.addData("Power", armMotor.getPower());
    }
}
