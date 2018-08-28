//Last edited by Tiina Otala
//1/9/2018

package org.usfirst.frc.team1351.robot.util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import edu.wpi.first.wpilibj.util.AllocationException;
import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DInput;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.logger.TKOLogger;

public class TKOHardware {
	/**
	 * The idea behind TKOHardware is to have one common class with all the objects we would need. The code is highly modular, as seen below
	 * where all the arrays are of variable size.
	 */
	private static XboxController xbox;
	private static Joystick joysticks[] = new Joystick[Definitions.NUM_JOYSTICKS];
	private static WPI_TalonSRX[] driveTalons = new WPI_TalonSRX[Definitions.NUM_DRIVE_TALONS];
	private static WPI_TalonSRX winchTalons[] = new WPI_TalonSRX[Definitions.NUM_WINCH_TALONS];
	private static WPI_TalonSRX liftTalons[] = new WPI_TalonSRX[Definitions.NUM_LIFT_TALONS];
	private static WPI_TalonSRX intakeTalons[] = new WPI_TalonSRX[Definitions.NUM_INTAKE_TALONS];
	private static DigitalInput digitalInputs[] = new DigitalInput[Definitions.NUM_DINPUTS];
	private static Compressor compressor;
	private static ADXRS450_Gyro gyro;
	private static PIDController pidControlRight, pidControlLeft;
	private static DoubleSolenoid shifter, upDown, arm60, arm40, ratchet;

