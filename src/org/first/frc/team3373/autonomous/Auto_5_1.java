package org.first.frc.team3373.autonomous;

import org.usfirst.frc.team3373.robot.DualActuators;
import org.usfirst.frc.team3373.robot.Grabber;
import org.usfirst.frc.team3373.robot.SwerveControl;

public class Auto_5_1 {

	SwerveControl swerve;
	DualActuators lifter;
	Grabber grabber;
	public Auto_5_1(SwerveControl swerveDrive, DualActuators actuators, Grabber cubeGrabber){
		swerve = swerveDrive;
		lifter = actuators;
		grabber = cubeGrabber;
		}
		
		public void run(){

		}

}
