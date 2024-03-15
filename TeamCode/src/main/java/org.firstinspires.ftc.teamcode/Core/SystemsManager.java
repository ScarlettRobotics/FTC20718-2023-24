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
    private boolean useDrivetrainAligner;
    protected ArmCore armCore;
    protected ClawCore clawCore;
    protected DroneLauncherCore droneLauncherCore;
    // FTC Dashboard telemetry variables
    protected FtcDashboard dashboard;
    protected Telemetry dashboardTelemetry;
    // updateDrivetrain() variables
    private boolean pRotating;

    @Override
    public void init() {
        // Initialize classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        useDrivetrainAligner = true;
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        droneLauncherCore = new DroneLauncherCore(hardwareMap);
        imuCore = new IMUCore(hardwareMap);
        // Grip onto pixels
        clawCore.close();
        // Initialize FTC Dashboard variables
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Initialize updateDrivetrain() variables
        pRotating = false;
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

    private boolean pToggle = false;
    /** Turns the drivetrain aligner on/off depending on current state of .
     * The drivetrain aligner seeks to ensure that the robot always faces the same direction when moving without rotating.
     * @param toggle Toggles the drivetrain aligner based on its previous state. */
    protected void updateDrivetrainAligner(boolean toggle) {
        if (toggle && !pToggle) useDrivetrainAligner = !useDrivetrainAligner;
        pToggle = toggle;
    }

    /** Updates the robot's X-drive drivetrain.
     * @param forward Joystick axis that controls movement forwards/backwards without rotation.
     * @param strafe Joystick axis that controls strafing left/right without rotation.
     * @param rotate Joystick axis that controls rotation without moving.
     * @param slowForward Button that slowly moves the robot forwrads
     * @param slowBackward Button that slowly moves the robot backwards
     * @param slowLeft Button that slowly moves the robot left
     * @param slowRight Button that slowly moves the robot right
     */
    protected void updateDrivetrain(double forward, double strafe, double rotate,
                                    boolean slowForward, boolean slowBackward,
                                    boolean slowLeft, boolean slowRight) {
        // If dpad input received
        boolean setMoving;
        // Set movement
        if (gamepad1.dpad_down || gamepad1.dpad_up) { // Move forward/backward at set rate
            strafe = 0;
            rotate = 0;
            forward = (gamepad1.dpad_up) ? -0.5 : 0.5; // backwards movement prioritized over forwards
            setMoving = true;
        } else if (gamepad1.dpad_left || gamepad1.dpad_right) { // Move left/right at set rate
            strafe = (gamepad1.dpad_left) ? -0.5 : 0.5; // left movement prioritized over right
            rotate = 0;
            forward = 0;
            setMoving = true;
        } else { // Move robot based on joystick
            forward = noDrift(gamepad1.left_stick_y, 0.05);
            strafe = noDrift(gamepad1.left_stick_x, 0.05);
            rotate = noDrift(gamepad1.right_stick_x, 0.05);
            setMoving = false;
        }
        // Processing inputs
        drivetrainCore.updateAlignerPID(imuCore.getYaw());                 // see function
        double[] translateArr = drivetrainCore.translate(forward, strafe); // left joystick
        double[] rotateArr = drivetrainCore.rotate(rotate);                // right joystick
        double[] powers = new double[4];                                   // final power
        if (rotateArr[0] != 0 ||
            !useDrivetrainAligner) { // Driver is rotating robot; alignerPID doesn't influence final power OR not using drivetrain aligner
            for (int i = 0; i < 4; i++) {
                powers[i] = translateArr[i] + rotateArr[i];
            }
        } else if (setMoving) { // Using X/Y button; alignerPID doesn't influence
            for (int i = 0; i < 4; i++) {
                powers[i] = translateArr[i] + rotateArr[i];
            }
        } else { // Driver isn't rotating robot; alignerPID influences final power
            if (pRotating) { // If rotating just stopped, alignerPID needs to be reset to snap to the current position
                imuCore.resetYaw();
            }
            for (int i = 0; i < 4; i++) {
                powers[i] = translateArr[i] + rotateArr[i] - drivetrainCore.getAlignerPIDPower();
            }
        }
        drivetrainCore.setPowers(powers);
        // set prev vars
        pRotating = rotateArr[0] != 0;
    }

    /** Updates arm movement.
     * Right and left trigger moves the arm.
     * Uses setPower(). Only use if ArmCore's RUN_TO_POSITION doesn't work.
     * @param up How much the arm should move up
     * @param down How much the arm should move down */
    protected void updateArm(double up, double down){
        armCore.setPower(up - down);
    }

    /** Updates the claw's movement.
     * A/B opens/closes the claw respectively.
     * Opening will be prioritized over closing the claw if both buttons are pressed.
     * @param open Button to check if claw should be opened.
     * @param close Button to check if claw should be closed. */
    protected void updateClaw(boolean open, boolean close) {
        if (open) clawCore.open();
        if (close) clawCore.close();
    }

    /** Checks for a button press to launch the drone.
     * If drone does not launch, rotate the part attached to the servo by 180 degrees.
     * @param launching Launches the drone if true */
    protected void checkForDroneLaunch(boolean launching) {
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
