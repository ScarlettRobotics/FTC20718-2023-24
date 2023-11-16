package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class SystemsManager extends OpMode {
    double[] translateArr, rotateArr, powers;
    DrivetrainCore drivetrainCore;
    ArmCore armCore;

    @Override
    public void init() {
        drivetrainCore = new DrivetrainCore(hardwareMap);
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */
    private double noDrift(double stick, double drift) {
        if (stick < drift && stick > 0-drift) {
            return 0;
        }
        return stick;
    }

    protected void updateArm(int controllerNum) {
        int raise;
        switch (controllerNum){
            case 1:
                raise = (int)(gamepad1.right_trigger - gamepad1.left_trigger);
                break;
            case 2:
                raise = (int)(gamepad2.right_trigger - gamepad2.left_trigger);
                break;
            default:
                raise = 0;
        }
        armCore.moveByEncoder(raise*1000);
        armCore.update();
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
        armCore.telemetry(telemetry);
    }
}
