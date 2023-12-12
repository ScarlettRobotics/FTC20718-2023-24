package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.*;

/** Used if on the close side of movement. Place where wheels touch right teeth.
 * Places yellow pixel based on prop position, then places purple pixel on backdrop based on prop position. */
@Autonomous(name = "RedCloseDetection", group = "red")
public class RedCloseDetection extends LinearOpMode {
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
    // Other
    int propLocation; // 0-1-2 is left-middle-right

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (timer.time() < 1.5) {
                if (!tensorFlowCore.recognizing()) {
                    continue;
                } // not recognizing any cubes
                if (tensorFlowCore.getX() > (double) 640 /2) propLocation = 1; // to left of camera
                else propLocation = 2; // to right of camera
            }
            if (eventManager.eventOccurred(timer.time(), 0)) {
                if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                    propLocation = 0;
                    drivetrainCore.forwardByEncoder(100);
                } else if (tensorFlowCore.getX() > (double) 640 /2) { // to left of camera
                    propLocation = 1;
                    drivetrainCore.forwardByEncoder(300);
                } else { // to right of camera
                    propLocation = 2;
                    drivetrainCore.forwardByEncoder(200);
                }
                armCore.setTargetPosition(-1800);
            } // find prop, move towards position based on prop, move arm to safe
            if (eventManager.eventOccurred(timer.time(), 1)) {
                if (propLocation ==  0) {
                    drivetrainCore.rotateByEncoder(-100);
                } else if (propLocation == 2) {
                    drivetrainCore.rotateByEncoder(100);
                }
                armCore.setTargetPosition(-2300);
            } // rotate based on prop, move arm to ground

            if (eventManager.eventOccurred(timer.time(), 2)) {
                clawCore.open();
            } // open claw
            if (eventManager.eventOccurred(timer.time(), 3)) {
                armCore.setTargetPosition(-2250);
            } // slightly move arm up
            if (eventManager.eventOccurred(timer.time(), 4)) {
                clawCore.close();
            } // close claw

            if (eventManager.eventOccurred(timer.time(), 5)) {
                if (propLocation == 0) {
                    drivetrainCore.rotateByEncoder(850);
                } else if (propLocation == 2) {
                    drivetrainCore.rotateByEncoder(650);
                }
                armCore.setTargetPosition(-2000);
            } // rotate back, move claw up
            if (eventManager.eventOccurred(timer.time(), 6)) {
                if (propLocation == 0) {
                    drivetrainCore.strafeByEncoder(-150);
                } else if (propLocation == 1) {
                    drivetrainCore.strafeByEncoder(-100);
                } else {
                    drivetrainCore.strafeByEncoder(-50);
                }
            } // strafe based on prop

            if (eventManager.eventOccurred(timer.time(), 7)) {
                drivetrainCore.forwardByEncoder(1000);
            } // move forward to backdrop
            if (eventManager.eventOccurred(timer.time(), 8)) {
                clawCore.open();
            } // drop pixel on backdrop
            if (eventManager.eventOccurred(timer.time(), 9)) {
                drivetrainCore.forwardByEncoder(-300);
                armCore.setTargetPosition(-500);
            } // move backwards, reset arm

            if (eventManager.eventOccurred(timer.time(), 10)) {
                if (propLocation == 0) {
                    drivetrainCore.strafeByEncoder(-50);
                } else if (propLocation == 1) {
                    drivetrainCore.strafeByEncoder(-100);
                } else {
                    drivetrainCore.strafeByEncoder(-150);
                }
            } // strafe to left square based on prop
            if (eventManager.eventOccurred(timer.time(), 11)) {
                drivetrainCore.forwardByEncoder(200);
            } // move forwards into park

            addTelemetry(telemetry);
        }
    }

    private void initialize() {
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(1); // find prop, move towards position based on prop, move arm to safe
        eventManager.addEvent(2); // rotate based on prop, move arm to ground

        eventManager.addEvent(3); // open claw
        eventManager.addEvent(3.3); // slightly move arm up
        eventManager.addEvent(3.6); // close claw

        eventManager.addEvent(3.9); // rotate back, move claw up
        eventManager.addEvent(5); // strafe based on prop

        eventManager.addEvent(6); // move forward to backdrop
        eventManager.addEvent(7.5); // drop pixel on backdrop
        eventManager.addEvent(8); // move backwards, reset arm

        eventManager.addEvent(8.5); // strafe to left square based on prop
        eventManager.addEvent(10); // move forwards into park
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
        //drivetrainCore.updateAuto(); TODO
        //armCore.updateAuto(); TODO
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        eventManager.telemetry(telemetry); //reuse ".telemetry(telemetry)"
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        tensorFlowCore.telemetry(telemetry);
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        eventManager.telemetry(dashboardTelemetry); //reuse ".telemetry(dashboardTelemetry)"
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        tensorFlowCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}