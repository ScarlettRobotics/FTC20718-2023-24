package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.ArmCore;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.Core.DrivetrainCore;
import org.firstinspires.ftc.teamcode.Core.EventManager;

@Autonomous(name = "RedBackdrop", group = "red")
public class RedBackdrop extends LinearOpMode {
    // Timing related
    private ElapsedTime timer;
    private EventManager eventManager;
    // Core classes
    protected DrivetrainCore drivetrainCore;
    protected ArmCore armCore;
    protected ClawCore clawCore;
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            updateAuto();

            if (eventManager.eventOccurred(timer.time(), 0)) {
                drivetrainCore.forwardByEncoder(750);
            }

            if (eventManager.eventOccurred(timer.time(), 1)) {
                drivetrainCore.rotateByEncoder(-650);
            }

            if (eventManager.eventOccurred(timer.time(), 2)) {
                drivetrainCore.forwardByEncoder(1200);
                armCore.setTargetPosition(-2700);
            }

            if (eventManager.eventOccurred(timer.time(), 3)) {
                clawCore.open();
            }

            if (eventManager.eventOccurred(timer.time(), 4)) {
                drivetrainCore.forwardByEncoder(-200);
            }

            if (eventManager.eventOccurred(timer.time(), 5)) {
                drivetrainCore.strafeByEncoder(750);
                armCore.setTargetPosition(-500);
            }

            if (eventManager.eventOccurred(timer.time(), 6)) {
                drivetrainCore.forwardByEncoder(500);
            }

            addTelemetry(telemetry);
        }
    }

    private void initialize() {
        // Init timing related
        timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        eventManager = new EventManager();
        // Init timings
        eventManager.addEvent(0.1);
        eventManager.addEvent(1.5);
        eventManager.addEvent(3);
        eventManager.addEvent(4.5);
        eventManager.addEvent(5);
        eventManager.addEvent(6.5);
        eventManager.addEvent(8);
        // Init core classes
        drivetrainCore = new DrivetrainCore(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
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

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        drivetrainCore.telemetry(telemetry);
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        drivetrainCore.telemetry(dashboardTelemetry);
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }
}
