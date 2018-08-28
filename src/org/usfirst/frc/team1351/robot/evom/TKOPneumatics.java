// Last edited by Tiina Otala
// on 3/3/2018

package org.usfirst.frc.team1351.robot.evom;

import org.usfirst.frc.team1351.robot.Definitions;
import org.usfirst.frc.team1351.robot.Definitions.DSolenoid;
import org.usfirst.frc.team1351.robot.util.TKOException;
import org.usfirst.frc.team1351.robot.util.TKOHardware;
import org.usfirst.frc.team1351.robot.util.TKOThread;

public class TKOPneumatics implements Runnable {
	private static TKOPneumatics m_Instance = null;
	public TKOThread pneuThread = null;

	private TKOPneumatics() {
		try {
			TKOHardware.getCompressor().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized TKOPneumatics getInstance() {
		if (TKOPneumatics.m_Instance == null) {
			m_Instance = new TKOPneumatics();
			m_Instance.pneuThread = new TKOThread(m_Instance);
		}
		return m_Instance;
	}

	public synchronized void start() {
		System.out.println("Starting pneumatics task");
		if (!pneuThread.isAlive() && m_Instance != null) {
			pneuThread = new TKOThread(m_Instance);
			pneuThread.setPriority(Definitions.getPriority("pneumatics"));
		}
		if (!pneuThread.isThreadRunning())
			pneuThread.setThreadRunning(true);

		try {
			//start w/ compressor on, intake up (safety's sake) and loose
			TKOHardware.getCompressor().start();
			TKOHardware.getDSolenoid(DSolenoid.UPDOWN).set(Definitions.INTAKE_LIFT_DOWN);
			TKOHardware.setArmsHardHold();
		} catch (TKOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		System.out.println("Stopping pneumatics task");
		if (pneuThread.isThreadRunning())
			pneuThread.setThreadRunning(false);
		try {
			TKOHardware.getCompressor().stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Stopped pneumatics task");
	}

	@Override
	public void run() {
		try {
			while (pneuThread.isThreadRunning()) {

				synchronized (pneuThread) {
					pneuThread.wait(20);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}