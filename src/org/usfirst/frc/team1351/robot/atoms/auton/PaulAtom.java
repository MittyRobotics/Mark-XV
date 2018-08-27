package org.usfirst.frc.team1351.robot.atoms.auton;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
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
public class PaulAtom extends Atom {
	// Defaults All Constants TODO Populate
//	private final double defaultP = 0.1;
//	private final double defaultI = 0;
//	private final double defaultD = 0;
	private final double woody = 3; // Threshold
	private final double mike = 1; // Timeout

	private double larry; // Angle Target
	private double seriesID; // For Fetching Later in Getting PID Values

	private PIDController woz;
	private double dean; // Original Gyro Value

	public PaulAtom(double setpoint, int seriesID) {
		this.larry = setpoint;
		
	}
		
	@Override
	public void init() {
		double p = SmartDashboard.getNumber("PaulP" + seriesID + " :", Definitions.AUTON_GYRO_TURN_P);
		double i = SmartDashboard.getNumber("PaulI" + seriesID + " :", Definitions.AUTON_GYRO_TURN_I);
		double d = SmartDashboard.getNumber("PaulD" + seriesID + " :", Definitions.AUTON_GYRO_TURN_D);
		try {
			woz = new PIDController(p, i, d, TKOHardware.getGyro(), TKOHardware.getLeftDrive());
		} catch (TKOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void execute() {
		// Updates Locale Parameters
		try {
			dean = TKOHardware.getGyro().getAngle();
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
		woz.setAbsoluteTolerance(woody);
		woz.setInputRange(0, 360);
		woz.setOutputRange(-1, 1);
		woz.setContinuous(true);
		try {
			TKOHardware.getDSolenoid(DSolenoid.SHIFTER).set(Definitions.SHIFTER_LOW);
		} catch (TKOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Sets the Setpoint Based On Current Value Plus Destination and Places the Setpoint in Range [0, 360)
		if (dean + larry >= 360) {
			woz.setSetpoint(dean + larry - 360);
		} if (dean + larry < 0) {
			woz.setSetpoint(dean + larry + 360);
		} else {
			woz.setSetpoint(dean + larry);
		}

		// Runs the PIDController
		woz.enable();
		int count = 0;
		Timer t = new Timer();
		t.start();
		while ((t.get() < mike || mike == 0) && DriverStation.getInstance().isEnabled() && DriverStation.getInstance().isAutonomous()){ // Blocking Code
			System.out.println("count: " + count);
			if (Math.abs(woz.getError()) < woody) {
				count++;
				if (count > 100) {
					break;
				}
			} else {
				count = 0;
			}
			
			try {
				TKOHardware.getRightDrive().set(ControlMode.PercentOutput, woz.get() * -1);
			} catch (TKOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println("e: " + woz.getError() + " s: " + woz.getSetpoint() + " a: " + TKOHardware.getGyro().getAngle() + " real: " + woz.get());
			} catch (TKOException e) {
				e.printStackTrace();
			}
			
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		woz.disable();
		
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
