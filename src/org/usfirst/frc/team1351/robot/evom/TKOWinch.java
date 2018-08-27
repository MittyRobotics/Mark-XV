package org.usfirst.frc.team1351.robot.evom;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

public class TKOWinch implements Runnable // implements Runnable is important to make this class support the Thread (run method)
{

	public TKOThread winchThread = null;
	private static TKOWinch m_Instance = null;
	
	TalonSRX talon4, talon8;
	Joystick joystick0;
	DoubleSolenoid ratchet;
	AnalogInput analog;
	DoubleSolenoid.Value kReverse;
	DoubleSolenoid.Value kForward;
	
	double winchCurrent;
	double incrementer = 0.01;
	double currentThreshold = 0.01;
	double distanceThreshold;
	double range;
	double matchTime;
	double rungThreshold;
	double distanceToRung;

	// Typical constructor made protected so that this class is only accessed statically, though that doesnt matter
	protected TKOWinch()
	{
		
	}

	public static synchronized TKOWinch getInstance()
	{
		if (m_Instance == null)
		{
			m_Instance = new TKOWinch();
			m_Instance.winchThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start()
	{
		if (!winchThread.isAlive() && m_Instance != null)
		{
			winchThread = new TKOThread(m_Instance);
			winchThread.setPriority(Definitions.getPriority("winch"));
		}
		if (!winchThread.isThreadRunning())
		{
			winchThread.setThreadRunning(true);
		}
		
		try {
			winchCurrent = TKOHardware.getWinchTalon().getOutputCurrent();
		} catch (TKOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The {@code stop} method disables the thread, simply by setting the {@code isThreadRunning} to false via {@code setThreadRunning} and
	 * waits for the method to stop running (on the next iteration of run).
	 */
	public void stop()
	{
		if (winchThread.isThreadRunning())
		{
			winchThread.setThreadRunning(false);
		}
	}

//	void autoWinchUp()
//	{
//		try
//		{
//			if (!TKOHardware.getJoystick(1).getRawButton(2) && TKOHardware.getWinchTalon().getOutputCurrent() > Definitions.WINCH_UNLADEN_CURRENT)
//			{
//				TKOHardware.getWinchTalon().set(ControlMode.Current, Definitions.WINCH_LADEN_FIXED_OUTPUT);
//			}
//			
//			else
//			{
//				TKOHardware.getWinchTalon().setNeutralMode(NeutralMode.Brake);
//				TKOHardware.getWinchTalon().set(ControlMode.Current, 0);
//			}
//		}
//		catch (TKOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * The run method is what the thread actually calls once. The continual running of the thread loop is done by the while loop, controlled
	 * by a safe boolean inside the TKOThread object. The wait is synchronized to make sure the thread safely sleeps.
	 */
	
	void hookOn() {
	//TKOHardware.getDSolenoid(?).set(Value.kForward); normally
	
	//TKOHardware.getDSolenoid(?).set(Value.kReverse); deploy
	
	}
	
	@Override
	public void run()
	{
		
//		double idealCurrent;
		
		try
		{
			while (winchThread.isThreadRunning())
			{
				while (DriverStation.getInstance().isEnabled()) {
//					ULTRASONIC
//					range = analog.getVoltage() / (5.0 / 512);
//					System.out.println("Range: " + range);
//					Timer.delay(0.05);	
					
//				if (Math.abs(range - distanceToRung) < rungThreshold && matchTime >= 120) {
//					
//					TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0.0);
//					TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0.0);
//					Timer.delay(0.5);	
//	
//				
			
				matchTime = DriverStation.getInstance().getMatchTime();
				
				if (joystick0.getRawButton(7) && matchTime >= 120) { //TODO: && time = 30 seconds left
					
					ratchet.set(kReverse);
					Timer.delay(.005);
					
				}
				
				if (joystick0.getRawButton(1)) {
					talon4.set(ControlMode.Current, 2.0);
					talon8.set(ControlMode.Current, 2.0);
					Timer.delay(.005);
				
				} else if (joystick0.getRawButton(2)) {
				
					talon4.set(ControlMode.Current, 10.0);
					talon8.set(ControlMode.Current, 10.0);
					System.out.println(talon4.getOutputCurrent());
					Timer.delay(.005);
				
				} else if (joystick0.getRawButton(3)) {
				
					talon4.set(ControlMode.Current, 15.0);
					talon8.set(ControlMode.Current, 15.0);
					System.out.println(talon4.getOutputCurrent());
					Timer.delay(.005);
				
				} else if (joystick0.getRawButton(4)) {
				
					talon4.set(ControlMode.Current, 20.0);
					talon8.set(ControlMode.Current, 20.0);
					System.out.println(talon4.getOutputCurrent());
					Timer.delay(.005);
				
				} else if (joystick0.getRawButton(5)) {
				
					talon4.set(ControlMode.Current, -2.0);
					talon8.set(ControlMode.Current, -2.0);
					Timer.delay(.01);
				
				} else if (joystick0.getRawButton(6)) {
						
					talon4.set(ControlMode.Current, -10.0);
					talon8.set(ControlMode.Current, -10.0);
					Timer.delay(.005);
			
				}	else if (joystick0.getRawButton(8)) {
					
					talon4.set(ControlMode.Current, -25.0);
					talon8.set(ControlMode.Current, -25.0);
					Timer.delay(.005);
					
				} else {
					
					talon4.set(ControlMode.Current, 0.0);
					talon8.set(ControlMode.Current, 0.0);

					Timer.delay(0.05);
				}
				

		}		
		
				
//				
//				PID
				
//				winchCurrent = TKOHardware.getWinchTalon().getOutputCurrent();
//				TKOHardware.getWinchTalon().set(ControlMode.Current, winchCurrent);
//				
//				if(winchCurrent >= 0.0) {
//					while(DriverStation.getInstance().isEnabled() && winchCurrent >= Definitions.WINCH_LADEN_FIXED_OUTPUT) { 
//						
//						winchCurrent = winchCurrent + incrementer; //should I put incrementer on smartdashboard??
//						
//						Timer.delay(0.001);
//					}
//					
//					while(DriverStation.getInstance().isEnabled() && winchCurrent < Definitions.WINCH_LADEN_FIXED_OUTPUT) {
//						
//						winchCurrent = winchCurrent - incrementer;
//						
//						Timer.delay(0.001);
//					}
//					
//					TKOHardware.getWinchTalon().set(ControlMode.Current, Definitions.WINCH_LADEN_FIXED_OUTPUT);
//				}
//				
//				// how to do threshhold?
//				while (DriverStation.getInstance().isEnabled()) {
//					if(Math.abs(Definitions.WINCH_LADEN_FIXED_OUTPUT - winchCurrent) > currentThreshold) {
//						
//						TKOHardware.getWinchTalon().set(ControlMode.Current, winchCurrent);
//						
//						
//					}

				
		
				
//				maintain tension + stop when ultrasonic + auto hook with button press + climb = profit???
//				
//				
//				System.out.printf("Current: %8.3f \t \n", TKOHardware.getWinchTalon().getOutputCurrent()); 
				
//				TKOHardware.getWinchTalon().set(ControlMode.Current, idealCurrent);
				
//				if (ultrasonic.getOutput<=0 || ultrasonic.getOutput>=0)
//				stopDrive(1500);
//			}
				
				//if (TKOHardware.getJoystick(1).getRawButton(?)){
				//hookOn();	
//				}
			
//				if (TKOHardware.getJoystick(1).getRawButton(?))	{		
			
//					TKOHardware.getWinchTalon().set(ControlMode.PercentOutput, Definitions.WINCH_LADEN_FIXED_OUTPUT);
//				
				
//				}
//				

				synchronized (winchThread) // synchronized per the thread to make sure that we wait safely
				{
					winchThread.wait(100); // the wait time that the thread sleeps, in milliseconds
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
