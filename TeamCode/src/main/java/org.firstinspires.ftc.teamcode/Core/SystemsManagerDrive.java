package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class SystemsManagerDrive extends OpMode {
    DrivetrainCore drivetrainCore;

    @Override
    public void init() {
        drivetrainCore = new DrivetrainCore(hardwareMap);
    }
}
