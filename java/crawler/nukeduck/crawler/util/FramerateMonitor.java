package nukeduck.crawler.util;

public class FramerateMonitor implements ITickable {
	private long[] frameTimes = new long[1 << 8];
	private float framerate;

	private RepeatingEvent update;

	public FramerateMonitor(long update) {
		System.out.println("Rolling framerate resolution: " + this.frameTimes.length);
		this.update = new RepeatingEvent(update) {
			@Override
			public void call() {
				framerate = 0.0F;
				for(long l : frameTimes) {
					framerate += (float) l;
				}
				framerate /= (float) frameTimes.length;
				framerate = 1000000000.0F / framerate;
				framerate = (float) Math.round(framerate * 10) / 10.0F;
			}
		};
	}

	private int currentIndex = 0;

	public void add(long frameTime) {
		if(++this.currentIndex >= this.frameTimes.length) this.currentIndex = 0;
		this.frameTimes[this.currentIndex] = frameTime;
	}

	public void tick() {
		this.update.tick();
	}

	public float getFramerate() {
		return this.framerate;
	}
}
