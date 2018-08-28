package org.usfirst.frc.team1351.robot.atoms.auton;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

/**
 * READ TODOs PLZ
 *
 * @author LookLotsOfPeople
 * @version 2018.3.27
 * @see DriveAtom
 */
public class RewrittenDriveAtom extends Atom {
	private final double timeout = 15;
	private final double threshold = 0;

	private int gear;
	private double distance;
	private double p = 0, i = 0, d = 0;

	public RewrittenDriveAtom(double distance, int gear) {
		this.distance = distance;
		this.gear = gear; // TODO REVERSE THE CALLING CODE, REAL ROBOT HAS REVERSED GEARS !!!
	}

	@Override
	public void init() {
		// LOL What's This For Again?
	}

	@Override
	public void execute() {
		System.out.println("Executing drive atom...");

		// Gets PID Values
		p = SmartDashboard.getNumber("coolP", p);
		i = SmartDashboard.getNumber("coolI", i);
		d = SmartDashboard.getNumber("coolD", d);
		System.out.println("P: " + p + "\tI: " + i + "\tD: " + d);

		// Init Talons
		try {
			TKOHardware.getLeftDrive().config_kP(0, p, 0);
			TKOHardware.getLeftDrive().config_kI(0, i, 0);
			TKOHardware.getLeftDrive().config_kD(0, d, 0);
			TKOHardware.getRightDrive().config_kP(0, p, 0);
			TKOHardware.getRightDrive().config_kP(0, i, 0);
			TKOHardware.getRightDrive().config_kP(0, d, 0);

			TKOHardware.getLeftDrive().setSensorPhase(false);
			TKOHardware.getRightDrive().setSensorPhase(false);
			TKOHardware.getLeftDrive().setInverted(true);
			TKOHardware.getRightDrive().setInverted(false);

			TKOHardware.getLeftDrive().setSelectedSensorPosition(0, 0, 0);
			TKOHardware.getRightDrive().setSelectedSensorPosition(0, 0, 0);
			TKOHardware.getLeftDrive().configAllowableClosedloopError(0, 0, 0);
			TKOHardware.getRightDrive().configAllowableClosedloopError(0, 0, 0);

			TKOHardware.getLeftDrive().configForwardSoftLimitEnable(false, 0);
			TKOHardware.getRightDrive().configForwardSoftLimitEnable(false, 0);

		} catch (TKOException e) {
			e.printStackTrace();
		}

		// Set Gear and Talons
		if (gear == 0) {
			try {
				distance *= Definitions.TICKS_PER_INCH_LOW;
				TKOHardware.getDSolenoid(Definitions.DSolenoid.SHIFTER).set(Definitions.SHIFTER_LOW); // TODO Switch for Testing (I Believe This is Right for Real Robot)
				TKOHardware.getLeftDrive().set(ControlMode.Position, distance);
//				TKOHardware.getRightDrive().set(ControlMode.Position, distance);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		} else if (gear == 1) {
			try {
				distance *= Definitions.TICKS_PER_INCH_HIGH;
				TKOHardware.getDSolenoid(Definitions.DSolenoid.SHIFTER).set(Definitions.SHIFTER_HIGH); // TODO Switch for Testing (I Believe This is Right for Real Robot)
				TKOHardware.getLeftDrive().set(ControlMode.Position, distance);
//				TKOHardware.getRightDrive().set(ControlMode.Position, distance);
			} catch (TKOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Not a valid gear chosen long time ago..!");
		}

		// Creates a Timeout Timer
		Timer t = new Timer();
		t.start();

		// While Blocking
		int count = 0;
		while (t.get() < timeout && DriverStation.getInstance().isEnabled()) {
			try {
				try {
					System.out.println("Position ::: Left: " + TKOHardware.getLeftDrive().getSelectedSensorPosition(0) + "\tRight: " + TKOHardware.getRightDrive().getSelectedSensorPosition(0));
					System.out.println("Error    ::: Left: " + TKOHardware.getLeftDrive().getClosedLoopError(0) + "\tRight: " + TKOHardware.getRightDrive().getClosedLoopError(0));
					System.out.println("Setpoint ::: Left: " + TKOHardware.getLeftDrive().getClosedLoopTarget(0) + "\tRight: " + TKOHardware.getRightDrive().getClosedLoopTarget(0));
				} catch (TKOException e) {
					e.printStackTrace();
				}

				if (Math.abs(TKOHardware.getLeftDrive().getClosedLoopError(0)) < threshold && Math.abs(TKOHardware.getRightDrive().getClosedLoopError(0)) < threshold) {
					count++;
					if (count > 200) {
						System.out.println("In Threshold!");
						break;
					}
				} else {
					count = 0;
				}
				Thread.sleep(10);
			} catch (TKOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Turns Off Talons
		try {
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
		} catch (TKOException e) {
			e.printStackTrace();
		}

		System.out.println("Done executing drive atom.");
	}
}
