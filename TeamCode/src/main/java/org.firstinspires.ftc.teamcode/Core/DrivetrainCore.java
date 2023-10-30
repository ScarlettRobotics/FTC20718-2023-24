package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

/*** Manages the drivetrain of the robot.
 * This drivetrain uses an X-drive for the robot, using an ArrayList to init DcMotors.
 * Requires an expansion hub, as all motors will be taken on the control hub. */
public class DrivetrainCore {
    // Initialize motor variables
    private ArrayList<DcMotor> motors;

    // Map motor variables to driver hub
    public DrivetrainCore(HardwareMap hardwareMap) {
        motors = new ArrayList<DcMotor>();
        for (int i=0; i<4; i++) {
            motors.add(hardwareMap.get(DcMotor.class, "driveMotor"+i));
        }

        for (DcMotor i : motors) {
            i.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
