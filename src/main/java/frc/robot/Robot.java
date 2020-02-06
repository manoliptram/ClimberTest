/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 * 
 * All periodic code is under testPeriodic() for the purposes of climber testing.
 * Values below assume a single talon and a flight simulator joystick are used.
 */
public class Robot extends TimedRobot
{
  /* Instance Variable Declaration */
  // Ports
  // TODO: TAL_PORT needs to be changed to the correct value before testing
  final int TAL_PORT = 0;
  final int JOY_PORT = 0;

  // Important joystick values
  // MAIN_AXIS assumes a flight similator joystick will be used (as opposed to an xbox controller)
  // TODO: Double-check MAIN_AXIS and INVERT before testing
  final int MAIN_AXIS = 1;
  final double DEADZONE = 0.1;
  final int INVERT = -1;

  // Parabolic Motor Control Values
  // C = 0 since the motor should not be turning when the joystick is in the deadzone
  /*
   * These values were all derived assuming the points (0,0), (1,1), and (0.5, 0.4)
   * would be on the quadratic curve. You can use an RREF calcuator to change these values if you'd like,
   * but I'd recommend keeping at least (0,0) and (1,1).
   */
  final double A = 0.4;
  final double B = 0.6;

  // Important Devices - Declaration
  TalonSRX climbTal;
  Joystick joy;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit()
  {
    // Important Devices - Initialization
    climbTal = new TalonSRX(TAL_PORT);
    joy = new Joystick(JOY_PORT);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic()
  {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit()
  {
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
    
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic()
  {

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
    // If the joystick value is outside the deadzone, set the talon to 0.
    if (Math.abs(joy.getRawAxis(MAIN_AXIS)) < DEADZONE)
    {
      climbTal.set(ControlMode.PercentOutput, 0);
    }

    else
    {
      /* Calculate the appropriate values based on the joystick input */
      // Magnitude of the joystick input (range of [0, 1]).
      double inputMagnitude = Math.abs(joy.getRawAxis(MAIN_AXIS));

      // Direction corresponding to inputMagnitude
      /* TODO:
       * You can delete this bit if you only want to move the axis forwards,
       * but I recommend keeping it so you can decelerate as you put the weight down.
       */
      int sign = (int) (Math.abs(joy.getRawAxis(MAIN_AXIS)) / (joy.getRawAxis(MAIN_AXIS)*INVERT));
      // Modified output given to the Talon (incorporating parabolic control, inputMagnitude, and sign)
      double output = (A*Math.pow(inputMagnitude, 2) + B*inputMagnitude) * sign;

      // Debug check to make sure nothing in the calculations went wrong
      // TODO: I seriously recommend checking the calculations with a mentor and removing this entirely
      if (Math.abs(output) > 1)
      {
        System.out.println("Invalid Output Value: " + output);
        climbTal.set(ControlMode.PercentOutput, 0);
      }

      // Set the talon to the modified output value
      // TODO: Remove the "else" statement after you remove the above "if" statement and before you test.
      else
      {
        climbTal.set(ControlMode.PercentOutput, output);
      }
    }
  }
}
