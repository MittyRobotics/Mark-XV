package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * This is replacement code for the Deprecated GyroTurnAtom
 * 
 * This code is not representative of half of the programming leads. Albert Drewke had no input when it came to the names that were used. ~Albert Drewke
 * @author LookLotsOfPeople
 * @since 3/24/2018
 * @version 1.0.0
 * @category Autonomous Atom
 */
public class AshAtom extends Atom {
	// Defaults All Constants TODO Populate
	private final double defaultP = 0.1;
	private final double defaultI = 0;
	private final double defaultD = 0;
	private final double threshold = 0; // Threshold
	private final double timeout = 0; // Timeout

	private double target; // Angle Target
	private double seriesID; // For Fetching Later in Getting PID Values

	private PIDController control;
	private double position; // Original Gyro Value

	public AshAtom(double setpoint, int seriesID) {
		this.target = setpoint;
		SmartDashboard.putNumber("AshP" + seriesID + " :", defaultP);
		SmartDashboard.putNumber("AshI" + seriesID + " :", defaultI);
		SmartDashboard.putNumber("AshD" + seriesID + " :", defaultD);
	}
	
	
	@Override
	public void init() {
		double p = SmartDashboard.getNumber("AshP" + seriesID + " :", defaultP);
		double i = SmartDashboard.getNumber("AshI" + seriesID + " :", defaultI);
		double d = SmartDashboard.getNumber("AshD" + seriesID + " :", defaultD);
		try {
			control = new PIDController(p, i, d, null, TKOHardware.getLeftDrive());
		} catch (TKOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void execute() {
		// Updates Locale Parameters
		try {
			position = TKOHardware.getGyro().getAngle();
		} catch (TKOException e) {
			e.printStackTrace();
		}
		
		// Configure the Talons
		try {
			TKOHardware.getRightDrive().set(ControlMode.Follower, TKOHardware.getLeftDrive().getDeviceID());
//			TKOHardware.getRightDrive().setInverted(TKOHardware.getRightDrive().getInverted()); // TODO Check Validity
		} catch (TKOException e) {
			e.printStackTrace();
		}
		
		// Sets Up the PIDController
		control.setAbsoluteTolerance(threshold);
		control.setInputRange(0, 360);
		control.setOutputRange(0, 0.2);
		control.setContinuous(true);

		// Sets the Setpoint Based On Current Value Plus Destination and Places the Setpoint in Range [0, 360)
		if (position + target >= 360) {
			control.setSetpoint(position + target - 360);
		} else {
			control.setSetpoint(position + target);
		}

		// Runs the PIDController
		control.enable();
		int count = 0;
		Timer t = new Timer();
		while (!(t.get() >= timeout && t.get() != 0) && DriverStation.getInstance().isEnabled()){ // Blocking Code
			if (Math.abs(control.getError()) < threshold) {
				count++;
				if (count > 1000) {
					break;
				}
			} else {
				count = 0;
			}
			
			try {
				TKOHardware.getRightDrive().set(ControlMode.PercentOutput, control.get() * -1);
			} catch (TKOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println(control.getError() + " " + control.getSetpoint() + " " + TKOHardware.getGyro().getAngle());
			} catch (TKOException e) {
				e.printStackTrace();
			}
		}
		control.disable();
		
		// Resets Talons
		try {
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
//			TKOHardware.getRightDrive().setInverted(TKOHardware.getRightDrive().getInverted()); // TODO Check Validity
		} catch (TKOException e) {
			e.printStackTrace();
		}
	}
}
