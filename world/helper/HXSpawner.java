package world.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class HXSpawner {
	
	private Timer spawnTimer = new Timer();
	private Boolean timerRunning = true;
	private double rate;
	private Class<?>[] parameterTypes;
	private Object[] parameterValues;
	private Class<?> entityClass;
	
	/**
	 * Sets up a timed reoccuring spawn of the given entity. 
	 * <p>
	 * <b>Example:</b><br>
	 * &nbsp&nbsp new HXSpawner( HXEntityTemplate.class, <br>
	 * &nbsp&nbsp&nbsp&nbsp new Class[] {int.class, int.class, double.class, double.class, HXWorld.class}, <br>
	 * &nbsp&nbsp&nbsp&nbsp new object[] { 50, 50, 6.5, 0, this}, <br>
	 * &nbsp&nbsp&nbsp&nbsp SPAWN_RATE_SECS <br>
	 * &nbsp&nbsp&nbsp&nbsp );
	 * @param entityClass - A subclass of the HXEntity class
	 * @param parameterTypes - The data types for the given classes constructor's parameters
	 * @param parameterValues - The values for the parameters of the classes constructor
	 * @param rate - How often a new entity will spawn (in seconds)
	 */
	public HXSpawner(Class<?> entityClass, Class<?>[] parameterTypes, Object[] parameterValues, double rate) {
		this.rate = rate;
		this.parameterTypes = parameterTypes;
		this.parameterValues = parameterValues;
		this.entityClass = entityClass;
		
		cycle();
	}
	
	/**
	 * Internal class usage only.
	 * <p>
	 * Abstracted from the constructor to allow TimerTask to call itself as a method
	 */
	private void cycle() {
		if (timerRunning) {
			try {
				entityClass.getConstructor(parameterTypes).newInstance(parameterValues);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			spawnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					cycle();
				}
			}, (long) ( rate * 1000));
		}
	}
	
	/**
	 * Start the HXSpawner if it was previously stopped.
	 * <p>
	 * This will occur automatically when a new HXSpawner is instantiated
	 */
	public void start() {
		if (!timerRunning) {
			timerRunning = true;
			spawnTimer = new Timer();
			spawnTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					cycle();
				}
			}, (long) ( rate * 1000));
		}
	}
	
	/**
	 * Halt the running of an active HXSpawner.
	 */
	public void stop() {
		if (timerRunning) {
			timerRunning = false;
			spawnTimer.cancel();
		}
	}
}
