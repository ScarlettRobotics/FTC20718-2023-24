package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/** Operates the arm of the robot.
 * Current, only setPower() and telemetry() are useful.
 * The current mode is RUN_USING_ENCODER, making multiple methods unusable.
 * Methods that don't work: getTargetPosition(), goToEncoder(), moveByEncoder()
 * */
public class ArmCore {
    private PIDController armMotor;

    public ArmCore(HardwareMap hardwareMap) {
        armMotor = new PIDController(hardwareMap, "armMotor",
                0.03, 0.0005, 0.0005, 0.7);
    }

    /** Sets a new target position for the motor. */
    public void setTargetPosition(int encoder) {
        armMotor.setTargetPosition(encoder);
    }

    /** Returns targetPosition */
    protected int getTargetPosition() {
        return armMotor.getDeltaTargetPosition();
    }

    /** Updates the PIDController to move towards the provided goal position. */
    public void updateAuto() {
        armMotor.update();
    }

    /** Moves the arm motor as a */
    protected void setPower(double power){
        armMotor.overridePower(power);
    }

    /** Telemetry */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("CURRENT CLASS", "ArmCore.java");
        armMotor.telemetry(telemetry);
    }
}
