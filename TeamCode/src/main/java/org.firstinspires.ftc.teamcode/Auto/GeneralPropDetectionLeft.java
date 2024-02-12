package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.*;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DrivetrainCore;

/** Used if on the close side of movement. Place where wheels touch right teeth.
 * Places yellow pixel based on prop position, then places purple pixel on backdrop based on prop position. */
@Autonomous(name = "GeneralPropDetectionLeft", group = "general")
public class GeneralPropDetectionLeft extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    DrivetrainCore drivetrainCore;
    ClawCore clawCore;
    // Vision classes
    VisionPortalCore visionPortalCore;
    TensorFlowCore tensorFlowCore;
    // Other
    int propLocation; // 0-1-2 is left-middle-right

    private void initialize() {
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(6); // find prop, strafe to align with centre, move arm to safe
        eventManager.addEvent(7.5); // move towards position based on prop
        eventManager.addEvent(9.5); // rotate based on prop
        eventManager.addEvent(10.5); // move purple forward to meet tape
        eventManager.addEvent(12); // move back to org pos

        // Init core classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        // Init vision classes
        visionPortalCore = new VisionPortalCore(hardwareMap);
        tensorFlowCore = new TensorFlowCore(hardwareMap, visionPortalCore.builder);
        visionPortalCore.build();
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();
        // Initialize claw to fit size reqs
        clawCore.initialize();
    }

    private void updateAuto() {
        drivetrainCore.updateAuto();
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
                } else if (tensorFlowCore.getX() > (double) 640/2) { // to right of camera
                    propLocation = 1;
                } else { // to right of camera
                    propLocation = 0;
                }
                visionPortalCore.stopStreaming();
                drivetrainCore.strafeByEncoder(-100);
            } // end find prop, strafe to align with centre, move arm to safe
            if (propLocation == 0) { // Left
                if (eventManager.eventOccurred(timer.time(), 1)) {
                    drivetrainCore.forwardByEncoder(700);
                } // end move towards position based on prop
                if (eventManager.eventOccurred(timer.time(), 2)) {
                    drivetrainCore.rotateByEncoder(300);
                } // end rotate based on prop
                if (eventManager.eventOccurred(timer.time(), 3)) {
                    drivetrainCore.forwardByEncoder(150);
                } // end move purple forward to meet tape
                if (eventManager.eventOccurred(timer.time(), 4)) {
                    drivetrainCore.forwardByEncoder(-250);
                } // end move back to org pos
            }
            else if (propLocation == 1) { // Middle
                if (eventManager.eventOccurred(timer.time(), 1)) {
                    drivetrainCore.forwardByEncoder(950);
                } // end move towards position based on prop
                if (eventManager.eventOccurred(timer.time(), 4)) {
                    drivetrainCore.forwardByEncoder(-300);
                } // end move back to org pos
            }
            else { // Right
                if (eventManager.eventOccurred(timer.time(), 1)) {
                    drivetrainCore.forwardByEncoder(650);
                } // end move towards position based on prop
                if (eventManager.eventOccurred(timer.time(), 2)) {
                    drivetrainCore.rotateByEncoder(-300);
                } // end rotate based on prop
                if (eventManager.eventOccurred(timer.time(), 3)) {
                    drivetrainCore.forwardByEncoder(350);
                } // end move purple forward to meet tape
                if (eventManager.eventOccurred(timer.time(), 4)) {
                    drivetrainCore.forwardByEncoder(-350);
                } // end move back to org pos
            }

            // TELEMETRY
            addTelemetry(telemetry);
        }
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        addTelemetryInstance(telemetry);
        // FTC Dashboard
        addTelemetryInstance(dashboardTelemetry);
    }

    private void addTelemetryInstance(Telemetry telemetry) {
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry); //reuse ".telemetry(dashboardTelemetry)"
        drivetrainCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        tensorFlowCore.telemetry(telemetry);
        telemetry.update();
    }
}