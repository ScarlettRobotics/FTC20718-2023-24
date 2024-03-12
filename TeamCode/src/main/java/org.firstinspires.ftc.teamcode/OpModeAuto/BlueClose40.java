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
    final ArrayList<Vector2d> backdropCoords = new ArrayList<>();
    Trajectory placeOnBackdropTrajectory;
    Pose2d aprilTagPose;

    protected void initialize() {
        super.initialize();

        backdropCoords.add(new Vector2d(60.75, 42));
        backdropCoords.add(new Vector2d(60.75, 36));
        backdropCoords.add(new Vector2d(60.75, 30));
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
        if (aprilTagCore.getDetections().isEmpty()) return;
        // Remove error from previous movement by setting current pose based on AprilTag detection
        AprilTagDetection detectedTag = aprilTagCore.getDetections().get(0);
        // Camera doesn't point parallel with ground so mathematical adjustments have to be made
        double range = detectedTag.ftcPose.range *
                Math.cos(detectedTag.ftcPose.elevation - detectedTag.ftcPose.pitch + Math.toRadians(30));
        // Robot X-coordinate relative to AprilTag (in field orientation)
        double xDist = range *
                Math.sin(detectedTag.ftcPose.yaw + detectedTag.ftcPose.bearing);
        // Robot Y-coordinate relative to AprilTag (in field orientation)
        double yDist = -range *
                Math.cos(detectedTag.ftcPose.yaw + detectedTag.ftcPose.bearing);
        // Robot heading relative to field; RoadRunner specific
        double heading = Math.toRadians(90) - detectedTag.ftcPose.yaw;

        Vector2d detectedTagCoords = backdropCoords.get(detectedTag.id-1);
        aprilTagPose = new Pose2d(
                new Vector2d(detectedTagCoords.getX() + xDist,
                        detectedTagCoords.getY() + yDist),
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

        /* Move in front of AprilTag depending on propLocation */
        // Build trajectory to move to right position
        placeOnBackdropTrajectory = drive.trajectoryBuilder(aprilTagPose)
                .build();
        // Move arm to appropriate position before placing
        //TODO
        // Move to appropriate position
        //TODO
    }

}