	public TKOHardware() {
		xbox = null;
		for (int i = 0; i < Definitions.NUM_JOYSTICKS; i++) {
			joysticks[i] = null;
		}
		// After a follower talon is created, it should not be accessed (exception thrown)
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			driveTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_WINCH_TALONS; i++) {
			winchTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_LIFT_TALONS; i++) {
			liftTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_INTAKE_TALONS; i++) {
			intakeTalons[i] = null;
		}
		for (int i = 0; i < Definitions.NUM_DINPUTS; i++) {
			digitalInputs[i] = null;
		}
		compressor = null;
		gyro = null;
		pidControlRight = null;
		pidControlLeft = null;
		shifter = null;
		arm60 = null;
		arm40 = null;
		upDown = null;
		ratchet = null;

	}

	private static synchronized void initDriveTalons() {
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			if (driveTalons[i] == null) {
				try {
					driveTalons[i] = new WPI_TalonSRX(Definitions.DRIVE_TALON_ID[i]);
				} catch (AllocationException | CANMessageNotFoundException e) {
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
	}

	private static synchronized void initWinchTalons() {
		for (int i = 0; i < Definitions.NUM_WINCH_TALONS; i++) {
			if (winchTalons[i] == null) {
				try {
					winchTalons[i] = new WPI_TalonSRX(Definitions.WINCH_TALON_ID[i]);
				} catch (AllocationException | CANMessageNotFoundException e) {
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
	}

	private static synchronized void initLiftTalons() {
		for (int i = 0; i < Definitions.NUM_LIFT_TALONS; i++) {
			if (liftTalons[i] == null) {
				try {
					liftTalons[i] = new WPI_TalonSRX(Definitions.LIFT_TALON_ID[i]);
				} catch (AllocationException | CANMessageNotFoundException e) {
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
	}

	private static synchronized void initIntakeTalons() {
		for (int i = 0; i < Definitions.NUM_INTAKE_TALONS; i++) {
			if (intakeTalons[i] == null) {
				try {
					intakeTalons[i] = new WPI_TalonSRX(Definitions.INTAKE_TALON_ID[i]);
				} catch (AllocationException | CANMessageNotFoundException e) {
					e.printStackTrace();
					System.out.println("MOTOR CONTROLLER " + i + " NOT FOUND OR IN USE");
					TKOLogger.getInstance().addMessage("MOTOR CONTROLLER " + i + " CAN ERROR");
				}
			}
		}
	}

	private static synchronized void initPIDControllers() {
		try {
			pidControlRight = new PIDController(Definitions.AUTON_GYRO_TURN_P, Definitions.AUTON_GYRO_TURN_I, Definitions.AUTON_GYRO_TURN_D, gyro, TKOHardware.getRightDrive());
			pidControlLeft = new PIDController(Definitions.AUTON_GYRO_TURN_P, Definitions.AUTON_GYRO_TURN_I, Definitions.AUTON_GYRO_TURN_D, gyro, TKOHardware.getLeftDrive());
			pidControlRight.setInputRange(0, 360);
			pidControlLeft.setInputRange(0, 360);
			pidControlRight.setContinuous(true);
			pidControlLeft.setContinuous(true);
			pidControlRight.setAbsoluteTolerance(Definitions.AUTON_GYRO_THRESHOLD);
			pidControlLeft.setAbsoluteTolerance(Definitions.AUTON_GYRO_THRESHOLD);
			pidControlRight.enable();
			pidControlLeft.enable();
		} catch (TKOException e) {
			e.printStackTrace();
			System.out.println("ERROR: PID CONTROLLER INITIALIZATION FAILED");
			TKOLogger.getInstance().addMessage("PID CONTROLLER INITIALIZATION FAILED");
		}
	}

	public static synchronized void initObjects() {
//		destroyObjects();

		//controllers
		if (xbox == null)
			xbox = new XboxController(Definitions.JOYSTICK_ID[0]);
		for (int i = 0; i < Definitions.NUM_JOYSTICKS; i++) {
			if (joysticks[i] == null)
				joysticks[i] = new Joystick(Definitions.JOYSTICK_ID[i + 1]);
		}

		//pneumatics
		if (shifter == null)
			shifter = new DoubleSolenoid(Definitions.PCM_ID_MAIN, Definitions.SHIFTER_A, Definitions.SHIFTER_B);
		if (upDown == null)
			upDown = new DoubleSolenoid(Definitions.PCM_ID_MAIN, Definitions.UPDOWN_A, Definitions.UPDOWN_B);
		if (arm60 == null)
			arm60 = new DoubleSolenoid(Definitions.PCM_ID_MAIN, Definitions.ARM60_A, Definitions.ARM60_B);
		if (arm40 == null)
			arm40 = new DoubleSolenoid(Definitions.PCM_ID_MAIN, Definitions.ARM40_A, Definitions.ARM40_B);
		if (ratchet == null)
			ratchet = new DoubleSolenoid(Definitions.PCM_ID_ADD, Definitions.RATCHET_A, Definitions.RATCHET_B);
		if (compressor == null)
			compressor = new Compressor(Definitions.PCM_ID_MAIN);

		//sensors/etc.
		for (int i = 0; i < Definitions.NUM_DINPUTS; i++) {
			if (digitalInputs[i] == null) {
				try {
					digitalInputs[i] = new DigitalInput(Definitions.DINPUT_ID[i]);
				} catch (AllocationException e) {
					e.printStackTrace();
					TKOLogger.getInstance().addMessage("DIGITAL INPUT " + i + " NOT FOUND");
				}
			}
		}

		if (gyro == null) {
			gyro = new ADXRS450_Gyro(Definitions.GYRO_SPI_PORT);
			gyro.reset();
			System.out.println("Gyro initialized: " + Timer.getFPGATimestamp());
		}

		//talons
		initDriveTalons();
		initWinchTalons();
		initLiftTalons();
		initIntakeTalons();

		configDriveTalons(Definitions.DRIVE_P, Definitions.DRIVE_I, Definitions.DRIVE_D, Definitions.DRIVE_TALONS_NORMAL_CONTROL_MODE);
		configWinchTalons(Definitions.WINCH_P, Definitions.WINCH_I, Definitions.WINCH_D, Definitions.WINCH_TALONS_NORMAL_CONTROL_MODE);
		configLiftTalons(Definitions.LIFT_P, Definitions.LIFT_I, Definitions.LIFT_D, Definitions.LIFT_TALONS_NORMAL_CONTROL_MODE);
		configIntakeTalons(Definitions.INTAKE_TALONS_NORMAL_CONTROL_MODE);

//		initPIDControllers();

	}

	@Deprecated
	public static synchronized void destroyObjects() {
		//controllers
		if (xbox != null)
			xbox = null;
		for (int i = 1; i < Definitions.NUM_JOYSTICKS; i++) {
			if (joysticks[i] != null) {
				joysticks[i] = null;
			}
		}
		//talons
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			if (driveTalons[i] != null) {
				driveTalons[i].free();
				driveTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_WINCH_TALONS; i++) {
			if (winchTalons[i] != null) {
				winchTalons[i].free();
				winchTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_LIFT_TALONS; i++) {
			if (liftTalons[i] != null) {
				liftTalons[i].free();
				liftTalons[i] = null;
			}
		}
		for (int i = 0; i < Definitions.NUM_INTAKE_TALONS; i++) {
			if (intakeTalons[i] != null) {
				intakeTalons[i].free();
				intakeTalons[i] = null;
			}
		}
		//sensors/etc.
		for (int i = 0; i < Definitions.NUM_DINPUTS; i++) {
			if (digitalInputs[i] != null) {
				digitalInputs[i].free();
				digitalInputs[i] = null;
			}
		}
		if (gyro != null) {
			gyro.free();
			gyro = null;
		}
		if (pidControlRight != null) //TODO: Turn to an array lol
		{
			pidControlRight = null;
			pidControlLeft = null;
		}

		//pneumatics
		if (compressor != null) {
			compressor.free();
			compressor = null;
		}
		if (shifter != null) {
			shifter.free();
			shifter = null;
		}
		if (arm60 != null) {
			arm60.free();
			arm60 = null;
		}
		if (arm40 != null) {
			arm40.free();
			arm40 = null;
		}
		if (upDown != null) {
			upDown.free();
			upDown = null;
		}
		if (ratchet != null) {
			ratchet.free();
			ratchet = null;
		}
	}

	public static synchronized void configDriveTalons(double p, double I, double d, ControlMode mode) {
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			if (driveTalons[i] == null) {
				driveTalons[i] = new WPI_TalonSRX(Definitions.DRIVE_TALON_ID[i]);
			}
			if (driveTalons[i] != null) {
				if (i % 2 == 1) // if follower
				{
					driveTalons[i].set(ControlMode.Follower, driveTalons[i - 1].getDeviceID()); // set to follow the TalonSRX with id i - 1;
				} else
				// if not follower
				{
					driveTalons[i].configSelectedFeedbackSensor(Definitions.DRIVE_ENCODER_TYPE, 0, Definitions.DEFAULT_TIMEOUT);
					driveTalons[i].config_kP(0, p, Definitions.DEFAULT_TIMEOUT);
					driveTalons[i].config_kI(0, I, Definitions.DEFAULT_TIMEOUT);
					driveTalons[i].config_kD(0, d, Definitions.DEFAULT_TIMEOUT);
				}
				driveTalons[i].setNeutralMode(Definitions.DRIVE_NEUTRAL_MODE[i]);
				driveTalons[i].setInverted(Definitions.DRIVE_REVERSE_OUTPUT_MODE[i]);
				driveTalons[i].setSensorPhase(Definitions.DRIVE_REVERSE_SENSOR[i]);
				driveTalons[i].configOpenloopRamp(Definitions.DRIVE_RAMP, Definitions.DEFAULT_TIMEOUT);
				driveTalons[i].configForwardSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				driveTalons[i].configReverseSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
			}
		}
	}

	public static synchronized void configWinchTalons(double p, double I, double d, ControlMode mode) {
		for (int i = 0; i < Definitions.NUM_WINCH_TALONS; i++) {
			System.out.println(Definitions.NUM_WINCH_TALONS);
			winchTalons[i] = null;
			winchTalons[i] = new WPI_TalonSRX(Definitions.WINCH_TALON_ID[i]);
			if (winchTalons[i] != null) {

				if (i % 2 == 1) // if follower
				{
					winchTalons[i].set(ControlMode.Follower, winchTalons[i - 1].getDeviceID()); // set to follow the TalonSRX with id i - 1;
				} else
				// if not follower
				{
					winchTalons[i].set(mode, 0);
					winchTalons[i].configSelectedFeedbackSensor(Definitions.WINCH_ENCODER_TYPE, 0, Definitions.DEFAULT_TIMEOUT);
					winchTalons[i].config_kP(0, p, Definitions.DEFAULT_TIMEOUT);
					winchTalons[i].config_kI(0, i, Definitions.DEFAULT_TIMEOUT);
					winchTalons[i].config_kD(0, d, Definitions.DEFAULT_TIMEOUT);
				}
				winchTalons[i].setNeutralMode(Definitions.WINCH_NEUTRAL_MODE[i]);
				winchTalons[i].setInverted(Definitions.WINCH_REVERSE_OUTPUT_MODE[i]);
				winchTalons[i].setSensorPhase(Definitions.WINCH_REVERSE_SENSOR[i]);
				winchTalons[i].configOpenloopRamp(Definitions.DEFAULT_RAMP, Definitions.DEFAULT_TIMEOUT);
				winchTalons[i].configClosedloopRamp(Definitions.DEFAULT_RAMP, Definitions.DEFAULT_TIMEOUT);
				winchTalons[i].configForwardSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				winchTalons[i].configReverseSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
			}
		}
	}

	public static synchronized void configLiftTalons(double p, double I, double d, ControlMode mode) {
		for (int i = 0; i < Definitions.NUM_LIFT_TALONS; i++) {
			liftTalons[i] = null;
			liftTalons[i] = new WPI_TalonSRX(Definitions.LIFT_TALON_ID[i]);
			if (liftTalons[i] != null) {
				if (i % 2 == 1) // if follower
				{
					liftTalons[i].set(ControlMode.Follower, liftTalons[i - 1].getDeviceID()); // set to follow the TalonSRX with id i - 1;
				} else
				// if not follower
				{
					liftTalons[i].set(mode, 0);
					liftTalons[i].configSelectedFeedbackSensor(Definitions.LIFT_ENCODER_TYPE, 0, Definitions.DEFAULT_TIMEOUT);
					liftTalons[i].config_kP(0, p, Definitions.DEFAULT_TIMEOUT);
					liftTalons[i].config_kI(0, i, Definitions.DEFAULT_TIMEOUT);
					liftTalons[i].config_kD(0, d, Definitions.DEFAULT_TIMEOUT);
				}
				liftTalons[i].setNeutralMode(Definitions.LIFT_NEUTRAL_MODE[i]);
				liftTalons[i].setInverted(Definitions.LIFT_REVERSE_OUTPUT_MODE[i]);
				liftTalons[i].setSensorPhase(Definitions.LIFT_REVERSE_SENSOR[i]);
				liftTalons[i].configOpenloopRamp(Definitions.LIFT_RAMP, Definitions.DEFAULT_TIMEOUT);
				liftTalons[i].configClosedloopRamp(Definitions.LIFT_RAMP, Definitions.DEFAULT_TIMEOUT);// TODO change ramp rate for pid
				liftTalons[i].configForwardSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				liftTalons[i].configReverseSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				liftTalons[i].configContinuousCurrentLimit(25, 0);
				liftTalons[i].enableCurrentLimit(true);

			}
		}
	}

	public static synchronized void configIntakeTalons(ControlMode mode) {
		for (int i = 0; i < Definitions.NUM_INTAKE_TALONS; i++) {
			intakeTalons[i] = null;
			intakeTalons[i] = new WPI_TalonSRX(Definitions.INTAKE_TALON_ID[i]);
			if (intakeTalons[i] != null) {
				if (i % 2 == 1) // if follower
				{
					intakeTalons[i].set(ControlMode.Follower, intakeTalons[i - 1].getDeviceID()); // set to follow the TalonSRX with id i - 1;
				} else
				// if not follower
				{
					intakeTalons[i].set(mode, 0);
				}
				intakeTalons[i].setNeutralMode(Definitions.INTAKE_NEUTRAL_MODE[i]);
				intakeTalons[i].setInverted(Definitions.INTAKE_REVERSE_OUTPUT_MODE[i]);
				intakeTalons[i].configOpenloopRamp(Definitions.INTAKE_RAMP, Definitions.DEFAULT_TIMEOUT);
				intakeTalons[i].configClosedloopRamp(Definitions.INTAKE_RAMP, Definitions.DEFAULT_TIMEOUT);
				intakeTalons[i].configForwardSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				intakeTalons[i].configReverseSoftLimitEnable(false, Definitions.DEFAULT_TIMEOUT);
				intakeTalons[i].setSafetyEnabled(false);
				intakeTalons[i].configContinuousCurrentLimit(20, 0);
			}
		}
	}

	public static synchronized void changeTalonMode(WPI_TalonSRX target, ControlMode newMode, double newP, double newI,
	                                                double newD) throws TKOException {
		if (target == null)
			throw new TKOException("ERROR Attempted to change mode of null TalonSRX");
		if (newMode == target.getControlMode()) {
			target.config_kP(0, newP, Definitions.DEFAULT_TIMEOUT);
			target.config_kI(0, newI, Definitions.DEFAULT_TIMEOUT);
			target.config_kD(0, newD, Definitions.DEFAULT_TIMEOUT);
			return;
		} else {
			// if (target.getControlMode() != TalonSRX.TalonControlMode.Position && target.getControlMode() != TalonSRX.TalonControlMode.Speed)
			target.configSelectedFeedbackSensor(Definitions.DEFAULT_ENCODER_TYPE, 0, Definitions.DEFAULT_TIMEOUT);

			//TODO: print PID

			target.set(newMode, 0);
			target.config_kP(0, newP, Definitions.DEFAULT_TIMEOUT);
			target.config_kI(0, newI, Definitions.DEFAULT_TIMEOUT);
			target.config_kD(0, newD, Definitions.DEFAULT_TIMEOUT);
			//TODO: figure out if ya need enable
//		target.enableControl();
			System.out.println("CHANGED TALON [" + target.getDeviceID() + "] TO [" + target.getControlMode() + "]");
		}
	}

	public static synchronized void changeTalonMode(WPI_TalonSRX target, ControlMode newMode) throws TKOException {
		if (target == null)
			throw new TKOException("ERROR Attempted to change mode of null TalonSRX");

		// if (target.getControlMode() != TalonSRX.TalonControlMode.Position && target.getControlMode() != TalonSRX.TalonControlMode.Speed)
		target.configSelectedFeedbackSensor(Definitions.DEFAULT_ENCODER_TYPE, 0, Definitions.DEFAULT_TIMEOUT);

		target.set(newMode, 0);
//		target.enableControl();

		System.out.println("CHANGED TALON [" + target.getDeviceID() + "] TO [" + target.getControlMode() + "]");
	}

	@Deprecated
	public static synchronized void autonInit(double p, double i, double d) throws TKOException {
		TKOHardware.changeTalonMode(TKOHardware.getLeftDrive(), ControlMode.Position, p, i, d);
		TKOHardware.changeTalonMode(TKOHardware.getRightDrive(), ControlMode.Position, p, i, d);
		TKOHardware.getLeftDrive().setInverted(false);
		TKOHardware.getLeftDrive().setSensorPhase(true);
		//TODO: Check the trues on Right
		TKOHardware.getRightDrive().setInverted(true);
		TKOHardware.getRightDrive().setSensorPhase(true);
		TKOHardware.getLeftDrive().setNeutralMode(NeutralMode.Coast);
		TKOHardware.getRightDrive().setNeutralMode(NeutralMode.Coast);
//		TKOHardware.getLeftDrive().setPosition(0.); // resets encoder
//		TKOHardware.getRightDrive().setPosition(0.);
		TKOHardware.getLeftDrive().setIntegralAccumulator(0, 0, Definitions.DEFAULT_TIMEOUT); // stops bounce - used to be "ClearIaccum"
		TKOHardware.getRightDrive().setIntegralAccumulator(0, 0, Definitions.DEFAULT_TIMEOUT); //parameters: iaccum, pIDx, timeout
		Timer.delay(0.1);
		TKOHardware.getLeftDrive().setSelectedSensorPosition(0, 0, Definitions.DEFAULT_TIMEOUT);
		TKOHardware.getRightDrive().setSelectedSensorPosition(0, 0, Definitions.DEFAULT_TIMEOUT);
//		TKOHardware.getLeftDrive().set(ControlMode.Position, TKOHardware.getLeftDrive().getSelectedSensorPosition(0)); //if not using cascaded PID, set to 0
//		TKOHardware.getRightDrive().set(ControlMode.Position, TKOHardware.getRightDrive().getSelectedSensorPosition(0));
		TKOHardware.getLeftDrive().set(ControlMode.Position, TKOHardware.getLeftDrive().getSelectedSensorPosition(0)); //if not using cascaded PID, set to 0
		TKOHardware.getRightDrive().set(ControlMode.Position, TKOHardware.getRightDrive().getSelectedSensorPosition(0));

		//TKOHardware.getDSolenoid(0).set(Definitions.SHIFTER_LOW);
		//TODO: Update w/ pneumatics
//		TKOHardware.getDSolenoid(1).set(Definitions.GEAR_DOOR_DOWN);
//		TKOHardware.getDSolenoid(2).set(Definitions.GEAR_ARMS_IN); 

	}

	public static synchronized void autonInitDriveAtom() throws TKOException {
		TKOHardware.getRightDrive().setIntegralAccumulator(0, 0, Definitions.DEFAULT_TIMEOUT); //clear accumulated i
		TKOHardware.getLeftDrive().setIntegralAccumulator(0, 0, Definitions.DEFAULT_TIMEOUT);

		TKOHardware.getRightDrive().setSelectedSensorPosition(0, 0, Definitions.DEFAULT_TIMEOUT); //clear position
		TKOHardware.getLeftDrive().setSelectedSensorPosition(0, 0, Definitions.DEFAULT_TIMEOUT);

		TKOHardware.getRightDrive().configClosedloopRamp(Definitions.AUTON_DRIVE_RAMP, Definitions.DEFAULT_TIMEOUT);
		TKOHardware.getLeftDrive().configClosedloopRamp(Definitions.AUTON_DRIVE_RAMP, Definitions.DEFAULT_TIMEOUT);
	}

	public static synchronized void autonInitTurnAtom() throws TKOException {
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			TKOHardware.getDriveTalon(i).setInverted(true);
		}
		TKOHardware.getRightDrive().configClosedloopRamp(Definitions.AUTON_GYRO_RAMP, Definitions.DEFAULT_TIMEOUT);
		TKOHardware.getLeftDrive().configClosedloopRamp(Definitions.AUTON_GYRO_RAMP, Definitions.DEFAULT_TIMEOUT);
		initPIDControllers();
		gyro.reset();
		//add pid controllers reset?
	}

	/**
	 * Sets *ALL* drive Talons to given value. CAUTION WHEN USING THIS METHOD, DOES NOT CARE ABOUT FOLLOWER TALONS. Intended for PID Tuning
	 * loop ONLY.u
	 *
	 * @param setTarget - Value to set for all the talons
	 * @deprecated Try not to use this method. It is very prone to introducing errors. Use getLeftDrive() and getRightDrive() or
	 * getDriveTalon(int n) instead, unless you know what you are doing.
	 */
	public static synchronized void setAllDriveTalons(double setTarget) {
		for (int i = 0; i < Definitions.NUM_DRIVE_TALONS; i++) {
			if (driveTalons[i] != null) {
				driveTalons[i].set(ControlMode.Position, setTarget);
				;
			}
		}
	}

	public static synchronized XboxController getXboxController() throws TKOException {
		if (xbox == null)
			throw new TKOException("ERROR: Xbox controller is null");
		return xbox;
	}

	public static synchronized Joystick getJoystick(int num) throws TKOException {
		if (num >= Definitions.NUM_JOYSTICKS) {
			throw new TKOException("Joystick requested out of bounds");
		}
		if (joysticks[num] != null)
			return joysticks[num];
		else
			throw new TKOException("Joystick " + (num) + "(array value) is null");
	}

	/**
	 * To avoid potential problems, use getLeftDrive() and/or getRightDrive() instead
	 */
	public static synchronized WPI_TalonSRX getDriveTalon(int num) throws TKOException {
		if (num >= Definitions.NUM_DRIVE_TALONS) {
			throw new TKOException("Drive talon requested out of bounds");
		}
		if (driveTalons[num] != null) {
			// if (driveTalons[num].getControlMode() == TalonSRX.TalonControlMode.Follower)
			// throw new TKOException("WARNING: Do not access follower talon");
			// else
			return driveTalons[num];
		} else
			throw new TKOException("Drive talon " + (num) + "(array value) is null");
	}

	public static synchronized WPI_TalonSRX getLeftDrive() throws TKOException {
		if (driveTalons[2] == null || driveTalons[3] == null)
			throw new TKOException("Left Drive Talon is null");

		return driveTalons[2];
	}

	public static synchronized WPI_TalonSRX getRightDrive() throws TKOException {

		if (driveTalons[0] == null || driveTalons[1] == null)
			throw new TKOException("Right Drive Talon is null");

		return driveTalons[0];
	}

	public static synchronized WPI_TalonSRX getWinchTalon() throws TKOException {
		for (int i = 0; i < winchTalons.length; i++) {
			if (winchTalons[i] == null) {
				throw new TKOException("Winch Talon " + i + " is null");
			}
		}
		return winchTalons[0];
	}

	public static synchronized WPI_TalonSRX getWinchTalon(int n) throws TKOException {
		if (winchTalons[n] == null) {
			throw new TKOException("Winch Talon " + n + " is null");
		}
		return winchTalons[n];
	}

	public static synchronized WPI_TalonSRX getLiftTalon() throws TKOException {
		if (liftTalons[0] == null) {
			throw new TKOException("Lift Talon 0 is null");
		}
		return liftTalons[0];//liftTalons[0];
	}

	public static synchronized WPI_TalonSRX getLiftTalon(int n) throws TKOException {
		if (liftTalons[n] == null) {
			throw new TKOException("Lift Talon " + n + " is null");
		}
		return liftTalons[n];
	}

	public static synchronized WPI_TalonSRX getIntakeTalon() throws TKOException {
		for (int i = 0; i < intakeTalons.length; i++) {
			if (intakeTalons[i] == null) {
				throw new TKOException("intake Talon " + i + " is null");
			}
		}
		return intakeTalons[0];
	}

	public static synchronized WPI_TalonSRX getIntakeTalon(int n) throws TKOException {
		if (intakeTalons[n] == null) {
			throw new TKOException("Shooter Talon " + n + " is null");
		}
		return intakeTalons[n];
	}

	public static synchronized PIDController getLeftPIDControl() throws TKOException {
		if (pidControlLeft == null) {
			throw new TKOException("Left PID Controller is null");
		}
		return pidControlLeft;
	}

	public static synchronized PIDController getRightPIDControl() throws TKOException {
		if (pidControlRight == null) {
			throw new TKOException("Right PID Controller is null");
		}
		return pidControlRight;
	}

	public static synchronized DoubleSolenoid getDSolenoid(DSolenoid name) throws TKOException {
		switch (name) {
			case SHIFTER:
				if (shifter == null) throw new TKOException("Solenoid (Shifter) is null");
				return shifter;
			case ARM60:
				if (arm60 == null) throw new TKOException("Solenoid (Arm @ 60 psi) is null");
				return arm60;
			case ARM40:
				if (arm40 == null) throw new TKOException("Solenoid (Arm @ 40 psi) is null");
				return arm40;
			case UPDOWN:
				if (upDown == null) throw new TKOException("Solenoid (Up Down is null");
				return upDown;
			case RATCHET:
				if (ratchet == null) throw new TKOException("Solenoid (Ratchet) is null");
				return ratchet;
		}
		throw new TKOException("How the hecky did you get here. Only exists 'cause of Eclipse.");
	}

	public static synchronized DigitalInput getDigitalInput(DInput name) throws TKOException {
		switch (name) {
			case LIFT_3RD_STAGE:
				if (digitalInputs[0] == null) throw new TKOException("3rd stage limit is null");
				return digitalInputs[0];
			case LIFT_2ND_STAGE:
				if (digitalInputs[1] == null) throw new TKOException("2nd stage limit is null");
				return digitalInputs[1];
			case INTAKE:
				if (digitalInputs[2] == null) throw new TKOException("Intake limit is null");
				return digitalInputs[2];
			case LIFT_0:
				if (digitalInputs[3] == null) throw new TKOException("0 limit is null");
				return digitalInputs[3];
		}
		throw new TKOException("How the hecky did you get here. Only exists 'cause of Eclipse.");
	}

	public static synchronized Compressor getCompressor() throws TKOException {
		if (compressor == null)
			throw new TKOException("Compressor is null");
		return compressor;
	}

	public static synchronized ADXRS450_Gyro getGyro() throws TKOException {
		if (gyro == null)
			throw new TKOException("ERROR: Gyro is null");
		return gyro;
	}

	/**
	 * Put pneumatic arms at 20 psi pushing inwards<p>
	 * Purpose: intaking cube
	 *
	 * @throws TKOException TKOException
	 * @author Tiina
	 */
	public static synchronized void setArmsSoftHold() throws TKOException {
		TKOHardware.getDSolenoid(DSolenoid.ARM40).set(Value.kForward); //40 branch on
		TKOHardware.getDSolenoid(DSolenoid.ARM60).set(Value.kForward); //60 branch on
		//60(1) - 40(1) = 20 psi
	}

	/**
	 * Put pneumatic arms at 60 psi pushing inwards<p>
	 * Purpose: holding cube
	 *
	 * @throws TKOException TKOException
	 * @author Tiina
	 */
	public static synchronized void setArmsHardHold() throws TKOException {
		TKOHardware.getDSolenoid(DSolenoid.ARM40).set(Value.kReverse); //40 branch off
		TKOHardware.getDSolenoid(DSolenoid.ARM60).set(Value.kForward); //60 branch on
		//60(1) - 40(0) = 60 psi
	}

	/**
	 * Put pneumatic arms at 40 psi pushing outwards<p>
	 * Purpose: dropping cube
	 *
	 * @throws TKOException TKOException
	 * @author Tiina
	 */
	public static synchronized void setArmsRelease() throws TKOException {
		TKOHardware.getDSolenoid(DSolenoid.ARM40).set(Value.kForward); //40 branch on
		TKOHardware.getDSolenoid(DSolenoid.ARM60).set(Value.kReverse); //60 branch off
		//60(0) - 40(1) = -40 psi (40 psi out)
	}
}
