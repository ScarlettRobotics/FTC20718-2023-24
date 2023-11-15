package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArmCore {
    private DcMotor armMotor;

    // Constructor: Initializes the ArmCore object with the specified hardware map.
    ArmCore(HardwareMap hardwareMap) {
        // Retrieve the DcMotor named "armMotor" from the hardware map.
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        // Set the run mode of the arm motor to use encoders for position control.
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Get the target position of the arm motor.
    protected int getTargetPosition() {
        return armMotor.getTargetPosition();
    }

    // Set the target position of the arm motor to the specified encoder value.
    protected void goToEncoder(int encoder) {
        armMotor.setTargetPosition(encoder);
    }

    // Set the target position, reset the encoder, and run the motor to the specified position.
    protected void moveByEncoder(int encoder) {
        // Stop and reset the encoder to ensure accurate position control.
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Set the target position for the motor.
        armMotor.setTargetPosition(encoder);
        // Set the run mode to run to the target position.
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // Set the power of the arm motor to move it (e.g., 0.5 indicates 50% power).
    // Note: This method should be called periodically to maintain the motor's position.
    protected void update() {
        armMotor.setPower(0.5);
    }

    // Send telemetry data about the arm motor to the driver station for debugging.
    protected void telemetry(Telemetry telemetry) {
        // Add telemetry data for the current class name.
        telemetry.addData("CURRENT CLASS", "ArmCore.java");
        // Add telemetry data for the run mode of the arm motor.
        telemetry.addData("runMode", armMotor.getMode());
        // Add telemetry data for the target position of the arm motor.
        telemetry.addData("targetPosition", armMotor.getTargetPosition());
        // Add telemetry data for the current position of the arm motor.
        telemetry.addData("currentPosition", armMotor.getCurrentPosition());
        // Add telemetry data for the power being applied to the arm motor.
        telemetry.addData("power", armMotor.getPower());
    }
}
