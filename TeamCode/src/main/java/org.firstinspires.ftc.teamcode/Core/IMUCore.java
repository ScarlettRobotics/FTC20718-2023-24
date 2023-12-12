package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/** Class to manage the internal IMU (gyroscope) of the control hub.
 * Can return pitch, yaw, and roll.
 * Used https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html */
public class IMUCore {
    IMU.Parameters IMUparameters;
    YawPitchRollAngles robotOrientation;

    public IMUCore(IMU imu) {
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
    public void update(IMU imu) {
        robotOrientation = imu.getRobotYawPitchRollAngles();
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
}
