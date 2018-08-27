package org.usfirst.frc.team1351.robot.evom;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import org.usfirst.frc.team1351.robot.Definitions;

//To make it work, change back to RobotgetSelectedSensorPosition(0));

public class RobotLiftTestREAL extends SampleRobot {
	TalonSRX talon1, talon2, talon3, talon4;
	XboxController controller;
	DigitalInput limit1, limit2;
	Joystick stick1;
	JoystickButton button8, button9;
	double p = 0.05;
	double i = 0.0001;
	double d = 0.08;
	public RobotLiftTestREAL() {
		SmartDashboard.putNumber("Lift P: ", p);
		SmartDashboard.putNumber("Lift P: ", i);
		SmartDashboard.putNumber("Lift P: ", d);
		p = SmartDashboard.getNumber("Lift P: ", p);
		i = SmartDashboard.getNumber("Lift I: ", i);
		d = SmartDashboard.getNumber("Lift D: ", d);
//		talon1.configClosedloopRamp(1, 1000);
	}
	
	public void robotInit() {
		talon1 = new TalonSRX(0);
		talon2 = new TalonSRX(1);
		controller = new XboxController(0);
		limit1= new DigitalInput(0);
		limit2= new DigitalInput(1);
		stick1 = new Joystick(0);
		talon1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 1000);
		button8= new JoystickButton(stick1, 8);
		button9= new JoystickButton(stick1, 9);
		talon2.set(ControlMode.Follower, talon1.getDeviceID());
		talon1.config_kP(0, p, 1000);
		talon1.config_kI(0, i, 1000);
		talon1.config_kD(0, d, 1000);
		double ticksPerInch=3915.0/32.0;
	}

	public void autonomous() {
		while(true){
		}
	}

	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			if(button9.get()) {
				pid(3915/2, 200, 2);
			}
			setEncoder();
			manualLift();
			
