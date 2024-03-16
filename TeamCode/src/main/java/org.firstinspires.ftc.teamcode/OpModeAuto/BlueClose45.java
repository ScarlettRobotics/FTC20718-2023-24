package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.ArrayList;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on BlueClose position */
@Autonomous(name = "BlueClose45", group = "blue-close")
public class BlueClose45 extends BlueClose20 {
    // Saved positions of AprilTags on backdrop
    final protected ArrayList<Vector2d> aprilTagCoords = new ArrayList<>();
    protected Pose2d aprilTagPose;

    protected void initialize() {
        super.initialize();

        // blue backdrop
        aprilTagCoords.add(new Vector2d(60.75, 42));
        aprilTagCoords.add(new Vector2d(60.75, 36));
        aprilTagCoords.add(new Vector2d(60.75, 30));
        // red backdrop
        aprilTagCoords.add(new Vector2d(60.75, -30));
        aprilTagCoords.add(new Vector2d(60.75, -36));
        aprilTagCoords.add(new Vector2d(60.75, -42));
        // red pixels
        aprilTagCoords.add(new Vector2d(-72, -36));
        aprilTagCoords.add(new Vector2d(-72, -41.5));
        // blue pixels
        aprilTagCoords.add(new Vector2d(-72, 36));
        aprilTagCoords.add(new Vector2d(-72, 41.5));
    }

    @Override
    public void runOpMode() {
        initialize();

        detectPropInInit();

        placePurple();

        placeYellow();
    }

    protected void placeYellow() {
        /* Move in front of AprilTag depending on propLocation */
        // Move arm to appropriate position before dropping
        armCore.setTargetAngle(155);
        while (!armCore.atTarget(30)) {
            armCore.updateAuto();
            sleep(10);
        }

        clawCore.open();
        sleep(200);
        // Drive backwards to place yellow
        Trajectory moveBackTrajectory = drive.trajectoryBuilder(purpleToBackdropTrajectories.get(propLocation).end())
                .back(8)
                .build();
        drive.followTrajectory(moveBackTrajectory);

        // Move arm to safe position
        armCore.setTargetAngle(45);
        while (!armCore.atTarget(30)) {
            armCore.updateAuto();
            sleep(10);
        }

        // Move to parking
        Trajectory backdropToParkTrajectory = drive.trajectoryBuilder(moveBackTrajectory.end())
                .lineToSplineHeading(new Pose2d(38, 55, Math.toRadians(-180)))
                .splineToSplineHeading(new Pose2d(42, 55, Math.toRadians(-180)), Math.toRadians(0))
                .lineToSplineHeading(new Pose2d(56, 60, Math.toRadians(-180)))
                .build();

        drive.followTrajectory(backdropToParkTrajectory);
        //TODO
        // angle 162deg for yellow pixel placement
        // angle 150deg with scuffed arm
        // X coord ~43" when placing
    }

}