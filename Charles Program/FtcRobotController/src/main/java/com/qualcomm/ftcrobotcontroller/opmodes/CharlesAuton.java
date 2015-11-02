package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Charles II on 9/19/2015.
 */
public class CharlesAuton extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void runOpMode() throws InterruptedException{

        motorRight = hardwareMap.dcMotor.get("motor_right");
        motorLeft = hardwareMap.dcMotor.get("motor_left");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        float forward = 0.0F;
        int temp = 0;
        String why = "WHY ON EARTH DID YOU SET TIME TO A NEGATIVE VALUE!?";
        float power = 0.0F;
        int temp2 = 0;


        while(true)
        {
            while(!gamepad1.left_bumper) {
                if (gamepad1.b) {

                    power = power + 0.05F;
                    temp2 = temp2 + 5;
                    sleep(500);
                    telemetry.addData("Motor power:", temp2);
                    while (gamepad1.b) {
                        power = power + 0.05F;
                        temp2 = temp2 + 5;
                        telemetry.addData("Motor power:", temp2);
                        sleep(500);
                    }
                }
                if (gamepad1.x) {

                    power = power - 0.05F;
                    temp2 = temp2 - 5;
                    sleep(500);
                    telemetry.addData("Motor power:", temp2);
                    while (gamepad1.b) {
                        power = power - 0.05F;
                        temp2 = temp2 - 5;
                        telemetry.addData("Motor power:", temp2);
                        sleep(500);
                    }
                }
                sleep(5);
                telemetry.addData("Motor power:", temp2);
                sleep(5);
            }


            while(!gamepad1.right_bumper) {

                if (gamepad1.a) {
                    forward = forward + 0.1F;
                    temp = temp + 100;
                    telemetry.addData("Time:", forward);
                    sleep(500);
                    while (gamepad1.a) {
                        forward = forward + 0.1F;
                        temp = temp + 100;
                        telemetry.addData("Time:", forward);
                        sleep(500);
                    }

                }
                else if (gamepad1.y){
                    forward = forward - 0.1F;
                    temp = temp + 100;
                    telemetry.addData("Time:", forward);
                    sleep(500);
                    while (gamepad1.y){
                        forward = forward - 0.1F;
                        temp = temp - 100;
                        telemetry.addData("Time:", forward);
                        sleep(500);
                    }
                }
                sleep(5);
                telemetry.addData("Time:", forward);
                sleep(5);
            }
            if(temp < 0){
                int stuff = 0;
                while(stuff < 400)
                telemetry.addData(why, " ");
                stuff = stuff + 1;
                sleep(10);
                //telemetry.b.put();
            }
            else{
                motorLeft.setPower(power);
                motorRight.setPower(power);
                sleep(temp);
                motorLeft.setPower(0.0);
                motorRight.setPower(0.0);
            }






        }
    }

}
