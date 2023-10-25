package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/*** Manages the drivetrain of the robot.
 * "Dual" refers to the two wheels in the drivetrain. */
public class DualMotorDrive {
    // Initialize motor variables
    private DcMotor leftMotor, rightMotor;

    // Map motor variables to driver hub
    public DualMotorDrive(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void move(double leftPower, double rightPower) {
        leftMotor.setPower(leftPower);
        rightMotor.setPower(-rightPower);
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("\nCurrent class", "DualMotorDrive.java");
        telemetry.addData("runMode", leftMotor.getMode());
        telemetry.addData("Left Power",
                "%4.2f", leftMotor.getPower());
        telemetry.addData("Right Power",
                "%4.2f", rightMotor.getPower());
    }
}
