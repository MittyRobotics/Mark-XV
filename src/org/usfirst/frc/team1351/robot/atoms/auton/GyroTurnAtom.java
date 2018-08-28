package org.usfirst.frc.team1351.robot.atoms.auton;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.atoms.Atom;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

/* TODO
 * tune incrementer
 * fix timer
 * set PID values in TKOHardware.java
 */

public class GyroTurnAtom extends Atom {
	double angle;
	double p, i, d;

	/**
	 * @param angle - Angle in degrees
	 */
	public GyroTurnAtom(double angle) {
		p = SmartDashboard.getNumber("Turn P: ", Definitions.AUTON_GYRO_TURN_P);
		i = SmartDashboard.getNumber("Turn I: ", Definitions.AUTON_GYRO_TURN_I);
		d = SmartDashboard.getNumber("Turn D: ", Definitions.AUTON_GYRO_TURN_D);

		this.angle = angle;
	}

	public void init() {
		try {
			TKOHardware.autonInitTurnAtom();

		} catch (TKOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Gyro atom initialization failed!");
		}
		System.out.println("Gyro turn atom initialized");
	}

	@Override
	public void execute() {
		System.out.println("Executing gyro turn atom...");

		Timer t = new Timer();
		t.reset();
		t.start();
		try {
			System.out.println("Angle: " + angle);

			TKOHardware.getLeftPIDControl().setSetpoint(angle);
			TKOHardware.getRightPIDControl().setSetpoint(angle);
			System.out.println("Setpoint L: " + TKOHardware.getLeftPIDControl().getSetpoint());
			System.out.println("Setpoint R: " + TKOHardware.getRightPIDControl().getSetpoint());

			//run loop while enabled and outside of threshold
			while (DriverStation.getInstance().isEnabled()
					&& !TKOHardware.getLeftPIDControl().onTarget()
					&& !TKOHardware.getRightPIDControl().onTarget()) {
				TKOHardware.getLeftPIDControl().setSetpoint(angle);
				System.out.println("L: " + TKOHardware.getLeftPIDControl().getError());
				TKOHardware.getRightPIDControl().setSetpoint(angle);
				System.out.println("R: " + TKOHardware.getRightPIDControl().getError());
				//checking for timeout in PID loop. current timeout: 7.5 seconds.
				if (t.get() > 7.5) {
					System.out.println("ERROR: Timeout in Gyro atom!");
					break;
				}
				//print statements
				System.out.println("Angle: " + TKOHardware.getGyro().getAngle());
				Timer.delay(0.03); //delay has to be long enough that the while loop doesn't end early
			}
			TKOHardware.getLeftPIDControl().disable();
			TKOHardware.getRightPIDControl().disable();
			System.out.println("Angle at end of atom: " + TKOHardware.getGyro().getAngle());
			Timer.delay(0.005);
		} catch (TKOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Done executing gyro turn atom.");
	}
}
