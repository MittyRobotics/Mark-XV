//Last edited by Tiina Otala
//1/12/2018

package org.usfirst.frc.team1351.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI.Port;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class Definitions {
	//V A L U E S
	public static final int DEFAULT_TIMEOUT = 0;
	public static final int DEFAULT_RAMP = 7;
	public static final double DRIVE_RAMP = 0.5;
	public static final double INTAKE_RAMP = 1;
	public static final double LIFT_RAMP = 0.5;
	public static final double INTAKE_LEFT_SPEED = -1; //in PercentOutput
	public static final double INTAKE_RIGHT_SPEED = 1;
	public static final int LIFT_HOLD_TIME = 5; // in seconds
	public static final double LIFT_HOLD_VOLT = 0.05; // in percent output
	//AUTON PID
	public static final double TICKS_PER_INCH_HIGH = 167.3;//Low: 509 or 488
	public static final double TICKS_PER_INCH_LOW = 488;//Low: 509 or 488
	public static final int LIFT_TICKS_PER_INCH = 651; // temporarily
	public static final double REVOLUTIONS_TO_TICKS = 6000. / 1024.;
	public static final double TICKS_TO_REVOLUTIONS = 1024. / 6000.;
	public static final double AUTON_DRIVE_P = 0.12; //FOR SILVER CHASSIS 0.034?
	public static final double AUTON_DRIVE_I = 0; //FOR SILVER CHASSIS 0.000007
	public static final double AUTON_DRIVE_D = 0;
	public static final double AUTON_GYRO_TURN_P = 0.0099; //0.0048; //0/005 works with low gear (black)
	public static final double AUTON_GYRO_TURN_I = 0; //0.0000001;
	public static final double AUTON_GYRO_TURN_D = 0; //tested with new chassis (silver), high gear
	public static final double AUTON_DRIVE_RAMP = 0;
	public static final double AUTON_DRIVE_THRESHOLD_LOW = TICKS_PER_INCH_LOW * 1.5;
	public static final double AUTON_DRIVE_THRESHOLD_HIGH = TICKS_PER_INCH_LOW * 3;
	public static final double AUTON_DRIVE_INCREMENTER_LOW = 100;
	public static final double AUTON_DRIVE_INCREMENTER_HIGH = 500;
	public static final double AUTON_GYRO_RAMP = 2;
	public static final double AUTON_GYRO_THRESHOLD = 5;
	//EVOM PID
	public static final double DRIVE_P = 0.1;
	public static final double DRIVE_I = 0.01;
	public static final double DRIVE_D = 0;
	public static final int DRIVE_TIMEOUT = 1000;
	public static final int DRIVE_SLOT = 0;
	public static final double[] DRIVE_MULTIPLIER =
			{1., 1., 1., 1.};
	public static final double DRIVE_MULTIPLIER_LEFT = DRIVE_MULTIPLIER[0];
	public static final double DRIVE_MULTIPLIER_RIGHT = DRIVE_MULTIPLIER[2];

	public static final double LIFT_P = 0.1;
	public static final double LIFT_I = 0;
	public static final double LIFT_D = 0;
	public static final double LIFT_THRESHOLD = 2 * Definitions.LIFT_TICKS_PER_INCH;
	public static final int LIFT_TIMEOUT = 1000;
	public static final int LIFT_SLOT = 0;
	public static final double WINCH_P = 1.0; //all winch is old
	public static final double WINCH_I = 0.01;
	public static final double WINCH_D = 0.0;
	public static final int WINCH_TIMEOUT = 1000;
	public static final int WINCH_SLOT = 0;
	public static final double WINCH_LADEN_FIXED_OUTPUT = 0.35; //laden and unladen output from last year was .5 and .2 respectively;
	public static final double WINCH_LADEN_OUTPUT = .5f; //TODO:  This is a little above for range, needs to be a clear difference however for this to work
	public static final double RUNG_DISTANCE = 5.0;  //TODO: Find the actual distance later

	public static final long[] CURRENT_TIMEOUT_LENGTH =
			{1000L, 1000L, 1000L, 1000L, 1000L, 1000L};
	public static final int DEF_DATA_REPORTING_THREAD_WAIT = 250;
	public static final double PULSES_PER_INCH = 332.5020781;

	//--------------------------------------------------------------------------

	//C A N T A L O N S
	//FEEDBACK
	//TODO: check feedback devices
	public static final FeedbackDevice DEFAULT_ENCODER_TYPE = FeedbackDevice.QuadEncoder;
	public static final FeedbackDevice DRIVE_ENCODER_TYPE = FeedbackDevice.CTRE_MagEncoder_Relative;
	public static final FeedbackDevice LIFT_ENCODER_TYPE = FeedbackDevice.QuadEncoder;
	public static final FeedbackDevice WINCH_ENCODER_TYPE = FeedbackDevice.Analog; //TODO: analog sensor is supposed to give battery voltage?

	//MODES
	public static final ControlMode DRIVE_TALONS_NORMAL_CONTROL_MODE = ControlMode.PercentOutput;
	public static final ControlMode LIFT_TALONS_NORMAL_CONTROL_MODE = ControlMode.PercentOutput;
	public static final ControlMode WINCH_TALONS_NORMAL_CONTROL_MODE = ControlMode.Current;
	public static final ControlMode INTAKE_TALONS_NORMAL_CONTROL_MODE = ControlMode.Current; //TODO: TBD

	//REVERSE
	public static final boolean[] DRIVE_REVERSE_OUTPUT_MODE =
			{true, true, false, false};
	public static final boolean[] DRIVE_REVERSE_SENSOR = //Silver Chassis: Left is negative (T2)
			{false, false, true, false};
	public static final boolean[] WINCH_REVERSE_OUTPUT_MODE =
			{false, false};
	public static final boolean[] WINCH_REVERSE_SENSOR =
			{false, false};
	public static final boolean[] LIFT_REVERSE_OUTPUT_MODE =
			{false, false};
	public static final boolean[] LIFT_REVERSE_SENSOR =
			{true, true};
	public static final boolean[] INTAKE_REVERSE_OUTPUT_MODE =
			{false, false};

	public static final NeutralMode[] DRIVE_NEUTRAL_MODE =
			{NeutralMode.Coast, NeutralMode.Coast, NeutralMode.Coast, NeutralMode.Coast};
	public static final NeutralMode[] LIFT_NEUTRAL_MODE =
			{NeutralMode.Coast, NeutralMode.Coast};
	public static final NeutralMode[] WINCH_NEUTRAL_MODE =
			{NeutralMode.Coast, NeutralMode.Coast};
	public static final NeutralMode[] INTAKE_NEUTRAL_MODE =
			{NeutralMode.Brake, NeutralMode.Brake};

	public static final double CURRENT_SAFETY_THRESHOLD = 60.; // Amps for the motors

	//CONSTANTS
	public static final int[] DRIVE_TALON_ID = //TODO: Note which ones are which!!!
			{0, 1, 2, 3};                                //TODO: FOR AUTON ID 3 is the one that's messed up. Reset talon IDs.
	public static final int[] WINCH_TALON_ID =
			{8, 4};
	public static final int[] LIFT_TALON_ID =
			{9, 5};
	public static final int[] INTAKE_TALON_ID = //6 is right, 7 is left
			{7, 6};
	public static final int[] JOYSTICK_ID =
			{0, 1, 2};
	public static final int[] DINPUT_ID =
			{0, 1, 2, 3};
	public static final int NUM_DRIVE_TALONS = 4;
	public static final int NUM_WINCH_TALONS = 2;
	public static final int NUM_LIFT_TALONS = 2;
	public static final int NUM_INTAKE_TALONS = 2;
	public static final int ALL_TALONS = NUM_DRIVE_TALONS + NUM_WINCH_TALONS + NUM_LIFT_TALONS + NUM_INTAKE_TALONS; //should be 10
	public static final double[] TALON_CURRENT_s =
			{5, 5, 5, 5, 5, 5, 5, 5, 5};
	//CONTROLLERS + SENSORS
	//TODO: update
	public static final int NUM_ENCODERS = 0;

	public static HashMap<String, Integer> threadPriorities;

	//--------------------------------------------------------------------------

	public static void addThreadName(String name) {
		threadNames.add(name);
	}

	//PRIORITIES
	//TODO: Check
	public static int getPriority(String name) {
		switch (name) {
			case "drive":
				return 9;
			case "pneumatics":
				return 8;
			case "intake":
				return 7;
			case "lift":
				return 6;
			case "winch":
				return 5;
			case "ledArduino":
				return 4;
			case "logger":
				return 3;
			case "talonSafety":
				return 2;
			default:
				return Thread.NORM_PRIORITY; // 5
		}
	}

	public enum DInput {
		LIFT_3RD_STAGE,
		LIFT_2ND_STAGE,
		INTAKE,
		LIFT_0
	}

	public enum DSolenoid {
		SHIFTER,
		ARM60,
		ARM40,
		UPDOWN,
		RATCHET
	}
}
