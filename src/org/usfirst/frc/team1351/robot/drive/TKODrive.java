package org.usfirst.frc.team1351.robot.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

public class TKODrive implements Runnable {
	private static TKODrive m_Instance = null;
	public TKOThread driveThread = null;

	public static synchronized TKODrive getInstance() {
		if (m_Instance == null) {
			m_Instance = new TKODrive();
			m_Instance.driveThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start() {
		if (!driveThread.isAlive() && m_Instance != null) {
			driveThread = new TKOThread(m_Instance);
			driveThread.setPriority(Definitions.getPriority("drive"));
		}
		if (!driveThread.isThreadRunning()) {
			driveThread.setThreadRunning(true);
		}
		System.out.println("Starting drive task.");
	}

	public void stop() {
		if (driveThread.isThreadRunning()) {
			driveThread.setThreadRunning(false);
		}
	}


	public void run() {
		try {
			while (driveThread.isThreadRunning()) {
				//Tank Drive
				//Left Side Tank Drive
				if (TKOHardware.getXboxController().getY(GenericHID.Hand.kLeft) < -0.05 || TKOHardware.getXboxController().getY(GenericHID.Hand.kLeft) > 0.05) {
					double yValue = TKOHardware.getXboxController().getY(GenericHID.Hand.kLeft) * (1 - TKOHardware.getXboxController().getTriggerAxis(Hand.kLeft));
					TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, -1 * yValue);
				} else {
					TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
				}
				//Right Side Tank Drive
				if (TKOHardware.getXboxController().getY(GenericHID.Hand.kRight) < -0.05 || TKOHardware.getXboxController().getY(GenericHID.Hand.kRight) > 0.05) {
					double yValue = TKOHardware.getXboxController().getY(GenericHID.Hand.kRight) * (1 - TKOHardware.getXboxController().getTriggerAxis(Hand.kLeft));
					TKOHardware.getRightDrive().set(ControlMode.PercentOutput, -1 * yValue);
				} else {
					TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);

				}
				//Gear Shifting
				//Manual Gear Switch
				if (TKOHardware.getXboxController().getAButton()) {
					shiftUp();
				} else if (TKOHardware.getXboxController().getBButton()) {
					shiftDown();
				}
				//Manual Gear Hold : Will Override Switch Mode
				if (TKOHardware.getXboxController().getTriggerAxis(GenericHID.Hand.kRight) > 0) {
					shiftDown();
				} else {
					shiftUp();
				}

				//Ramping
				//Ramping Toggle On
				if (TKOHardware.getXboxController().getXButton()) {
					for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
						TKOHardware.getDriveTalon(i).configOpenloopRamp(0.25, 0);
					}
				}
				//Ramping Toggle Off
				if (TKOHardware.getXboxController().getYButton()) {
					for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
						TKOHardware.getDriveTalon(i).configOpenloopRamp(0, 0);
					}
				}
				//Throttle Thread
				synchronized (driveThread) // synchronized per the thread to
				// make sure that we wait safely
				{
					driveThread.wait(100); // the wait time that the thread
					// sleeps, in milliseconds
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void shiftDown() {
		try {
			TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_LOW);
		} catch (TKOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void shiftUp() {
		try {
			TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_HIGH);
		} catch (TKOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
