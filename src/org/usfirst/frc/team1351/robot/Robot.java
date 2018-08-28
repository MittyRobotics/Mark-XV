//Last edited by Tiina Otala
//1/9/2018

package org.usfirst.frc.team1351.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.atoms.auton.DriveAtom;
import org.usfirst.frc.team1351.robot.atoms.auton.PaulAtom;
import org.usfirst.frc.team1351.robot.drive.TKODrive;
import org.usfirst.frc.team1351.robot.evom.TKOIntake;
import org.usfirst.frc.team1351.robot.evom.TKOLift;
import org.usfirst.frc.team1351.robot.evom.TKOPneumatics;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;

public class Robot extends SampleRobot {
	private SendableChooser<Integer> autonChooser = new SendableChooser<>();
	private SendableChooser<Integer> objectiveChooser = new SendableChooser<>();
	private UsbCamera cube;

	@Override
	public void robotInit() {
		System.out.println("-----WELCOME TO MarkXV2018 : WALL-E -----");
		System.out.println("-----SYSTEM BOOT: " + Timer.getFPGATimestamp() + "-----");
		SmartDashboard.putNumber("Lift P: ", Definitions.LIFT_P);
		SmartDashboard.putNumber("Lift I: ", Definitions.LIFT_I);
		SmartDashboard.putNumber("Lift D: ", Definitions.LIFT_D);

		SmartDashboard.putNumber("Drive distance: ", 12);
		SmartDashboard.putNumber("Turn angle: ", 90);

		SmartDashboard.putNumber("Turn P: ", Definitions.AUTON_GYRO_TURN_P);
		SmartDashboard.putNumber("Turn I: ", Definitions.AUTON_GYRO_TURN_I);
		SmartDashboard.putNumber("Turn D: ", Definitions.AUTON_GYRO_TURN_D);

		SmartDashboard.putNumber("Drive P: ", Definitions.AUTON_DRIVE_P);
		SmartDashboard.putNumber("Drive I: ", Definitions.AUTON_DRIVE_I);
		SmartDashboard.putNumber("Drive D: ", Definitions.AUTON_DRIVE_D);

		SmartDashboard.putNumber("Left Intake Speed", Definitions.INTAKE_LEFT_SPEED);
		SmartDashboard.putNumber("Right Intake Speed", Definitions.INTAKE_RIGHT_SPEED);

		SmartDashboard.putNumber("coolP", Definitions.AUTON_DRIVE_P);
		SmartDashboard.putNumber("coolI", Definitions.AUTON_DRIVE_I);
		SmartDashboard.putNumber("coolD", Definitions.AUTON_DRIVE_D);

		SmartDashboard.putNumber("PaulP" + 0 + " :", Definitions.AUTON_GYRO_TURN_P);
		SmartDashboard.putNumber("PaulI" + 0 + " :", Definitions.AUTON_GYRO_TURN_I);
		SmartDashboard.putNumber("PaulD" + 0 + " :", Definitions.AUTON_GYRO_TURN_D);

		autonChooser.addDefault("Left", 0);
		autonChooser.addObject("Center", 1);
		autonChooser.addObject("Right", 2);
		SmartDashboard.putData("Auto modes", autonChooser);

		objectiveChooser.addDefault("Crossing line", 0);
		objectiveChooser.addObject("Switch", 1);
		objectiveChooser.addObject("Scale", 2);
		SmartDashboard.putData("Objective", objectiveChooser);

		SmartDashboard.putNumber("Wait Time: ", 0);
		TKOHardware.initObjects();

		//TODO: FIX BEFORE MATCH!!!
		cube = CameraServer.getInstance().startAutomaticCapture("cube", 0);
		cube.setResolution(360, 280);
		cube.setFPS(30);


		TKOLift.getInstance().begin();

		System.out.println("Initialization finished");
	}

