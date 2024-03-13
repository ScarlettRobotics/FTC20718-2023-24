package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    final private DcMotor armMotor;

    // average -708.94 encoder / 45 deg or -15.754 encoder/deg
    final private double ANGLE_TO_ENCODER = -15.754;

    public ArmCore(HardwareMap hardwareMap) {
        pid = new PIDControllerSimple("armMotor",
                0.01, 0, 0, 0.8); //TODO ADJUST
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /** Sets a new target position for the motor. TODO WILL BE MADE REDUNDANT */
    public void setTargetPosition(int encoder) {
        pid.setTargetPosition(encoder);
    }

    /** Sets a new target angle for the motor.
     * 0 degrees is when the arm is first parallel with the ground.
     * Positive angle movement is out from starting position.
     * Use degrees. */
    public void setTargetAngle(double targetAngle) {
        int encoder = (int) ((targetAngle - startAngle) * ANGLE_TO_ENCODER); // TODO CHANGE THIS COEFFICIENT
        pid.setTargetPosition(encoder);
    }

    /** Changes the encoder position by the inputted amount. TODO WILL BE MADE REDUNDANT */
    public void moveByEncoder(int encoder) {
        pid.moveByEncoder(encoder);
    }

    /** Updates the PIDController to move towards the provided goal position. */
    public void updateAuto() {
        pid.update(armMotor.getCurrentPosition());
        double gravityCompensation = Kcos * Math.cos(encoderToAngle((int) pid.getTargetPosition()));
        armMotor.setPower(pid.getPower());
    }

    /** Moves the arm motor, ignoring autonomous in the process */
    protected void setPower(double power){
        armMotor.setPower(power);
    }

    private double encoderToAngle(int encoder) {
        return encoder / ANGLE_TO_ENCODER;
    }

    /** Telemetry */
    public void telemetry(Telemetry telemetry) {
        telemetry.addData("CURRENT CLASS", "ArmCore.java");
        pid.telemetry(telemetry);
        telemetry.addData("arm position", armMotor.getCurrentPosition());
    }
}
