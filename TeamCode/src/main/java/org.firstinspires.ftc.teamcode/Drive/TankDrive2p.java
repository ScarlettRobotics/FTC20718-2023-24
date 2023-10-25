package org.firstinspires.ftc.teamcode.Drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Core.*;


/**
 * ServoTest
 * Designed to find the best servo locations for opening and closing the claw.
 */
@TeleOp(name = "TankDrive2p", group = "auto")
public class TankDrive2p extends OpMode {
    // Class variables
    protected ClawCore clawCore;
    protected DualMotorDrive dualMotorDrive;

    // Gamepad variables
    protected boolean pgamepad2a;

    @Override
    public void init() {
        clawCore = new ClawCore(hardwareMap);
        dualMotorDrive = new DualMotorDrive(hardwareMap);
        // Telemetry
        telemetry.addData("STATUS: ", "Initialized"); // the FTC equivalent to println()
        telemetry.addData("FTC Team #", "22531");
    }

    @Override
    public void loop() {
        // Update drive system
        dualMotorDrive.move(gamepad1.left_stick_y, gamepad1.right_stick_y);

        // Update claw
        if (gamepad2.a) {
            clawCore.open();
        }
        if (gamepad2.b) {
            clawCore.close();
        }
        //clawCore.update();

        // Telemetry
        telemetry(telemetry);

        pgamepad2a = gamepad2.a;
    }

    public void telemetry(Telemetry telemetry) {
        clawCore.telemetry(telemetry);
        dualMotorDrive.telemetry(telemetry);
    }
}
