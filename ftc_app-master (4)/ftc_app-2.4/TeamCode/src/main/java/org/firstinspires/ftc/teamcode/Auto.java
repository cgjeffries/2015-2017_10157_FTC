/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Auto", group="Autonomous")  // @Autonomous(...) is the other common choice
public class Auto extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor intakeMotor;
    DcMotor flywheelMotor;
    long lastFlywheelPos = 0;
    float flywheelPower = 0;
    long lastTime = 0;
    double temporary = 0;
    long velocity = 0;
    long deltaTime = 0;
    float pOut = 0;
    int target = 2000;
    double P = 0.0001;
    private static final long distance = 17200;
    long start = 0;

    double temporaryHolder = 0;

    double getDeltaTime(){
        deltaTime = System.currentTimeMillis() - lastTime;
        //temporaryHolder = System.currentTimeMillis();
        lastTime = System.currentTimeMillis();

        //deltaTime = System.currentTimeMillis() - temporaryHolder;

        return deltaTime;
    }

    long getFlyhweelvelocity(){
        velocity = (flywheelMotor.getCurrentPosition() - lastFlywheelPos); // / (50) * 60000) / 103);
        //deltaTime = (getRuntime() - lastTime);
        //getDeltaTime();
        //lastTime = (getRuntime());
        lastFlywheelPos = flywheelMotor.getCurrentPosition();
        return velocity;
    }

    void setDrive(float power){
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }

    @Override
    public void runOpMode() {

        leftMotor  = hardwareMap.dcMotor.get("left");
        rightMotor = hardwareMap.dcMotor.get("right");
        intakeMotor = hardwareMap.dcMotor.get("intake");
        flywheelMotor = hardwareMap.dcMotor.get("flywheel");

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        start = leftMotor.getCurrentPosition();

        final Thread threadFlywheel = new Thread(new Runnable() {

            @Override
            public void run() {
                while(opModeIsActive()){
                    pOut = (float) ((target - getFlyhweelvelocity()) * P);
                    flywheelPower += pOut;
                    flywheelMotor.setPower(flywheelPower);
                    /*
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    */
                }
            }

        });

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */


        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        //threadFlywheel.start();
        long random;
        ;
        // run until the end of the match (driver presses STOP)
        try{
            Thread.sleep(10000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        setDrive(1);
        long initial = leftMotor.getCurrentPosition();
        while(abs(leftMotor.getCurrentPosition() - initial) < distance && opModeIsActive()){
            telemetry.addData("Distance: ",(leftMotor.getCurrentPosition() - initial));
            telemetry.update();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        setDrive(-0.25F);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setDrive(0);
    }
}