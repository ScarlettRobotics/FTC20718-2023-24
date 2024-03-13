package org.firstinspires.ftc.teamcode.OpModeAuto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;

/** Uses RoadRunner to score 40 autonomous points (20 from purple, 20 from yellow).
 * This class only works on BlueClose position */
@Autonomous(name = "BlueClose40", group = "blue-close")
public class BlueClose40 extends BlueClose20 {
    // Saved positions of AprilTags on backdrop
    final ArrayList<Vector2d> aprilTagCoords = new ArrayList<>();
    Trajectory placeOnBackdropTrajectory;
    Pose2d aprilTagPose;

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
    }

    /** When an AprilTag is detected, the robot can very precisely tell where it is by using ftcPose data.
     * Make sure that the robot is 100% detecting an AprilTag or nothing will happen */
    protected void detectPositionFromAprilTag() {
        // offset is 8.125" (x) by 1.5" (y)
        // Distance from camera to robot's center (pythagorean theorem)
        final double centerOffsetMagnitude = 8.262;
        // Heading that centerOffsetMagnitude faces when the robot's heading is 0 (TOA)
        final double centerOffsetHeading = 10.46;

        if (aprilTagCore.getDetections().isEmpty()) return;
        // Remove error from previous movement by setting current pose based on AprilTag detection
        AprilTagDetection detectedTag = aprilTagCore.getDetections().get(0);
        // Camera doesn't point parallel with ground so mathematical adjustments have to be made
        double range = detectedTag.ftcPose.range * Math.cos(Math.toRadians(23));

        double theta = Math.toRadians(detectedTag.ftcPose.yaw + detectedTag.ftcPose.bearing); // see math notes
        // Robot X-coordinate relative to AprilTag (in field orientation)
        double xDist = range * Math.cos(theta);
        // Robot Y-coordinate relative to AprilTag (in field orientation)
        double yDist = range * Math.sin(theta);
        // Robot heading relative to field; RoadRunner specific
        double heading = -detectedTag.ftcPose.yaw;

        Vector2d detectedTagCoords = aprilTagCoords.get(detectedTag.id-1);
        aprilTagPose = new Pose2d(
                new Vector2d(
                        (detectedTagCoords.getX() - xDist) - // camera's x coordinate
                                (Math.cos(Math.toRadians(heading + centerOffsetHeading)) * centerOffsetMagnitude), // move coordinate to robot's center
                        (detectedTagCoords.getY() - yDist) - // camera's y coordinate
                                (Math.sin(Math.toRadians(heading + centerOffsetHeading)) * centerOffsetMagnitude)), // move coordinate to robot's center
                heading);
        drive.setPoseEstimate(aprilTagPose);
    }

    protected void placeYellow() {
        // Re-initialize VisionPortalCore solely for AprilTagDetections
        visionPortalCore = new VisionPortalCore(hardwareMap);
        aprilTagCore = new AprilTagCore(hardwareMap, visionPortalCore.builder, 2);
        visionPortalCore.build();

        // Wait until robot detects an AprilTag
        while (aprilTagCore.getDetections().isEmpty()) {
            sleep(10);
        }
        detectPositionFromAprilTag();
        visionPortalCore.stopStreaming(); // save cpu/resources

        /* Move in front of AprilTag depending on propLocation */
        // Build trajectory to move to right position
        placeOnBackdropTrajectory = drive.trajectoryBuilder(aprilTagPose)
                .build();
        // Move arm to appropriate position before placing
        //TODO
        // Move to appropriate position
        //TODO
        // angle 162deg for yellow pixel placement
        // angle 142deg with scuffed arm
        // X coord ~43" when placing
    }

}