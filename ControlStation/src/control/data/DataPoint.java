
package control.data;

/**
 * Used to organize the telemetry data read from the robot
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 *
 */
public class DataPoint
{

	private int light;
	private int sound;
	private boolean touch;
	private int ultrasonic;
	
	/**
	 * 4 parameter constructor creates datapoint based on all telemetry data
	 * @param light	data from light sensor
	 * @param sound	data from sound sensor
	 * @param ultrasonic	ultrasonic data from ultrasonic sensor
	 * @param touch	touch data from touch sensor
	 */
	public DataPoint(int light, int sound, int ultrasonic, boolean touch)
	{
		this.light = light;
		this.sound = sound;
		this.touch = touch;
		this.ultrasonic = ultrasonic;
	}
	
	/**
	 * Get method for light  data
	 * @return light	light intensity integer
	 */
	public int getLight()
	{
		return light;
	}
	
	/**
	 * Get method for sound data
	 * @return sound 	integer sound level
	 */
	public int getSound()
	{
		return sound;
	}
	
	/**
	 * Get method for touch data
	 * @return touch	boolean touch, not-touch snesor
	 */
	public double getTouch()
	{
		if(touch) {			//for drawing graph
			return 100.0;
		}
		return 0.0;
	}
	
	/**
	 * Get method for ultraonsic sensor
	 * @return ultrasonic	integer value for ultrasonic sensor
	 */
	public int getUltrasonic()
	{
		return ultrasonic;
	}
}
