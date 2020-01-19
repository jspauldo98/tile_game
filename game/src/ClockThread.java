
/**
 * @author Jordan Ruckle, Jared Spaulding, William Cary, Shin Yamagami, and Miles Golding
 * Date: May 1, 2019
 *
 * This class updates the menu clock ever second
 */

import java.util.TimerTask;
import javax.swing.*;

public class ClockThread extends Thread {

	private JLabel clock_;
	private long start;
	private boolean run = true;
	private long seconds = 0;
	private long minutes = 0;
	private long hours = 0;
	private long time = 0;

	/**
	 * Creates a clock thread with the start parameter being
	 * 
	 * @param The   point in time to at which you would like the clock to begin
	 *              counting
	 * @param clock A jLabel where the clock will print values
	 */
	public ClockThread(long start, JLabel clock) {
		this.clock_ = clock;
		this.start = start;
	}

	/**
	 * Prevents the object from being a null pointer
	 */
	public ClockThread() {
		// keep this from being a null pointer
	}

	/**
	 * Restarts the hours, minutes, and seconds and the starting position of the
	 * clock Essentially ressetting the clock to zero.
	 */
	public void restart() {
		start = System.currentTimeMillis();
		seconds = 0;
		minutes = 0;
		hours = 0;
	}

	/**
	 * Restarts at a particular starting position
	 */
	public void restart(long s) {
		start = System.currentTimeMillis() - (s * 1000);

		hours = (long) Math.floor((double) (s / 3600));
		s = s - (hours * 3600);
		minutes = (long) Math.floor((double) (s / 60));
		s = s - (minutes * 60);
		seconds = s;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Runs the clock counting changing the display 1 second every second
	 */
	public void run() {
		try {
			while (run) {
				time = (System.currentTimeMillis() - start) / 1000;
				seconds++;
				if (seconds == 60) {
					seconds = 0;
					minutes++;
				}
				if (minutes == 60) {
					minutes = 0;
					hours++;
				}
				String sHours = "", sMinutes = "", sSeconds = "";
				if (hours < 10) {
					sHours = "0" + String.valueOf(hours);
				} else
					sHours = String.valueOf(hours);
				if (minutes < 10)
					sMinutes = "0" + String.valueOf(minutes);
				else
					sMinutes = String.valueOf(minutes);
				if (seconds < 10)
					sSeconds = "0" + String.valueOf(seconds);
				else
					sSeconds = String.valueOf(seconds);

				clock_.setText(sHours + ":" + sMinutes + ":" + sSeconds);
				Thread.sleep(1000);
			}

		} catch (Exception e) {

		}
	}

	public void stopClock() {
		run = false;
	}
}