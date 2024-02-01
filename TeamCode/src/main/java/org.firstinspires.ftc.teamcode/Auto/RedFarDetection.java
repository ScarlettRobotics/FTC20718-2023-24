package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.*;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

/** Used if on the close side of movement. Place where wheels touch right teeth.
 * Places yellow pixel based on prop position, then places purple pixel on backdrop based on prop position. */
@Autonomous(name = "RedFarDetection", group = "red")
public class RedFarDetection extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    DrivetrainCore drivetrainCore;
    ArmCore armCore;
    ClawCore clawCore;
    TensorFlowCore tensorFlowCore;
    AprilTagCore aprilTagCore;
    // Other
    int propLocation; // 0-1-2 is left-middle-right
    PIDControllerSimple aprilTagAlignerPID;

    private void initialize() {
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(1); // find prop, strafe to align with centre, move arm to safe
        eventManager.addEvent(2.5); // move towards position based on prop
        eventManager.addEvent(4.5); // rotate based on prop
        eventManager.addEvent(5.5); // move purple forward to meet tape

        eventManager.addEvent(7); // move back to org pos
        eventManager.addEvent(8.5); // rotate back to align with AprilTag
        eventManager.addEvent(10); // move forward to see AprilTag
        eventManager.addEvent(12); // align with AprilTag based on propLocation
        eventManager.addEvent(13); // strafe to center
        eventManager.addEvent(14); // move forward to backdrop, set arm to drop pos

        eventManager.addEvent(15.5); // open claw
        eventManager.addEvent(16); // move arm to safe pos, strafe to edge based on propLocation
        eventManager.addEvent(17.5); // move into park

        // Init core classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        tensorFlowCore = new TensorFlowCore(hardwareMap);
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Close claw to grip pixels
        clawCore.close();
    }

    private void updateAuto() {
        drivetrainCore.updateAuto();
        armCore.updateAuto();
    }

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (eventManager.eventOccurred(timer.time(), 0)) {
                if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                    propLocation = 2;
                } else if (tensorFlowCore.getX() > (double) 640 /2) { // to right of camera
                    propLocation = 1;
                } else { // to right of camera
                    propLocation = 0;
                }
                propLocation = 0; // TODO debug forced propLocation
                armCore.setTargetPosition(-2000);
                tensorFlowCore.stopStreaming();
            } // end find prop, strafe to align with centre, move arm to safe
            if (eventManager.eventOccurred(timer.time(), 1)) {
                if (propLocation == 1) {
                    drivetrainCore.forwardByEncoder(1000);
                } else {
                    drivetrainCore.forwardByEncoder(700);
                }
            } // end move towards position based on prop
            if (eventManager.eventOccurred(timer.time(), 2)) {
                if (propLocation == 0) {
                    drivetrainCore.rotateByEncoder(-300);
                } else if (propLocation == 2) {
                    drivetrainCore.rotateByEncoder(300);
                }
            } // end rotate based on prop
            if (eventManager.eventOccurred(timer.time(), 3)) {
                if (propLocation != 1) {
                    drivetrainCore.forwardByEncoder(300);
                }
            } // end move purple forward to meet tape

            if (eventManager.eventOccurred(timer.time(), 4)) {
                if (propLocation == 1) {
                    drivetrainCore.forwardByEncoder(-300);
                } else {
                    drivetrainCore.forwardByEncoder(-200);
                }
            } // end move back to org pos
            if (eventManager.eventOccurred(timer.time(), 5)) {
                if (propLocation == 0) {
                    drivetrainCore.rotateByEncoder(700);
                } else if (propLocation == 1) {
                    drivetrainCore.rotateByEncoder(500);
                } else {
                    drivetrainCore.rotateByEncoder(300);
                }
            } // end rotate back to align with AprilTag
            if (eventManager.eventOccurred(timer.time(), 6)) {
                if (propLocation != 1) {
                    drivetrainCore.strafeByEncoder(300);
                }
            } // end strafe to center

            if (eventManager.eventOccurred(timer.time(), 7)) {
                drivetrainCore.forwardByEncoder(500);
            } // end move forward to see AprilTag
            if (eventManager.eventOccurred(timer.time(), 8)) {
                aprilTagCore = new AprilTagCore(hardwareMap, 2);
                aprilTagAlignerPID = new PIDControllerSimple("AprilTag aligner", 0, 0, 0, 0.3);
                aprilTagAlignerPID.setTargetPosition(0); // goal of X = 0 with apriltag
            } // end align with AprilTag based on propLocation
            if (eventManager.getActionTaken(8) && !eventManager.getActionTaken(9)) {
                // Update aligner with correct AprilTag ID
                List<AprilTagDetection> currentDetections = aprilTagCore.getDetections();
                for (AprilTagDetection detection : currentDetections) {
                    // Align with ID that is same position as propLocation
                    // Works with red only, as blue has different AprilTags and IDs (+4 will have to be changed)
                    if (detection.id != propLocation+4) continue;
                    aprilTagAlignerPID.update(detection.ftcPose.x);
                    break;
                }
                // Strafe drivetrain based on AprilTag
                drivetrainCore.setPowers(
                        drivetrainCore.translate(0, aprilTagAlignerPID.getPower()));
            } // end align with AprilTag
            if (eventManager.eventOccurred(timer.time(), 9)) {
                aprilTagCore.closeVisionPortal();
                drivetrainCore.forwardByEncoder(300);
                armCore.setTargetPosition(-2400);
            } // end move forward to backdrop, set arm to drop pos

            if (eventManager.eventOccurred(timer.time(), 10)) {
                clawCore.open();
            } // end open claw
            if (eventManager.eventOccurred(timer.time(), 11)) {
                if (propLocation == 0) {
                    drivetrainCore.strafeByEncoder(-400);
                } else if (propLocation == 1) {
                    drivetrainCore.strafeByEncoder(-300);
                } else {
                    drivetrainCore.strafeByEncoder(-200);
                }
                armCore.setTargetPosition(-300);
            } // end move arm to safe pos, strafe to edge based on propLocation
            if (eventManager.eventOccurred(timer.time(), 12)) {
                drivetrainCore.forwardByEncoder(200);
            } // end move into park

            addTelemetry(telemetry);
        }
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry); //reuse ".telemetry(telemetry)"
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        tensorFlowCore.telemetry(telemetry);
        if (aprilTagCore != null) {
            aprilTagCore.telemetry(telemetry);
        }
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        eventManager.telemetry(dashboardTelemetry); //reuse ".telemetry(dashboardTelemetry)"
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        tensorFlowCore.telemetry(dashboardTelemetry);
        if (aprilTagCore != null) {
            aprilTagCore.telemetry(dashboardTelemetry);
        }
        dashboardTelemetry.update();
    }
}