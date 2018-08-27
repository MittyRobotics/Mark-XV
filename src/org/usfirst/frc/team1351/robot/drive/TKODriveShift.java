package org.usfirst.frc.team1351.robot.drive;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TKODriveShift extends SampleRobot
{

	WPI_TalonSRX oneTalon, twoTalon, threeTalon, fourTalon;
	DoubleSolenoid shifter;
	Joystick stick, stick2;
	double y0, y1;
	boolean automatic = false;
	final int forwardChannel = 7;
	final int reverseChannel = 6;
	final Value upShift = Value.kForward;
	final Value downShift = Value.kReverse;
	static double deadzone = 0.05;

	@Override
	public void robotInit()
	{
		oneTalon = new WPI_TalonSRX(0);
		twoTalon = new WPI_TalonSRX(1);
		threeTalon = new WPI_TalonSRX(2);
		fourTalon = new WPI_TalonSRX(3);
		shifter = new DoubleSolenoid(forwardChannel, reverseChannel);
		stick = new Joystick(0);
		stick2 = new Joystick(1);
		twoTalon.set(ControlMode.Follower, oneTalon.getDeviceID());

		fourTalon.set(ControlMode.Follower, threeTalon.getDeviceID());
//
//		oneTalon.enable();
//		twoTalon.enable();
//		threeTalon.enable();
//		fourTalon.enable();

	}

	@Override
	public void autonomous()
	{
	}

	@Override
	public void operatorControl()
	{
		while (isOperatorControl() && isEnabled())
		{
			SmartDashboard.putBoolean("AUTOMATIC", automatic);
			SmartDashboard.putNumber("Speed", oneTalon.getSelectedSensorVelocity(0));

			if (stick.getY() > -deadzone && stick.getY() < deadzone)
			{
				y0 = 0;

			}
			else
			{
				y0 = stick.getY();
				// fourTalon.set(y0);
				// sets the motors to run at the percent that the left joystick
				// is pushed forward
			}
			threeTalon.set(y0);
			oneTalon.set(-y0);

			// if (stick2.getY() > -deadzone && stick2.getY() < deadzone) {
			// y1 = 0;
			// } else {
			// y1 = stick2.getY();
			// // twoTalon.set(-y1);
			// // sets the motors to run at the percent that the right joystick
			// // is pushed forward
			// }
			// oneTalon.set(-y1);

			if (stick.getRawButton(3))
			{
				if (automatic == false)
				{
					automatic = true;
				}
				else if (automatic == true)
				{
					automatic = false;
				}
			}
			if (automatic == false)
			{

				if (stick.getRawButton(1) && shifter.get() != Value.kForward)
				{
					shifter.set(Value.kForward);

				}
				if (stick.getRawButton(2) && shifter.get() != Value.kReverse)
				{
					shifter.set(Value.kReverse);
				}

			}
			else
			{
				if ((oneTalon.getSelectedSensorVelocity(0) >= 2400 || oneTalon.getSelectedSensorVelocity(0) <= -2400) && shifter.get() != Value.kForward)
				{
					shifter.set(Value.kForward);
				}
				if ((oneTalon.getSelectedSensorVelocity(0) <= 2000 && oneTalon.getSelectedSensorVelocity(0) >= -2000) && shifter.get() != Value.kReverse)
				{
					shifter.set(Value.kReverse);
				}
			}
			Timer.delay(0.005); // wait for a motor update time
			System.out.println(oneTalon.getSelectedSensorVelocity(0));
		}
		System.out.println("WTFFFFF");
	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test()
	{
	}
}