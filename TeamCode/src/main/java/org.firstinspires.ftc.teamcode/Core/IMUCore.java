package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/** Class to manage the internal IMU (gyroscope) of the control hub.
 * Can return pitch, yaw, roll, and angular velocities.
 * Used https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html */
public class IMUCore {
    private final IMU imu;
    private YawPitchRollAngles robotOrientation;
    private AngularVelocity robotAngularVelocity;

    public IMUCore(HardwareMap hardwareMap) {
        imu = hardwareMap.get(IMU.class, "imu");
        // Init IMU
        imu.initialize(
            new IMU.Parameters(
                    // Set IMU orientation
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                    )
            )
        );
    }

    /** Gets all IMU information, then stores it in class variables.
     * Run this before running ".get___()". */
    public void update() {
        robotOrientation = imu.getRobotYawPitchRollAngles();
        robotAngularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);
    }

    /** Resets IMU yaw to 0. Use to prevent gyro drift. */
    public void resetYaw() {
        imu.resetYaw();
    }


    /** Returns current yaw of IMU. */
    public double getYaw() {
        return robotOrientation.getYaw(AngleUnit.DEGREES);
    }

    /** Returns current pitch of IMU. */
    public double getPitch() {
        return robotOrientation.getPitch(AngleUnit.DEGREES);
    }

    /** Returns current roll of IMU. */
    public double getRoll() {
        return robotOrientation.getRoll(AngleUnit.DEGREES);
    }

    /** Returns current X angular velocity. */
    public double getAngularVelocityX() {
        return robotAngularVelocity.xRotationRate;
    }

    /** Returns current Y angular velocity. */
    public double getAngularVelocityY() {
        return robotAngularVelocity.yRotationRate;
    }

    /** Returns current Z angular velocity. */
    public double getAngularVelocityZ() {
        return robotAngularVelocity.xRotationRate;
    }

    protected void telemetry(Telemetry telemetry) {
        telemetry.addData("CURRENT CLASS", "IMUCore.java");

        telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", getYaw());
        telemetry.addData("Pitch (X)", "%.2f Deg.", getPitch());
        telemetry.addData("Roll (Y)", "%.2f Deg.\n", getRoll());

        telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", getAngularVelocityZ());
        telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", getAngularVelocityX());
        telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", getAngularVelocityY());
    }
}
