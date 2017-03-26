Written 11-4-16

If the date is significantly after that, don't expect anything mentioned here to necessarily exist, let alone work the same.


Here is the link to the entire project: https://www.dropbox.com/sh/fddmvozxgrgd96o/AACs8byQE3TcD7cX5zEKkxn7a?dl=0

Be sure to download the entire ftc_app-master (4) folder.

To open it properly, you need to open the ftc_app-master folder inside the ftc_app-master (4) folder. You also,need to make sure you have the andriod kitKat SDK installed (API Level 19), as well as a few other tools, but andriod studio should guide you through installing them. 

When using the programs, the motors must be mapped perfectly in the configuration part of the app.

For operation of the Basic drive code program, the motors must be labeled exactly "leftRear","rightRear","leftFront", and "rightFront". the left drive is controlled by the y-axis of the left stick, and the right side is controlled by the y-axis of the right stick.

For operation of the Basic drive code and intake program, make a new config with he same motors as above, but with an additional motor named "intake". intake is controlled by the left and right bumpers.

For operation of just the flywheel, use either the FlywheelTesting or MotorTest programs, with a single motor named "motor". motor is controlled by the right trigger.

For operation of a double flywheel (if you have/will build it), use FlywheelTestingDual, with two motors named "motor1" and "motor2". motor1 is controlled by the right trigger. motor2 is controlled by the left trigger.

Since I do not know how any of the motors will be set up, I have not reversed any of them. This means it may be necessary to either reverse the polarity of the wire that plugs from the motor into the motor controller, or to set the motor's direction to reverse in the program.

Hopefully it won't crash...