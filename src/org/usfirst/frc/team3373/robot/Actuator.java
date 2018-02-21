package org.usfirst.frc.team3373.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class Actuator{
	private int actuatorName; 
	private double position1;
	private double maxPot;
	private double minPot;
	private double maxDistance;
	private double minDistance;
	private boolean toExtreme;
	SupremeTalon talon1;
	SupremeTalon talon2;
	public Actuator(int port1,int port2,double maxPot1, double minPot1, double maxTravel, double minTravel){
		actuatorName = port1; // For calibration proccedor, gives Dashboard name for actuator
		talon1 = new SupremeTalon(port1);
		talon2 = new SupremeTalon(port2);
		talon1.setBrakeMode();
		talon2.setBrakeMode();
		maxPot = maxPot1;
		minPot = minPot1;
		maxDistance = maxTravel; // Distance is in Centimeters
		minDistance = minTravel;
		position1 = this.getPosition();
		toExtreme = false;
		}
	public double getPosition(){
		//returns the distance in centimeters
		double deltaPot = maxPot-minPot;
		double deltaDistance = maxDistance - minDistance;
		double slope = deltaDistance/deltaPot;
		return slope*(talon1.getRawSensor()-minPot)+minDistance; //calculates linear relationship between pot values and centimeters
	}
	public double getRawPosition(int num){
		//returns Analog Position
		return talon1.getRawSensor(); //talon1 is the one with the pot
	}
	public void set(double speed){
		double initialspeed = speed;
		if(speed >0){//going towards top of travel
			toExtreme = false;
			double distanceToTop = maxPot-talon1.getRawSensor();
			speed = speed * Math.abs(distanceToTop)*.0175; //set speed to be proportional to the distance from extrema
			if(speed > initialspeed){ //makes sure the magnitude of the velocity is not greater than the passed in velocity
				speed = initialspeed;
			}
			if((talon1.getRawSensor()>maxPot)){ //final fail safe for if the actuator exceeds the travel 
				this.superSet(0);
				toExtreme = true;
			}
		}else if(speed<0){	//going towards bottom of travel
			toExtreme = false;
			double distanceToBot = talon1.getRawSensor()- minPot;
			speed= speed * Math.abs(distanceToBot)*.0175;  //set speed to be proportional to the distance from extrema
			if(speed < initialspeed){ //makes sure the magnitude of the velocity is not greater than the passed in velocity
				speed = initialspeed;
			}
			if((talon1.getRawSensor()<minPot)){
				this.superSet(0);
				toExtreme = true;
			}
		}
			if(!toExtreme){
				talon1.accelerate(speed, .05, false);
				talon2.accelerate(speed, .05, false);
			}
	}
	public void superSet(double speed){
		talon1.set(speed);
		talon2.set(speed);
	}
	public void calibrate(SuperJoystick stick){
		//Move actuator towards min/max  @ about 30 cm or @ about 1.5 cm
		//Manually move actuator to roughly min and max
		// record Potentiometer and Cm Values in Robot.java in the Dual Actuator Calibration Setting
		talon1.set(stick.getRawAxis(1)*.2);
		talon2.set(stick.getRawAxis(1)*.2);
		SmartDashboard.putNumber("Actuator PotValue " + actuatorName,talon1.getRawSensor());
	}
	public double getMaxHeight(){
		return maxDistance;
	}
	public double getMinHeight(){
		return minDistance;
	}
	public double getVelocity(){
		double currentPosition = this.getPosition();
		return (currentPosition-position1)/.01;
	}
	public void setVelocity(double velocity){ //in cm per second
		double error = velocity - this.getVelocity(); 
	}
	public double getOutputCurrent(){
		return (talon1.getOutputCurrent() + talon2.getOutputCurrent())/2;
	}
}
