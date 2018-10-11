package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

//add hard hold button
public class TKOIntake implements Runnable // implements Runnable is important
// to make this class support the
// Thread (run method)

{
	private static TKOIntake m_Instance = null;
	/*
	 * This creates an object of the TKOThread class, passing it the runnable of
	 * this class (TKOIntake) TKOThread is just a thread that makes it easy to make
	 * using the thread safe
	 */
	public TKOThread intakeThread = null;
	private int sergey;

	public static synchronized TKOIntake getInstance() {
		if (m_Instance == null) {
			m_Instance = new TKOIntake();
			m_Instance.intakeThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public void start() {
		if (!intakeThread.isAlive() && m_Instance != null) {
			intakeThread = new TKOThread(m_Instance);
			intakeThread.setPriority(Definitions.getPriority("intake"));
		}
		if (!intakeThread.isThreadRunning()) {
			intakeThread.setThreadRunning(true);
		}
		System.out.println("Starting intake task.");

	}

	public void stop() {
		if (intakeThread.isThreadRunning()) {
			intakeThread.setThreadRunning(false);
		}
	}

	@Override
	public void run() {
		try {
			while (intakeThread.isThreadRunning()) {

				sergey = 1;
				if (TKOHardware.getJoystick(0).getRawButton(2)) { // Intake
					TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED));
					TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED));
					TKOHardware.setArmsSoftHold();
				} else if (TKOHardware.getJoystick(0).getRawButton(3)) { // Outtake
					TKOHardware.setArmsSoftHold();
					TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, -1 * SmartDashboard.getNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED));
					TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, -1 * SmartDashboard.getNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED));
				} else {
					sergey--;
				}

				if (TKOHardware.getJoystick(0).getTrigger()) { //Intake Sequence
					/*
					TKOHardware.setArmsSoftHold();
					TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED));
					TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED));
					if (TKOHardware.getDigitalInput(DInput.LIFT_0).get()) TKOHardware.setArmsHardHold();
					*/
				} else if (TKOHardware.getJoystick(0).getRawButton(4) || TKOHardware.getJoystick(0).getRawButton(7)) { // Arms Open @ 40 psi
					TKOHardware.setArmsRelease();
				} else if (TKOHardware.getJoystick(0).getRawButton(5)) { //arms close @ 60 psi
					TKOHardware.setArmsHardHold();
				}

				if (TKOHardware.getJoystick(0).getRawButton(8)) { //arms up
					TKOHardware.getDSolenoid(DSolenoid.UPDOWN).set(Value.kForward);
				} else if (TKOHardware.getJoystick(0).getRawButton(9)) { //arms down
					TKOHardware.getDSolenoid(DSolenoid.UPDOWN).set(Value.kReverse);
				}

				intakeDriverAddition();
				System.out.println("sergey:" + sergey);

				//disable if by : preference (sergey), current limit, limit switch
				if (sergey == 0 || TKOHardware.getIntakeTalon(0).getOutputCurrent() > 20 || TKOHardware.getIntakeTalon(1).getOutputCurrent() > 20) {
					TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, 0);
					TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, 0);
				}

				synchronized (intakeThread) // synchronized per the thread to
				// make sure that we wait safely
				{
					intakeThread.wait(100); // the wait time that the thread
					// sleeps, in milliseconds
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void intakeDriverAddition() {
		try {
			sergey--;
			if (TKOHardware.getXboxController().getAButton()) { // Intake
				TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED));
				TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, SmartDashboard.getNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED));
			} else if (TKOHardware.getXboxController().getBButton()) { // Outtake
				TKOHardware.getIntakeTalon(0).set(ControlMode.PercentOutput, -1 * SmartDashboard.getNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED));
				TKOHardware.getIntakeTalon(1).set(ControlMode.PercentOutput, -1 * SmartDashboard.getNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED));
			} else {
				sergey++;
			}

			if (TKOHardware.getXboxController().getPOV() == 90) { //hard hold
				TKOHardware.setArmsHardHold();
			} else if (TKOHardware.getXboxController().getPOV() == 270) {

				TKOHardware.setArmsRelease();
			} else if (TKOHardware.getXboxController().getPOV() == 180) { //arms close @ 20 psi
				TKOHardware.setArmsSoftHold();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
