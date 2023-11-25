package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Intended to be inherited from driver portion OpModes.
 * Takes "---Core" classes, mapping them to controller inputs depending on controllerNum.
 * All contained methods will receive "controllerNum" stating which gamepad/controller controls each system part.
 * For example, "updateMotorTank(1)" will have controller 1 control the robot.
 * If you'd like to use FTC Dashboard, please refer to https://acmerobotics.github.io/ftc-dashboard/,
 *  then "Getting Started." */
public abstract class SystemsManager extends OpMode {
    // Core classes
    DrivetrainCore drivetrainCore;
    ArmCore armCore;
    ClawCore clawCore;
    DroneLauncherCore droneLauncherCore;
    // Variables used in methods
    double[] translateArr, rotateArr, powers;

    @Override
    public void init() {
        // Initialize classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        droneLauncherCore = new DroneLauncherCore(hardwareMap);
        // Make preloading work by closing claw
        clawCore.close();
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "22531");
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */
    private double noDrift(double stick, double drift) {
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

    /** Updates arm movement.
     * Right and left trigger moves the arm.
     * Uses moveByEncoder(). Only use if ArmCore's RUN_TO_POSITION works.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing. */
    protected void updateArm(int controllerNum) {
        double power;
        switch (controllerNum) {
            case 1:
                power = gamepad1.right_trigger - gamepad1.left_trigger;
                break;
            case 2:
                power = gamepad2.right_trigger - gamepad2.left_trigger;
                break;
            default:
                power = 0;
        }
        armCore.moveByEncoder((int)power*1000);
    }

    /** Updates arm movement.
     * Right and left trigger moves the arm.
     * Uses setPower(). Only use if ArmCore's RUN_TO_POSITION doesn't work.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing. */
    protected void updateArmBlind(int controllerNum){
        double power;
        switch(controllerNum) {
            case 1:
                power = gamepad1.right_trigger - gamepad1.left_trigger;
                break;
            case 2:
                power = gamepad2.right_trigger - gamepad2.left_trigger;
                break;
            default:
                power = 0;
        }
        armCore.setPower(power);
    }

    /** Updates the claw's movement.
     * A/B opens/closes the claw respectively.
     * Opening will be prioritized over closing the claw if both buttons are pressed.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing. */
    protected void updateClaw(int controllerNum) {
        boolean open, close;
        switch (controllerNum) {
            case 1:
                open = gamepad1.a;
                close = gamepad1.b;
                break;
            case 2:
                open = gamepad2.a;
                close = gamepad2.b;
                break;
            default:
                open = false;
                close = false;
        }
        if (open) clawCore.open();
        if (close) clawCore.close();
    }

    /** Checks for a button press to launch the drone.
     * If drone does not launch, rotate the part attached to the servo by 180 degrees.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing. */
    protected void checkForDroneLaunch(int controllerNum) {
        boolean launching = false;
        switch (controllerNum) {
            case 1:
                launching = gamepad1.dpad_up;
                break;
            case 2:
                launching = gamepad2.dpad_up;
                break;
        }
        if (launching) droneLauncherCore.launch();
    }

    /** Telemetry */
    protected void telemetry(Telemetry telemetry) {
        // Telemetry sent to Driver Hub
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        droneLauncherCore.telemetry(telemetry);
    }
}
