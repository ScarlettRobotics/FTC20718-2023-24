package org.firstinspires.ftc.teamcode.AutoCore;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
//org/openftc/easyopencv/OpenCvCamera.java

/** Manages the webcam of the camera to be used by the autonomous portion of running.
 * Stolen from external samples.
 * See FtcRobotController/src/main/java/external.samples/ConceptTensorFlowObjectDetection for original implementation. */
public class VisionPortalCore {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private VisionPortal visionPortal;
    // Create the vision portal by using a builder.
    public VisionPortal.Builder builder = new VisionPortal.Builder();

    // FOR INFO ON WHAT THESE LINES OF CODE ARE DOING, SEE:
    // https://github.com/OpenFTC/EasyOpenCV/blob/master/doc/user_docs/camera_initialization_overview.md
    public VisionPortalCore(HardwareMap hardwareMap){
        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
    }

    /** Builds the Vision Portal, using initialized settings.
     * Run this once during class initialization after all other vision classes have been initialized. */
    public void build() {
        visionPortal = builder.build();
    }



    /** Prevents the Vision Portal from streaming, but does not stop it altogether.
     * Done to save CPU resources. */
    public void stopStreaming() {
        visionPortal.stopStreaming();
    }

    /** Continues streaming the Vision Portal if it has been stopped.
     * Used in conjunction with .stopStreaming() to save CPU resources. */
    public void resumeStreaming() {
        visionPortal.resumeStreaming();
    }

    /** Closes the Vision Portal to save on CPU processing.
     * Use this when you don't need to identify props anymore. */
    public void close() {
        visionPortal.close();
    }
}