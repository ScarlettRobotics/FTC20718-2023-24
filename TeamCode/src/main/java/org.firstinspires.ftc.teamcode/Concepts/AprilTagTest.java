
package org.firstinspires.ftc.teamcode.Concepts;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.AprilTagCore;
import org.firstinspires.ftc.teamcode.AutoCore.VisionPortalCore;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;

/**
 * This OpMode illustrates the basics of AprilTag recognition and pose estimation,
 * including Java Builder structures for specifying Vision parameters.
 *
 * For an introduction to AprilTags, see the FTC-DOCS link below:
 * https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
 *
 * In this sample, any visible tag ID will be detected and displayed, but only tags that are included in the default
 * "TagLibrary" will have their position and orientation information displayed.  This default TagLibrary contains
 * the current Season's AprilTags and a small set of "test Tags" in the high number range.
 *
 * When an AprilTag in the TagLibrary is detected, the SDK provides location and orientation of the tag, relative to the camera.
 * This information is provided in the "ftcPose" member of the returned "detection", and is explained in the ftc-docs page linked below.
 * https://ftc-docs.firstinspires.org/apriltag-detection-values
 *
 * To experiment with using AprilTags to navigate, try out these two driving samples:
 * RobotAutoDriveToAprilTagOmni and RobotAutoDriveToAprilTagTank
 *
 * There are many "default" VisionPortal and AprilTag configuration parameters that may be overridden if desired.
 * These default parameters are shown as comments in the code below.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 */
@TeleOp(name = "Concept: AprilTag", group = "concept-vision")
public class AprilTagTest extends LinearOpMode {
    private VisionPortalCore visionPortalCore;
    private AprilTagCore aprilTagCore;
    private double range, xDist, yDist, heading;
    private Pose2d aprilTagPose = new Pose2d();
    final ArrayList<Vector2d> backdropCoords = new ArrayList<>();
    // offset is 8.125" (x) by 1.5" (y)
    final double centerOffsetMagnitude = 8.262; // Distance from camera to robot's center (pythagorean theorem)
    final double centerOffsetHeading = 10.46; // Heading that centerOffsetMagnitude faces when the robot's heading is 0 (TOA)

    @Override
    public void runOpMode() {
        backdropCoords.add(new Vector2d(60.75, 42));
        backdropCoords.add(new Vector2d(60.75, 36));
        backdropCoords.add(new Vector2d(60.75, 30));

        visionPortalCore = new VisionPortalCore(hardwareMap);
        aprilTagCore = new AprilTagCore(hardwareMap, visionPortalCore.builder, 2);
        visionPortalCore.build();

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                detectPositionFromAprilTag();
                telemetry(telemetry);

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortalCore.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortalCore.resumeStreaming();
                }

                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortalCore.close();

    }   // end method runOpMode()

    /** When an AprilTag is detected, the robot can very precisely tell where it is by using ftcPose data.
     * Make sure that the robot is 100% detecting an AprilTag or nothing will happen */
    private void detectPositionFromAprilTag() {
        if (aprilTagCore.getDetections().isEmpty()) return;
        // Remove error from previous movement by setting current pose based on AprilTag detection
        AprilTagDetection detectedTag = aprilTagCore.getDetections().get(0);
        // Camera doesn't point parallel with ground so mathematical adjustments have to be made
        range = detectedTag.ftcPose.range * Math.cos(Math.toRadians(23));

        double theta = Math.toRadians(detectedTag.ftcPose.yaw + detectedTag.ftcPose.bearing); // see math notes
        // Robot X-coordinate relative to AprilTag (in field orientation)
        xDist = range * Math.cos(theta);
        // Robot Y-coordinate relative to AprilTag (in field orientation)
        yDist = range * Math.sin(theta);
        // Robot heading relative to field; RoadRunner specific
        heading = -detectedTag.ftcPose.yaw;

        Vector2d detectedTagCoords = backdropCoords.get(detectedTag.id-1);
        aprilTagPose = new Pose2d(
                new Vector2d(
                        (detectedTagCoords.getX() - xDist) - // camera's x coordinate
                                (Math.cos(Math.toRadians(heading + centerOffsetHeading)) * centerOffsetMagnitude), // move coordinate to robot's center
                        (detectedTagCoords.getY() - yDist) - // camera's y coordinate
                                (Math.sin(Math.toRadians(heading + centerOffsetHeading)) * centerOffsetMagnitude)), // move coordinate to robot's center
                heading);
    }

    private void telemetry(Telemetry telemetry) {
        telemetry.addData("range", range);
        telemetry.addData("xDist", xDist);
        telemetry.addData("yDist", yDist);
        telemetry.addData("heading", heading);
        telemetry.addData("Pose data", " %.2f %.2f %.0f",
                aprilTagPose.getX(),
                aprilTagPose.getY(),
                aprilTagPose.getHeading());
        
        aprilTagCore.telemetry(telemetry);
        telemetry.update();
    }

}   // end class