//			System.out.println("Current: " + talon1.getOutputCurrent());
//			System.out.println("Voltage: " + talon1.getMotorOutputVoltage());
		}	
	}
	public void setEncoder() {
		if(button8.get()){
			while(!limit2.get()){
				talon1.set(ControlMode.PercentOutput, -0.2);
//				talon2.set(ControlMode.PercentOutput, -0.2);
				System.out.println(talon1.getSelectedSensorPosition(0));
			}
			talon1.set(ControlMode.PercentOutput, 0);
//			talon2.set(ControlMode.PercentOutput, 0);
			Timer.delay(0.5);
			talon1.setSelectedSensorPosition(0, 0, 1000);
			System.out.println(talon1.getSelectedSensorPosition((0)));
			Timer.delay(0.5);
			talon1.set(ControlMode.PercentOutput, 0.2);
//			talon2.set(ControlMode.PercentOutput, 0.2);
			Timer.delay(0.5);
			talon1.set(ControlMode.PercentOutput, 0);
//			talon2.set(ControlMode.PercentOutput, 0);
			
		}
		System.out.println(talon1.getSelectedSensorPosition((0)));
	}
	public void holdStuff(){
		System.out.println(talon1.getSelectedSensorPosition(0));
		while(!limit2.get()){
			talon1.set(ControlMode.PercentOutput, -0.2);
//			talon2.set(ControlMode.PercentOutput, -0.2);
			System.out.println(talon1.getSelectedSensorPosition(0));
		}
		while(!limit1.get()){
			talon1.set(ControlMode.PercentOutput, 0.2);
//			talon2.set(ControlMode.PercentOutput, 0.2);
		}
	}
	public void manualLift(){
		if (controller.getY(Hand.kLeft) < -0.05 || controller.getY(Hand.kLeft) > 0.05) {
			double yValue = controller.getY(Hand.kLeft);
			talon1.set(ControlMode.PercentOutput, -1 * yValue);
		} else {
			talon1.set(ControlMode.PercentOutput, 0);
		}

//		if (controller.getY(Hand.kRight) < -0.05 || controller.getY(Hand.kRight) > 0.05) {
//			double yValue = controller.getY(Hand.kRight);
//			talon2.set(ControlMode.PercentOutput, yValue);
//		} else {
//			talon2.set(ControlMode.PercentOutput, 0);
//		}
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(limit1.get() || limit2.get()) {
			while(limit1.get()|| limit2.get()) {
				talon1.set(ControlMode.PercentOutput, 0);
//				talon2.set(ControlMode.PercentOutput, 0);
				if (limit1.get()) {
					
					talon1.set(ControlMode.PercentOutput, -0.2);
//					talon2.set(ControlMode.PercentOutput, -0.2);
					Timer.delay(0.5);
					talon1.set(ControlMode.PercentOutput, 0);
//					talon2.set(ControlMode.PercentOutput, 0);
					
				}
				if (limit2.get()) {
					
					talon1.set(ControlMode.PercentOutput, 0.2);
//					talon2.set(ControlMode.PercentOutput, 0.2);
					Timer.delay(0.5);
					talon1.set(ControlMode.PercentOutput, 0);
//					talon2.set(ControlMode.PercentOutput, 0);
					
				}
			}
		}
		System.out.println(talon1.getSelectedSensorPosition(0));
	}
	
	public void pid(double setpoint, double threshold, double incrementer) {
		System.out.println("entered pid loop");
		double position = talon1.getSelectedSensorPosition(0);
		talon1.setIntegralAccumulator(0, 0, 1000);
		talon1.set(ControlMode.Position, 0);
//		while (setpoint > position) {
//			System.out.println("entered while loop for setpoint > position");
//	 		System.out.println(setpoint);
//	 		talon1.set(ControlMode.Position, talon1.getClosedLoopTarget(0) + incrementer);
//		 	position = talon1.getSelectedSensorPosition(0);
//		 	hitLimitSwitch();
//		 	Timer.delay(0.8);
//	 	 }
//	 	while (setpoint < position) {
//	 		System.out.println("entered while loop for setpoint < position");
//	 		System.out.println(setpoint);
//	  	  talon1.set(ControlMode.Position, talon1.getClosedLoopTarget(0) - incrementer);
//	  	  position = talon1.getSelectedSensorPosition(0);
//	  	  hitLimitSwitch();
//	  	  Timer.delay(0.8);
//	 	}
		while (Math.abs(setpoint- position) > threshold) {
			System.out.println("entered while loop for threshold < position");
			System.out.println(talon1.getClosedLoopError(0));
			talon1.set(ControlMode.Position, setpoint);
			hitLimitSwitch();
			Timer.delay(0.8);
		 }
		if (Math.abs(setpoint-position) <= threshold) {
			
			talon1.set(ControlMode.PercentOutput, 0);
			hitLimitSwitch();
			
		}
		System.out.println("reached end of pid loop");
		
		
		}
	public void hitLimitSwitch() {
		if(limit1.get() || limit2.get()) {
			while(limit1.get()|| limit2.get()) {
				talon1.set(ControlMode.PercentOutput, 0);
//				talon2.set(ControlMode.PercentOutput, 0);
				if (limit1.get()) {
					
					talon1.set(ControlMode.PercentOutput, -0.2);
//					talon2.set(ControlMode.PercentOutput, -0.2);
					Timer.delay(0.5);
					talon1.set(ControlMode.PercentOutput, 0);
//					talon2.set(ControlMode.PercentOutput, 0);
					
				}
				if (limit2.get()) {
					
					talon1.set(ControlMode.PercentOutput, 0.2);
//					talon2.set(ControlMode.PercentOutput, 0.2);
					Timer.delay(0.5);
					talon1.set(ControlMode.PercentOutput, 0);
//					talon2.set(ControlMode.PercentOutput, 0);
					
				}
			}
		}
	}
	
	public void test() {
		
	}
}