	@Override
	public void autonomous() {
		/*
		DriveAtom a = new DriveAtom(120, 0, 0);
		a.init();
		a.execute();
		*/
		
		/*
		TKOLift.getInstance().setElon(48);
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DriveAtom a = new DriveAtom(108, 0, 0);
		a.init();
		a.execute();
		try
		{
			TKOHardware.setArmsRelease();
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		try {
			TKOHardware.getDSolenoid(DSolenoid.UPDOWN).set(Value.kReverse);
			TKOLift.getInstance().setElon(0);
			DriveAtom d = new DriveAtom(120, 0, 0);
			d.init();
			d.execute();
			TKOLift.getInstance().setElon(84);
			Thread.sleep(2000);
			PaulAtom a = new PaulAtom(-90, 0);
			a.init();
			a.execute();
			d = new DriveAtom(12, 0, 0);
			d.init();
			d.execute();
			TKOHardware.setArmsRelease();
			d = new DriveAtom(-24, 0, 0);
			d.init();
			d.execute();
			TKOLift.getInstance().setElon(5);
			a = new PaulAtom(90, 0);
			a.init();
			a.execute();
			d = new DriveAtom(-24, 0, 0);
			d.init();
			d.execute();
		} catch (TKOException | InterruptedException e) {
			e.printStackTrace();
		}

		/*
		String message = DriverStation.getInstance().getGameSpecificMessage().toUpperCase();
		DriveAtom d;
		PaulAtom p;
		
		switch (autonChooser.getSelected()) {
			case 0:
				switch (objectiveChooser.getSelected()) {
					case 0:
						d = new DriveAtom(120, 0, 0);
						d.init();
						d.execute();
						break;
				}
			case 1:
				
				break;
			case 2:
				switch (objectiveChooser.getSelected()) {
					case 0:
						d = new DriveAtom(120, 0, 0);
						d.init();
						d.execute();
						break;
				}
		}
		
		
		switch (objectiveChooser.getSelected()) {
			case 0:
				d = new DriveAtom(120, 0, 0);
				d.init();
				d.execute();
				switch (objectiveChooser.getSelected()) {
					case 0:
						break;
					case 1:
						break;
					case 2:
				}
				break;
			case 1:
				d = new DriveAtom(36, 0, 0);
				d.init();
				d.execute();
				switch (objectiveChooser.getSelected()) {
					case 0:
						break;
					case 1:
						break;
					case 2:
						
				}
				break;
			case 2:
				d = new DriveAtom(120, 0, 0);
				d.init();
				d.execute();
				switch (autonChooser.getSelected()) {
					case 0:
						switch (message.charAt(0)) {
						case 'R':
							break;
						case 'L':
							d = new DriveAtom(180, 0);
							d.init();
							d.execute();
							try {
								TKOLift.getInstance().setElon(84);
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							p = new PaulAtom(90, 0);
							p.init();
							p.execute();
							try {
								TKOHardware.setArmsRelease();
							} catch (TKOException e) {
								e.printStackTrace();
							}
							break;
					}

						break;
					case 1:
						break;
					case 2:
						switch (message.charAt(0)) {
							case 'L':
								break;
							case 'R':
								d = new DriveAtom(180, 0);
								d.init();
								d.execute();
								try {
									TKOLift.getInstance().setElon(84);
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								p = new PaulAtom(90, 0);
								p.init();
								p.execute();
								try {
									TKOHardware.setArmsRelease();
								} catch (TKOException e) {
									e.printStackTrace();
								}
								break;
					}

						break;
				}
				break;
		}
		*/

		//CHEATY FIX
		/*
		TKOLift.getInstance().setElon(48);
		try
		{
			Thread.sleep(200);
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0.5);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0.5);
			while ((TKOHardware.getLeftDrive().getOutputCurrent() < 15 && TKOHardware.getRightDrive().getOutputCurrent() < 15) && DriverStation.getInstance().isEnabled());
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
			if (DriverStation.getInstance().getGameSpecificMessage().toUpperCase().charAt(0) == 'R') {
				TKOHardware.setArmsRelease();
			}
		} catch (TKOException e) {
			e.printStackTrace();
		}
		*/


		//OLD AUTON
		/*		
//		String pos = "LLL";
		String pos = DriverStation.getInstance().getGameSpecificMessage(); //Contains the switch and scale alignments, L = left, R = right, all relative to our alliance wall
		pos = pos.toUpperCase();
		System.out.println("Enabling autonomous!");

		TKOLogger.getInstance().start();
		// TKODataReporting.getInstance().start();
		// TKOTalonSafety.getInstance().start();
		// TKOPneumatics.getInstance().start();

		Molecule molecule = new Molecule();
		molecule.clear();

		double distance = SmartDashboard.getNumber("Drive distance: ", 0);
		double angle = SmartDashboard.getNumber("Turn angle: ", 90);
		double wait = SmartDashboard.getNumber("Wait Time: ", 0);

		if (autonChooser.getSelected().equals(0) && objectiveChooser.getSelected().equals(0))
		{
			//starting at right and crossing line
			/*
			molecule.add(new LiftAtom(20));
			
			try {
				System.out.println("Left Position: " + TKOHardware.getLeftDrive().getSelectedSensorPosition(0) + "  Right Position: " + TKOHardware.getRightDrive().getSelectedSensorPosition(0));
			} catch (TKOException e) {
				
				e.printStackTrace();
			}
//			molecule.add(new DriveAtom(distance, 0, wait));
			molecule.add(new GyroTurnAtom(90));
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			molecule.add(new DriveAtom(distance, 0, wait));

			molecule.add(new GyroTurnAtom(90));
			*/
		//Doing literally nothing for 1st practice match.
		/*
		}
		else if (autonChooser.getSelected().equals(0) && objectiveChooser.getSelected().equals(1))
		{
			//starting at right and unloading into switch
			if (pos.charAt(0) == 'L')
			{
				molecule.add(new DriveAtom(18, 1, wait));
				molecule.add(new GyroTurnAtom(-63));
				molecule.add(new DriveAtom(186, 1));
				molecule.add(new GyroTurnAtom(63));
				molecule.add(new DriveAtom(9, 1));
				//molecule.add(new LiftAtom(1));
			}
			else
			{
				molecule.add(new DriveAtom(282, 1, wait));
				molecule.add(new GyroTurnAtom(-90));
				molecule.add(new DriveAtom(1, 1));
//				molecule.add(new LiftAtom(1));
			}
		}
		else if (autonChooser.getSelected().equals(0) && objectiveChooser.getSelected().equals(2))
		{
			//starting at right and unloading at scale
			if (pos.charAt(1) == 'L')
			{
				molecule.add(new DriveAtom(221, 1, wait));
				molecule.add(new GyroTurnAtom(-90));
				molecule.add(new DriveAtom(162, 1));
				molecule.add(new GyroTurnAtom(90));
				molecule.add(new DriveAtom(38, 1));
//				molecule.add(new LiftAtom(2));
			}
			else
			{
				molecule.add(new DriveAtom(282.5, 1, wait));
				molecule.add(new GyroTurnAtom(-90));
				molecule.add(new DriveAtom(1, 1));
//				molecule.add(new LiftAtom(2));
			}
		}
		else if (autonChooser.getSelected().equals(1) && objectiveChooser.getSelected().equals(0))
		{
			//starting at center and crossing line
			molecule.add(new DriveAtom(distance, 1, wait));
		}
		else if (autonChooser.getSelected().equals(1) && objectiveChooser.getSelected().equals(1))
		{
			//starting at center and unloading at switch
			if (pos.charAt(0) == 'L') 
			{
				molecule.add(new DriveAtom(12, 1, wait));
				molecule.add(new GyroTurnAtom(-30));
				molecule.add(new DriveAtom(115, 1));
				molecule.add(new GyroTurnAtom(30));
//				molecule.add(new LiftAtom(1));
			}
			else
			{
				molecule.add(new DriveAtom(12, 1, wait));
				molecule.add(new GyroTurnAtom(40));
				molecule.add(new DriveAtom(128, 1));
				molecule.add(new GyroTurnAtom(-40));
//				molecule.add(new LiftAtom(1));
			}
		}
		else if (autonChooser.getSelected().equals(1) && objectiveChooser.getSelected().equals(2))
		{
			//starting at center and unloading at scale
			System.out.println("Invalid combination, performing default");
			molecule.add(new DriveAtom(125, 1, wait));
		}
		else if (autonChooser.getSelected().equals(2) && objectiveChooser.getSelected().equals(0))
		{
			//starting at left and crossing line
			molecule.add(new DriveAtom(distance, 1, wait));
		}
		else if (autonChooser.getSelected().equals(2) && objectiveChooser.getSelected().equals(1))
		{
			//starting at left and unloading at switch
			if (pos.charAt(0) == 'L') 
			{
				molecule.add(new DriveAtom(141, 1, wait));
				molecule.add(new GyroTurnAtom(90));
				molecule.add(new DriveAtom(20, 1));
//				molecule.add(new LiftAtom(1));
			}
			else
			{
				molecule.add(new DriveAtom(1, 1, wait));
				molecule.add(new GyroTurnAtom(37));
				molecule.add(new DriveAtom(165, 1));
				molecule.add(new GyroTurnAtom(-37));
//				molecule.add(new LiftAtom(1));
			}
		}
		else if (autonChooser.getSelected().equals(2) && objectiveChooser.getSelected().equals(2))
		{
			//starting at left and unloading at scale
			if (pos.charAt(1) == 'L') 
			{
				molecule.add(new DriveAtom(282.5, 1, wait));
				molecule.add(new GyroTurnAtom(90));
				molecule.add(new DriveAtom(1, 1));
//				molecule.add(new LiftAtom(2));
			}
			else
			{
				molecule.add(new DriveAtom(221, 1, wait));
				molecule.add(new GyroTurnAtom(90));
				molecule.add(new DriveAtom(162, 1));
				molecule.add(new GyroTurnAtom(-90));
				molecule.add(new DriveAtom(38, 1));
//				molecule.add(new LiftAtom(2));
			}
		}
		else
		{
			System.out.println("Molecule empty");
		}

		System.out.println("Running molecule");
		molecule.initAndRun();
		System.out.println("Finished running molecule");
		
//		try
//		{
//			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
//			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
//		}
//		catch (TKOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}


		try
		{
			// TKOPneumatics.getInstance().stop();
			// TKOPneumatics.getInstance().pneuThread.join();
			TKOLogger.getInstance().stop();
			TKOLogger.getInstance().loggerThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void operatorControl() {
		/*
		try
		{
			TKOHardware.getLeftDrive().set(ControlMode.PercentOutput, 0);
			TKOHardware.getRightDrive().set(ControlMode.PercentOutput, 0);
		}
		catch (TKOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		TKODrive.getInstance().start();
		TKOIntake.getInstance().start();
		TKOLift.getInstance().tele();
		TKOPneumatics.getInstance().start();
		while (isOperatorControl() && isEnabled()) {
			Timer.delay(0.05); // wait for a motor update time
		}

		try {
			TKOIntake.getInstance().stop();
			TKOIntake.getInstance().intakeThread.join();
			TKODrive.getInstance().stop();
			TKODrive.getInstance().driveThread.join();
			TKOPneumatics.getInstance().stop();
			TKOPneumatics.getInstance().pneuThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// TKOIntake.getInstance().stop();
		// TKOWinch.getInstance().stop();
	}

	@Override
	public void test() {
		TKOLift.getInstance().setElon(84);
		DriveAtom d = new DriveAtom(135, 0, 0);
		d.init();
		d.execute();
		PaulAtom p = new PaulAtom(-90, 0);
		p.init();
		p.execute();
		try {
			TKOHardware.setArmsRelease();
		} catch (TKOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PaulAtom f = new PaulAtom(90, 0);
		f.init();
		f.execute();
		TKOLift.getInstance().setElon(48);
		DriveAtom h = new DriveAtom(-130, 0, 0);
		h.init();
		h.execute();
	}
}
