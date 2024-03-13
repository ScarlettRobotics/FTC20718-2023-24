package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AutoCore.PIDController;
import org.firstinspires.ftc.teamcode.AutoCore.PIDControllerSimple;

/** Operates the arm of the robot.
 * Current, only setPower() and telemetry() are useful.
 * The current mode is RUN_USING_ENCODER, making multiple methods unusable.
 * Methods that don't work: getTargetPosition(), goToEncoder(), moveByEncoder()
 * */
public class ArmCore {
    private PIDControllerSimple pid;
    final private double Kcos = 0;
    final private int startAngle = 0; //TODO TUNE
    final private PIDController armMotor;

    public ArmCore(HardwareMap hardwareMap) {
        pid = new PIDControllerSimple("armMotor",
                0.01, 0, 0.0002, 0.8);
        armMotor = new PIDController(hardwareMap, "armMotor",
                0.01, 0, 0.0002, 0.8); //TODO ADJUST
    }

    /** Sets a new target position for the motor. */
    public void setTargetPosition(int encoder) {
        armMotor.setTargetPosition(encoder);
    }

    /** Sets a new target angle for the motor.
     * 0 degrees is when the arm is perfectly vertical.
     * Positive angle movement is out from starting position.
     * Use degrees. */
    public void setTargetAngle(double targetAngle) {
        int encoder = (int) ((targetAngle - startAngle) * 10); // TODO CHANGE THIS COEFFICIENT
        armMotor.setTargetPosition(encoder);
    }

    /** Changes the encoder position by the inputted amount. */
    public void moveByEncoder(int encoder) {
        armMotor.moveByEncoder(encoder);
    }

    /** Updates the PIDController to move towards the provided goal position. */
    public void updateAuto() {
        pid.update(armMotor.getEncoderPosition());
        armMotor.update(armMotor.getEncoderPosition());
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
