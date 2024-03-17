package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

/** Uses RoadRunner to score 45 autonomous points (20 from purple, yellow, and 5 points from parking).
 * This class only works on BlueFar position */
@Autonomous(name = "BlueFar45", group = "blue-far")
public class BlueFar45 extends RoadRunnerStarter {
    // Roadrunner variables
    int propLocation;
    Pose2d startPose;
    ArrayList<Trajectory> placePurpleTrajectories;
    ArrayList<Trajectory> purpleToBackdropTrajectories;
    ArrayList<Trajectory> backdropToParkTrajectories;

    protected void initialize() {
        super.initialize();

        /* TRAJECTORIES */
        // Detected game prop location
        propLocation = 0;

        // The robot's starting position
        startPose = new Pose2d(-39.9075, -63.3825, Math.toRadians(90));

        drive.setPoseEstimate(startPose); // prevent PID from trying to self correct

        // Updated Trajectories
        placePurpleTrajectories = new ArrayList<>(); // based on propLocation, place on tape
        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)

                .forward(1)
                .splineToConstantHeading(new Vector2d(-38, 52), Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(-33, 30, Math.toRadians(-45)), Math.toRadians(-30))
                .build()); // place on left tape

        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)

                .strafeTo(new Vector2d(-37, 55.5))
                .splineToConstantHeading(new Vector2d(-38, 31), Math.toRadians(-90))
                .build()); // place on middle tape

        placePurpleTrajectories.add(drive.trajectoryBuilder(startPose)

                .strafeTo(new Vector2d(-36, 54))
                .splineToConstantHeading(new Vector2d(-49, 40), Math.toRadians(-90))
                .build()); // place on right tape

//-----------------------------------------------------------------------------------------------------------

        purpleToBackdropTrajectories = new ArrayList<>();

        //First Reverse
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(0).end(), true)

                .forward(1)
                .splineToConstantHeading(new Vector2d(-38, 52), Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(-33, 30, Math.toRadians(-45)), Math.toRadians(-30))
                .build()); // place on left tape

        //Go to Backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(0).end())

                .strafeTo(new Vector2d(17, 56))
                .splineToSplineHeading(new Pose2d(40, 36, Math.toRadians(0)), Math.toRadians(0))
                .build());
        //---------------------------------------------------------------------------------------------------

        //First Reverse
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(1).end(), true)

                .strafeTo(new Vector2d(-37, 55.5))
                .splineToConstantHeading(new Vector2d(-38, 31), Math.toRadians(-90))
                .build()); // place on middle tape

        //Go to Backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(1).end())

                .strafeTo(new Vector2d(17, 56))
                .splineToSplineHeading(new Pose2d(40, 36, Math.toRadians(0)), Math.toRadians(0))
                .build());

        //---------------------------------------------------------------------------------------------------

        //First Reverse
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(2).end(), true)

                .strafeTo(new Vector2d(-36, 54))
                .splineToConstantHeading(new Vector2d(-49, 40), Math.toRadians(-90))
                .build()); // place on right tape

        //Go to Backdrop
        purpleToBackdropTrajectories.add(drive.trajectoryBuilder(placePurpleTrajectories.get(2).end())

                .strafeTo(new Vector2d(17, 56))
                .splineToSplineHeading(new Pose2d(40, 36, Math.toRadians(0)), Math.toRadians(0))
                .build());

                /*.lineToSplineHeading(new Pose2d(-40, -50, Math.toRadians(0)))
                .splineToConstantHeading(new Vector2d(0, -57), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(10, -54, Math.toRadians(30)), Math.toRadians(30))

                .build());*/

        /** Arm Code*/

        backdropToParkTrajectories = new ArrayList<>();

        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(0).end())

                .lineToSplineHeading(new Pose2d(20, 20, Math.toRadians(0)))
                .splineToSplineHeading(new Pose2d(56, 10, Math.toRadians(180)), Math.toRadians(0))
                .build());
        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(1).end())

                .lineToSplineHeading(new Pose2d(20, 20, Math.toRadians(0)))
                .splineToSplineHeading(new Pose2d(56, 10, Math.toRadians(180)), Math.toRadians(0))
                .build());
        backdropToParkTrajectories.add(drive.trajectoryBuilder(purpleToBackdropTrajectories.get(2).end())

                .lineToSplineHeading(new Pose2d(20, 20, Math.toRadians(0)))
                .splineToSplineHeading(new Pose2d(56, 10, Math.toRadians(180)), Math.toRadians(0))
                .build());


    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();

        //sleep(1);

    }

    protected void detectPropInInit() {
        // Detect prop while in initialization phase
        while (opModeInInit()) {
            if (!tensorFlowCore.recognizing()) { // not recognizing any cubes
                propLocation = 2;
            } else if (tensorFlowCore.getX() > (double) 640/2) { // to right of camera
                propLocation = 1;
            } else { // to right of camera
                propLocation = 0;
            }
            tensorFlowCore.telemetry(telemetry);
            telemetry.addData("propLocation", propLocation);
            telemetry.update();
            tensorFlowCore.telemetry(dashboardTelemetry);
            dashboardTelemetry.addData("propLocation", propLocation);
            dashboardTelemetry.update();
        }

        waitForStart();
        visionPortalCore.stopStreaming(); // close portal to save cpu/memory
    }

    protected void placePurple() {
        // Auto movement
        // set purple then move out of way for team auto
        drive.followTrajectory(placePurpleTrajectories.get(propLocation));
        drive.followTrajectory(purpleToBackdropTrajectories.get(propLocation));

    }

}