package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public abstract class SystemsManager extends OpMode {
    double[] translateArr, rotateArr, powers;
    DrivetrainCore drivetrainCore;

    @Override
    public void init() {
        drivetrainCore = new DrivetrainCore(hardwareMap);
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */

    private double noStickDrift(double stick, double drift) {
        if (stick < drift && stick > 0-drift) {
            return 0;
        }
        return stick;
    }

    /** Updates the robot's X-drive drivetrain.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing.
     */
    protected void updateDrivetrain(int controllerNum) {
        // Inputs received from controller
        double forward, strafe, rotate;
        switch (controllerNum) {
            case 1:
                forward = noStickDrift(gamepad1.left_stick_y, 0.05);
                strafe = noStickDrift(gamepad1.left_stick_x, 0.05);
                rotate = noStickDrift(gamepad1.right_stick_x, 0.05);
                break;
            case 2:
                forward = noStickDrift(gamepad2.left_stick_y, 0.05);
                strafe = noStickDrift(gamepad2.left_stick_x, 0.05);
                rotate = noStickDrift(gamepad2.right_stick_x, 0.05);
                break;
            default:
                forward = 0;
                strafe = 0;
                rotate = 0;
        }
        // Processing inputs
        translateArr = drivetrainCore.translate(forward, strafe);
        rotateArr = drivetrainCore.rotate(rotate);
        powers = new double[4];
        for (int i=0; i<4; i++) {
            powers[i] = translateArr[i] + rotateArr[i];
        }
        drivetrainCore.setPowers(powers);
    }

    /** Telemetry */
    protected void telemetry(Telemetry telemetry) {
        drivetrainCore.telemetry(telemetry);
    }
}
