package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.Range;



public class TemplateOpMode_Linear extends OpMode {









    DcMotor motorRightFront;

    DcMotor motorLeftFront;

    DcMotor motorRightRear;

    DcMotor motorLeftRear;

    DcMotor motorArm;

    Servo claw1;

    Servo claw2;



    double clawPosition;

    double clawDelta;



    final static double CLAW_MIN_RANGE  = 0.20;

    final static double CLAW_MAX_RANGE  = 0.7;



    private void getEncoderValue(){



    }

    /*

     * Note: the configuration of the servos is such that

     * as the arm servo approaches 0, the arm position moves up (away from the floor).

     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).

     */



    /**

     * Constructor

     */

    // public K9TeleOp() {



    //}



    /*

     * Code to run when the op mode is initialized goes here

     *

     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()

     */

    @Override

    public void init() {





		/*

		 * Use the hardwareMap to get the dc motors and servos by name. Note

		 * that the names of the devices must match the names used when you

		 * configured your robot and created the configuration file.

		 */

		

		/*

		 * For the demo Tetrix K9 bot we assume the following,

		 *   There are two motors "motor_1" and "motor_2"

		 *   "motor_1" is on the right side of the bot.

		 *   "motor_2" is on the left side of the bot and reversed.

		 *   

		 * We also assume that there are two servos "servo_1" and "servo_6"

		 *    "servo_1" controls the arm joint of the manipulator.

		 *    "servo_6" controls the claw joint of the manipulator.

		 */

        motorRightFront = hardwareMap.dcMotor.get("motor_RF");

        motorLeftFront = hardwareMap.dcMotor.get("motor_LF");

        motorRightRear = hardwareMap.dcMotor.get("motor_RR");

        motorLeftRear = hardwareMap.dcMotor.get("motor_LR");

        motorArm = hardwareMap.dcMotor.get("motor_Arm");

        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);

        //motorLeftRear.setDirection(DcMotor.Direction.REVERSE);

        motorRightRear.setDirection(DcMotor.Direction.REVERSE);

        motorRightFront.setDirection(DcMotor.Direction.REVERSE);



        claw1 = hardwareMap.servo.get("servo_1");

        claw2 = hardwareMap.servo.get("servo_2");

        claw2.setDirection(Servo.Direction.REVERSE);



        // assign the starting position of the wrist and claw

        clawPosition = 0.2;

        clawDelta = 0.1;

    }



    /*

     * This method will be called repeatedly in a loop

     *

     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()

     */

    @Override

    public void loop() {



		/*

		 * Gamepad 1

		 * 

		 * Gamepad 1 controls the motors via the left stick, and it controls the

		 * wrist/claw via the a,b, x, y buttons

		 */



        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and

        // 1 is full down

        // direction: left_stick_x ranges from -1 to 1, where -1 is full left

        // and 1 is full right

        float left = -gamepad1.left_stick_y;

        float right = gamepad1.right_stick_y;

        float armUp = gamepad1.right_trigger;

        float armDown = gamepad1.left_trigger;



        double armPower = 0;



        // clip the right/left values so that the values never exceed +/- 1

        right = Range.clip(right, -1, 1);

        left = Range.clip(left, -1, 1);



        // scale the joystick value to make it easier to control

        // the robot more precisely at slower speeds.

        right = (float)scaleInput(right);

        left =  (float)scaleInput(left);



        // write the values to the motors

        motorRightFront.setPower(right);

        motorLeftFront.setPower(left);

        motorRightRear.setPower(right);

        motorLeftRear.setPower(left);



        // update the position of the arm.



        // update the position of the claw

        if (gamepad1.left_bumper) {

            clawPosition += clawDelta;

        }

        else if (gamepad1.right_bumper) {

            clawPosition -= clawDelta;

        }



        //arm code





        if((armDown < 0.05 && armUp < 0.05) || (armDown > 0.05 && armUp > 0.05)){

            armPower = -0.05;

        }

        else if(armDown > 0.05){

            armPower = -(armDown / 2);

        }

        else if(armUp > 0.05){

            armPower = (armUp / 5);

        }









        // clip the position values so that they never exceed their allowed range.

        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);



        // write position values to the wrist and claw servo

        claw1.setPosition(clawPosition);

        claw2.setPosition(clawPosition);



        motorArm.setPower(armPower);





		/*

		 * Send telemetry data back to driver station. Note that if we are using

		 * a legacy NXT-compatible motor controller, then the getPower() method

		 * will return a null value. The legacy NXT-compatible motor controllers

		 * are currently write only.

		 */

        double temp2 = 0;

        temp2 = motorRightRear.getPower();

        //telemetry.addData("Text", "*** Robot Data***");

        //      telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));

        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));

        //telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));

        //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

        telemetry.addData("", temp2);



    }



    /*

     * Code to run when the op mode is first disabled goes here

     *

     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()

     */

    @Override

    public void stop() {



    }



    /*

     * This method scales the joystick input so for low joystick values, the

     * scaled value is less than linear.  This is to make it easier to drive

     * the robot more precisely at slower speeds.

     */

    double scaleInput(double dVal)  {

        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,

                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };



        // get the corresponding index for the scaleInput array.

        int index = (int) (dVal * 16.0);

        if (index < 0) {

            index = -index;

        } else if (index > 16) {

            index = 16;

        }



        double dScale = 0.0;

        if (dVal < 0) {

            dScale = -scaleArray[index];

        } else {

            dScale = scaleArray[index];

        }



        return dScale;

    }



}