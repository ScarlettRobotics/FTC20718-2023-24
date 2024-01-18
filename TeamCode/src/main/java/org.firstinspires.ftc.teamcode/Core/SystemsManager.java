package org.firstinspires.ftc.teamcode.Core;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Intended to be inherited from driver portion OpModes.
 * Takes "---Core" classes, mapping them to controller inputs depending on controllerNum.
 * All contained methods will receive "controllerNum" stating which gamepad/controller controls each system part.
 * For example, "updateMotorTank(1)" will have controller 1 control the robot.
 * If you'd like to use FTC Dashboard, please refer to https://acmerobotics.github.io/ftc-dashboard/,
 *  then "Getting Started." */
public abstract class SystemsManager extends OpMode {
    // Orientation classes
    protected IMUCore imuCore;
    // Core classes
    protected DrivetrainCore drivetrainCore;
    protected ArmCore armCore;
    protected ClawCore clawCore;
    protected DroneLauncherCore droneLauncherCore;
    // FTC Dashboard telemetry variables
    protected FtcDashboard dashboard;
    protected Telemetry dashboardTelemetry;

    @Override
    public void init() {
        // Initialize classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        droneLauncherCore = new DroneLauncherCore(hardwareMap);
        imuCore = new IMUCore(hardwareMap);
        // Make preloading work by closing claw
        clawCore.close();
        // Initialize FTC Dashboard variables
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "22531");
    }

    /** Updates IMU data
     * VERY IMPORTANT, as everything else will break if updateIMU() is not ran */
    protected void updateIMU() {
        imuCore.update();
    }

    /** Receives a gamepad joystick input and returns zero if below a value. */
    private double noDrift(double stick, double drift) {
        if (stick < drift && stick > -drift) {
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
                if (gamepad1.x || gamepad1.y) { // Move forward or backward at set rate
                    strafe = 0;
                    rotate = 0;
                    forward = (gamepad1.y) ? -0.5 : 0.5; // Backwards movement prioritized over forwards
                    break;
                }
                forward = noDrift(gamepad1.left_stick_y, 0.05);
                strafe = noDrift(gamepad1.left_stick_x, 0.05);
                rotate = noDrift(gamepad1.right_stick_x, 0.05);
                break;
            case 2:
                if (gamepad2.x || gamepad2.y) { // Move forward (X) or backward (Y) at set rate
                    strafe = 0;
                    rotate = 0;
                    forward = (gamepad2.y) ? -0.5 : 0.5; // Backwards movement prioritized over forwards
                    break;
                }
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
        drivetrainCore.updateAlignerPID(imuCore.getYaw());
        double[] translateArr = drivetrainCore.translate(forward, strafe);
        double[] rotateArr = drivetrainCore.rotate(rotate);
        double[] powers = new double[4];
        for (int i=0; i<4; i++) {
            powers[i] = translateArr[i] + rotateArr[i] - drivetrainCore.getAlignerPIDPower();
        }
        drivetrainCore.setPowers(powers);
    }

    /** Updates arm movement.
     * Right and left trigger moves the arm.
     * Uses setPower(). Only use if ArmCore's RUN_TO_POSITION doesn't work.
     * @param controllerNum Determines the driver number that operates the machine system.
     *                      Receives 1 or 2; otherwise does nothing. */
    protected void updateArm(int controllerNum){
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
        imuCore.telemetry(telemetry);
        // Telemetry sent to FTC Dashboard
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        droneLauncherCore.telemetry(dashboardTelemetry);
        imuCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
