package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Charles II on 9/19/2015.
 */
public class ThunderclapAuto extends LinearOpMode {

    static float leftMotorMaxPower = (float) 0.2;
    static float rightMotorMaxPower = (float) 0.2;
    private int encoderValueRight = 0;
    private int encoderValueLeft = 0;
    private int temp = 0;
    private int previousValueRight = 0;
    private int previousValueLeft = 0;
    private boolean encoderReset = true;
    private float Kp = 0.000001F;
    private float Kd = 0.000F;
    private float PDOut = 0.0F;
    private int PDIn = 0;
    private boolean done = false;
    private boolean PDEnabled = true;
    private int target = 1000000;
    private float error = 0;
    private float previousError = 0;
    private float derivative = 0;
    private static int t = 0;
    private boolean issomethingwrong = false;
    private int count = 0;
    private int test = 0;
    private boolean on = false;

    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor motorRightRear;
    DcMotor motorLeftRear;
    DcMotor motorArm;
    Servo claw1;
    Servo claw2;

    @Override
    public void runOpMode() throws InterruptedException{

        while(!opModeIsActive()){
            sleep(1);
        }


        motorRightFront = hardwareMap.dcMotor.get("motor_RF");
        motorLeftFront = hardwareMap.dcMotor.get("motor_LF");
        motorRightRear = hardwareMap.dcMotor.get("motor_RR");
        motorLeftRear = hardwareMap.dcMotor.get("motor_LR");
        motorArm = hardwareMap.dcMotor.get("motor_Arm");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        //motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        //motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        //motorRightFront.setDirection(DcMotor.Direction.REVERSE);

        //claw1 = hardwareMap.servo.get("servo_1");
        //claw2 = hardwareMap.servo.get("servo_2");
        //claw2.setDirection(Servo.Direction.REVERSE);
        //gamepad1.setJoystickDeadzone(0.05F);






        new Thread(new Runnable() {
            @Override
            public void run() {
                while (opModeIsActive()) {
                    if(encoderReset == true){
                        encoderValueRight = 0;
                        encoderValueLeft = 0;
                        encoderReset = false;
                    }
                    else{
                        //temp = motorRightRear.getCurrentPosition() - previousValueRight;
                        //previousValueRight = motorRightRear.getCurrentPosition();
                        //encoderValueRight += temp;

                        temp = (-1 * motorLeftRear.getCurrentPosition()) - previousValueLeft;
                        previousValueLeft = motorLeftRear.getCurrentPosition();
                        encoderValueLeft += temp;
                    }

                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                issomethingwrong = true;
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                error = 0;
                previousError = 0;
                float temp = 0;
                while (opModeIsActive()) {
                    if(PDEnabled == true)
                    {
                        error = target - PDIn;

                        derivative = error - previousError;

                        previousError = error;
                        temp = (error * Kp) + (derivative * Kd);
                        PDIn = encoderValueLeft;
                        if(temp > 1.0){

                            temp = 1.0F;
                        }
                        else if(temp < -1.0){

                            temp = -1.0F;
                        }
                        PDOut = temp;
                    }

                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                issomethingwrong = true;

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                error = 0;
                previousError = 0;
                float temp = 0;
                while (opModeIsActive()) {
                    
                    telemetry.addData("count", count);
                    telemetry.addData("opModeIsActive", opModeIsActive());
                    telemetry.addData("PDout", PDOut);
                    telemetry.addData("power", motorLeftRear.getPower());
                    telemetry.addData("test", test);
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                issomethingwrong = true;

            }
        }).start();

        //new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while(true)
//                {
//                    target += gamepad1.left_stick_y * 100;
//
//                    try {
//                        sleep(10);
//                    } catch (InterruptedException e) {
//                       e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();

        //new Thread((Runnable) () ){

        //}

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true && opModeIsActive()) {

                    //boolean on = true;


                    while (on && opModeIsActive()) {
                        if (error < 100 && count < 200) {
                            count++;
                        } else if (error < 100 && count >= 200) {
                            on = false;
                            done = true;
                        }
                        else
                        {
                            count = 0;
                        }

                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    while(done && opModeIsActive())
                    {
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }).start();

        sleep(5000);
        on = true;

        int temp = 0;
        float power = 0.0F;

       //target = 10000;

        done = false;
        encoderValueLeft = 0;
        while(opModeIsActive() && !done)
        {
            test++;
            PDIn = encoderValueLeft;
            if(PDOut > 0.5){

                PDOut = 0.5F;

            }
            else if(PDOut < 0.0){

                PDOut = 0.0F;
            }

            motorLeftRear.setPower(PDOut);
            motorRightRear.setPower(PDOut);
            motorLeftFront.setPower(PDOut);
            motorRightFront.setPower(PDOut);
            telemetry.addData("PDOut:", PDOut);
            telemetry.addData("error:", error);
            sleep(10);
        }
        issomethingwrong = true;

        motorLeftRear.setPower(0.0);
        motorLeftFront.setPower(0.0);
        motorRightRear.setPower(0.0);
        motorRightFront.setPower(0.0);

        sleep(500);

        motorLeftRear.setPower(0.0);
        motorLeftFront.setPower(0.0);
        motorRightRear.setPower(0.0);
        motorRightFront.setPower(0.0);





        //If all else fails...

        //motorRightFront.setPower(0.0);
        //motorLeftFront.setPower(0.0);
        //motorRightRear.setPower(0.0);
        //motorLeftRear.setPower(0.0);

        //sleep(0);

        //motorRightFront.setPower(0.0);
        //motorLeftFront.setPower(0.0);
        //motorRightRear.setPower(0.0);
        //motorLeftRear.setPower(0.0);




        /*
        while(true) {
            if (gamepad1.left_bumper) {

                while (!gamepad1.left_bumper) {
                    if (gamepad1.b) {

                        power = power + 0.05F;
                        temp = temp + 5;
                        sleep(500);
                        telemetry.addData("Joystick deadzone:", temp);
                        while (gamepad1.b) {
                            power = power + 0.05F;
                            temp = temp + 5;
                            telemetry.addData("Joystick deadzone:", temp);
                            sleep(500);
                        }
                    }
                    if (gamepad1.x) {

                        power = power - 0.05F;
                        temp = temp - 5;
                        sleep(500);
                        telemetry.addData("Joystick deadzone:", temp);
                        while (gamepad1.b) {
                            power = power - 0.05F;
                            temp = temp - 5;
                            telemetry.addData("Joystick deadzone:", temp);
                            sleep(500);
                        }
                    }
                    sleep(5);
                    telemetry.addData("Joystick deadzone:", temp);
                    gamepad1.setJoystickDeadzone(power);
                    sleep(5);
                }

            }


            if (gamepad1.right_bumper) {

                leftMotorMaxPower = 1;
                rightMotorMaxPower = 1;

            } else {
                leftMotorMaxPower = (float) 0.2;
                rightMotorMaxPower = (float) 0.2;
            }


            float left = -gamepad1.left_stick_y;
            float right = -gamepad1.right_stick_y;

            left = left * leftMotorMaxPower;
            right = right * rightMotorMaxPower;

            right = Range.clip(right, -rightMotorMaxPower, rightMotorMaxPower);
            left = Range.clip(left, -leftMotorMaxPower, leftMotorMaxPower);

            motorLeftRear.setPower(left);
            motorRightRear.setPower(right);

            //telemetry.addData("1-motor left power", left);
            //telemetry.addData("2-motor right power", right);
            //telemetry.addData("Joystick deadzone", power);
            //telemetry.addData("Real encoder value left", motorLeft.getCurrentPosition());
            //telemetry.addData("Adjusted encoder value left", encoderValueLeft);
            //telemetry.addData("Real encoder value right", motorRight.getCurrentPosition());
            //telemetry.addData("Adjusted encoder value right", encoderValueRight);

            //telemetry.addData();
            sleep(10);
        }
        */






    }
}