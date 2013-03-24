package control.data;

public class DataPoint
{

	private int light;
	private int sound;
	private boolean touch;
	private int ultrasonic;
	
	public DataPoint(int light, int sound, int ultrasonic, boolean touch)
	{
		this.light = light;
		this.sound = sound;
		this.touch = touch;
		this.ultrasonic = ultrasonic;
	}
	
	public int getLight()
	{
		return light;
	}
	public int getSound()
	{
		return sound;
	}
	public boolean getTouch()
	{
		return touch;
	}
	public int getUltrasonic()
	{
		return ultrasonic;
	}
}
