//Last edited by Tiina Otala
//2/20/18

package org.usfirst.frc.team1351.robot.atoms.auton;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

/**
 * Drive Atom class is the PID drive code for Auton Mode.
 *
 * @author 1351_Programming
 */
public class DriveAtom extends Atom {
	double distance;
	double p, i, d;
	double threshold;
	double wait;
	double incrementer;

	/**
	 * Converts the distance to ticks and sets the delay;
	 *
	 * @param distance - Distance in inches
	 * @param setting  - Gear setting (0 - low; 1 - high)
	 * @param wait     - Delay to start atom
	 */
	public DriveAtom(double distance, int setting, double wait) {
		p = SmartDashboard.getNumber("Drive P: ", 0.f); //make definitions?
		i = SmartDashboard.getNumber("Drive I:  ", 0.f);
		d = SmartDashboard.getNumber("Drive D: ", 0.f);

		if (setting == 0) {
			this.distance = distance * Definitions.TICKS_PER_INCH_LOW;
			this.threshold = Definitions.AUTON_DRIVE_THRESHOLD_LOW;
			this.incrementer = Definitions.AUTON_DRIVE_INCREMENTER_LOW;
			try {
				TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_LOW);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		} else {
			this.distance = distance * Definitions.TICKS_PER_INCH_HIGH;
			this.threshold = Definitions.AUTON_DRIVE_THRESHOLD_HIGH;
			this.incrementer = Definitions.AUTON_DRIVE_INCREMENTER_HIGH;
			try {
				TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_HIGH);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		}


		this.wait = wait;
	}

	/**
	 * @param distance - Distance in inches
	 * @param setting  - Gear setting ( 0 - low; 1 - high)
	 */
	public DriveAtom(double distance, int setting) {
		p = SmartDashboard.getNumber("Drive P: ", 0.f);
		i = SmartDashboard.getNumber("Drive I:  ", 0.f);
		d = SmartDashboard.getNumber("Drive D: ", 0.f);

		if (setting == 0) {
			this.distance = distance * Definitions.TICKS_PER_INCH_LOW;
			this.threshold = Definitions.AUTON_DRIVE_THRESHOLD_LOW;
			this.incrementer = Definitions.AUTON_DRIVE_INCREMENTER_LOW;
			try {
				TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_LOW);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		} else {
			this.distance = distance * Definitions.TICKS_PER_INCH_HIGH;
			this.threshold = Definitions.AUTON_DRIVE_THRESHOLD_HIGH;
			this.incrementer = Definitions.AUTON_DRIVE_INCREMENTER_HIGH;
			try {
				TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_HIGH);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		}

		this.wait = 0;
	}

	public void init() {
		try {
			TKOHardware.configDriveTalons(p, i, d, ControlMode.Position); //ALWAYS CHECK THIS FIRST IF THINGS ARE GOING WRONG
			TKOHardware.autonInitDriveAtom(); //"safe" - clears iaccum, sets to zero, and configs closed loop ramp
		} catch (TKOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Drive atom initialization failed!");
		}
		System.out.println("Drive atom initialized.");
	}

