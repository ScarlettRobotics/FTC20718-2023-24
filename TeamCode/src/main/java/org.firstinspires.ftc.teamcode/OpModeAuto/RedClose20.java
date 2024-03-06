package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.TensorFlowCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.teamcode.Core.ClawCore;
import org.firstinspires.ftc.teamcode.RRdrive.SampleMecanumDrive;

import java.util.ArrayList;

/** Uses RoadRunner to score 20 autonomous points (20 from purple).
 * This class only works on RedClose position */
@Autonomous(name = "RedClose20", group = "red-close") //TODO
public class RedClose20 extends LinearOpMode {
    // FTC Dashboard
    private FtcDashboard dashboard;
    private Telemetry dashboardTelemetry;
    // Timing related
    private ElapsedTime timer;
    // Vision
    private VisionPortalCore visionPortalCore;
    private TensorFlowCore tensorFlowCore;
    private AprilTagCore aprilTagCore;
    // Core classes
    private SampleMecanumDrive drive;
    private ClawCore clawCore;

    private void initialize() {
        // Init core classes
        drive = new SampleMecanumDrive(hardwareMap);
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
        // Close claw to grip pixels
        clawCore.close();
    }

    @Override
    public void runOpMode() {
        initialize();

        // The robot's starting position
        Pose2d startPose = new Pose2d(9, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose); // prevent PID from trying to self correct

        // Trajectories
        ArrayList<Trajectory> placePurpleTrajectories = new ArrayList<>(); // based on propLocation, place on tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(16, -52), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(7, -40, Math.toRadians(135)), Math.toRadians(150))
                .build()); // place on left tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(12, -32), Math.toRadians(90))
                .build()); // place on middle tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)
                .strafeRight(1)
                .splineToConstantHeading(new Vector2d(12, -50), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(17, -40, Math.toRadians(45)), Math.toRadians(20))
                .build()); // place on right tape

        ArrayList<Trajectory> purpleToBackdropTrajectories = new ArrayList<>(); // reset in front of backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(0).end())
                .strafeTo(new Vector2d(8, -42))
                .splineToConstantHeading(new Vector2d(14, -45), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(45))
                .build());
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(1).end())
                .strafeTo(new Vector2d(12, -35))
                .splineToConstantHeading(new Vector2d(24, -38), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(15))
                .build());
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(2).end())
                .splineToConstantHeading(new Vector2d(14, -43), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(36, -40), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(36, -36, Math.toRadians(0)), Math.toRadians(90))
                .build());

        // Detect prop while in initialization phase
        int propLocation = 0;
        while (opModeInInit()) {
            if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                propLocation = 2;
            } else if (tensorFlowCore.getX() > (double) 640/2) { // to right of camera
                propLocation = 1;
            } else { // to right of camera
                propLocation = 0;
            }
        }

        waitForStart();
        visionPortalCore.stopStreaming(); // close portal to save cpu/memory

        if(isStopRequested()) return;

        // Auto movement
        // set purple then move in front of backdrop
        drive.followTrajectory(placePurpleTrajectories.get(propLocation));
        drive.followTrajectory(purpleToBackdropTrajectories.get(propLocation));
    }

    private void addTelemetry(Telemetry telemetry) {
        // Telemetry
        telemetry.addData("timer", timer.time());
        /* TODO CORE TELEMETRY */
        telemetry.update();
        // FTC Dashboard
        dashboardTelemetry.addData("timer", timer.time());
        /* TODO CORE TELEMETRY */
        dashboardTelemetry.update();
    }

}