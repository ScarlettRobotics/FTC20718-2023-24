package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.TensorFlowCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.teamcode.Core.ArmCore;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.RRdrive.SampleMecanumDrive;

/** Abstract class containing all mechanical initializations with no trajectories.
 * Starting auto for all RoadRunner autonomous programs.
 * Done so that a mechanical change won't have to be done on all classes. */
public abstract class RoadRunnerStarter extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    protected Telemetry dashboardTelemetry;
    // Timing related
    protected ElapsedTime timer;
    // Vision
    protected VisionPortalCore visionPortalCore;
    protected TensorFlowCore tensorFlowCore;
    protected AprilTagCore aprilTagCore;
    // Core classes
    protected SampleMecanumDrive drive;
    protected ArmCore armCore;
    protected ClawCore clawCore;

    protected void initialize() {
        // Init core classes
        drive = new SampleMecanumDrive(hardwareMap);
        armCore = new ArmCore(hardwareMap);
        clawCore = new ClawCore(hardwareMap);
        // Init dashboard
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        // Init vision
        visionPortalCore = new VisionPortalCore(hardwareMap);
        tensorFlowCore = new TensorFlowCore(hardwareMap, visionPortalCore.builder);
        visionPortalCore.build();
        // Init telemetry
        telemetry.addData("STATUS", "Initialized");
        telemetry.update();
        dashboardTelemetry.addData("STATUS", "Initialized");
        dashboardTelemetry.update();

        /* INIT ACTIONS */
        // Close claw to grip pixels
        clawCore.close();
    }

    protected void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        armCore.telemetry(telemetry);
        clawCore.telemetry(telemetry);
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        armCore.telemetry(dashboardTelemetry);
        clawCore.telemetry(dashboardTelemetry);
        dashboardTelemetry.update();
    }

}