	@Override
	public void execute() {
		System.out.println("Executing drive atom...");
		Timer.delay(wait); //wait for as long as designated

		Timer t = new Timer();
		t.reset();
		t.start();

		try {
			//more init stuff
			//TODO: automate/clean up
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);

			//GET AND SET PID VALUES
			double P = SmartDashboard.getNumber("coolP", Definitions.AUTON_DRIVE_P);
			double I = SmartDashboard.getNumber("coolI", Definitions.AUTON_DRIVE_I);
			double D = SmartDashboard.getNumber("coolD", Definitions.AUTON_DRIVE_D);
			TKOHardware.configDriveTalons(P, I, D, ControlMode.Position);

			//TALON SETTINGS
			TKOHardware.getLeftDrive().configForwardSoftLimitEnable(false, 0);
			TKOHardware.getRightDrive().configForwardSoftLimitEnable(false, 0);
			TKOHardware.getLeftDrive().configClosedloopRamp(0.002, 0);
			TKOHardware.getRightDrive().configClosedloopRamp(0.002, 0);

			//TALON INVERTS
			TKOHardware.getLeftDrive().setSensorPhase(false); //sensor + inverted have to align or will grind/brownout ... if that was true then why would they have separate options for setting them!!!
			TKOHardware.getRightDrive().setSensorPhase(false);
			TKOHardware.getDriveTalon(0).setInverted(true);
			TKOHardware.getDriveTalon(1).setInverted(true);
			TKOHardware.getDriveTalon(2).setInverted(false);
			TKOHardware.getDriveTalon(3).setInverted(false);

			System.out.println("Distance: " + distance);
			try {
				Thread.sleep(500); //TODO: see if delay is a problem
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}


			//SETTING POSITION


			//NORMAL SETPOINT
			TKOHardware.getLeftDrive().set(ControlMode.Position, distance + TKOHardware.getLeftDrive().getSelectedSensorPosition(0));
			TKOHardware.getRightDrive().set(ControlMode.Position, distance + TKOHardware.getRightDrive().getSelectedSensorPosition(0));
			
			
			/* For easy removal
			//RAMPINGGGGGG
			//incrementing setpoint to make safer w/o current spikes
			//be careful - remove if necessary
			TKOHardware.getLeftDrive().setSelectedSensorPosition(0, 0, 0); //reset encoder
			TKOHardware.getRightDrive().setSelectedSensorPosition(0, 0, 0);
			if (distance < 0) { //if neg setpoint
				while (TKOHardware.getLeftDrive().getClosedLoopTarget(0) > distance && TKOHardware.getRightDrive().getClosedLoopTarget(0) > distance) {
					TKOHardware.getLeftDrive().set(ControlMode.Position, TKOHardware.getLeftDrive().getClosedLoopTarget(0) - incrementer);
					TKOHardware.getRightDrive().set(ControlMode.Position, TKOHardware.getRightDrive().getClosedLoopTarget(0) - incrementer);
					System.out.println("Ramping Neg - Left: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0) + " Right: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0));
				}
				System.out.println("RAMPING NEGATIVE FINISHED");
			} else if (distance > 0) { //if pos setpoint
				while (TKOHardware.getLeftDrive().getClosedLoopTarget(0) < distance && TKOHardware.getRightDrive().getClosedLoopTarget(0) < distance) {
					TKOHardware.getLeftDrive().set(ControlMode.Position, TKOHardware.getLeftDrive().getClosedLoopTarget(0) + incrementer);
					TKOHardware.getRightDrive().set(ControlMode.Position, TKOHardware.getRightDrive().getClosedLoopTarget(0) + incrementer);
					System.out.println("Ramping Pos - Left: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0) + " Right: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0));
				}
				System.out.println("RAMPING POSITIVE FINISHED");
			}
			TKOHardware.getLeftDrive().set(ControlMode.Position, distance);
			TKOHardware.getRightDrive().set(ControlMode.Position, distance);
			System.out.println("Final Setpoints - Left: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0) + " Right: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0));
			 */


			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
//			TKOHardware.getLeftDrive().configForwardSoftLimitThreshold(TKOHardware.getLeftDrive().getClosedLoopTarget(0), 0);
//			TKOHardware.getRightDrive().configForwardSoftLimitThreshold(TKOHardware.getRightDrive().getClosedLoopTarget(0), 0);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}


			//PID LOOP

			//run loop while enabled and outside of threshold
			while (DriverStation.getInstance().isEnabled()
					&& DriverStation.getInstance().isAutonomous()
					&& (Math.abs(TKOHardware.getLeftDrive().getClosedLoopError(0)) > threshold
					|| Math.abs(TKOHardware.getRightDrive().getClosedLoopError(0)) > threshold)) {
				//checking for timeout in PID loop. current timeout: 20 seconds for testing
				System.out.println("Current L: " + TKOHardware.getLeftDrive().getOutputCurrent() + " Current R: " + TKOHardware.getRightDrive().getOutputCurrent());
				if (t.get() > 15) {
					System.out.println("ERROR: Timeout in drive atom!");
					break;
				}
				//print statements
				System.out.println("Setpoint L: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0) + "\t Setpoint R: " + TKOHardware.getRightDrive().getClosedLoopTarget(0));
				System.out.println("Encoder L: " + TKOHardware.getLeftDrive().getSelectedSensorPosition(0) + "\t Encoder R: " + TKOHardware.getRightDrive().getSelectedSensorPosition(0));
				System.out.println("Error L: " + TKOHardware.getLeftDrive().getClosedLoopError(0) + "\t Error R: " + TKOHardware.getRightDrive().getClosedLoopError(0));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
			/*
			 * Re-enable if using Soft Limits. Ensures they won't interfere with later actions.
			TKOHardware.getLeftDrive().configForwardSoftLimitEnable(false, 0);
			TKOHardware.getRightDrive().configForwardSoftLimitEnable(false, 0);
			*/
			System.out.println("Position at end of atom - Left: " + TKOHardware.getLeftDrive().getSelectedSensorPosition(0) + "\t Right: " + TKOHardware.getRightDrive().getClosedLoopError(0));
		} catch (TKOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Done executing drive atom.");
	}
}