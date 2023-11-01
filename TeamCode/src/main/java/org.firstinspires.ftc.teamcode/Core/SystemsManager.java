package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class SystemsManager extends OpMode {
    DrivetrainCore drivetrainCore;

    @Override
    public void init() {
        drivetrainCore = new DrivetrainCore(hardwareMap);
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */
    private double noDrift(double stick, double drift) {
        if (stick < drift) {
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
                forward = noDrift(gamepad1.left_stick_y, 0.05);
                strafe = noDrift(gamepad1.left_stick_x, 0.05);
                rotate = noDrift(gamepad1.right_stick_x, 0.05);
                break;
            case 2:
                forward = noDrift(gamepad2.left_stick_y, 0.05);
                strafe = noDrift(gamepad2.left_stick_x, 0.05);
                rotate = noDrift(gamepad2.right_stick_x, 0.05);
                break;
            default:
                forward = 0;
                strafe = 0;
                rotate = 0;
        }
        // Processing inputs
        double[] translateArr, rotateArr, powers;
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
