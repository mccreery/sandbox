package nukeduck.crawler.util;

public abstract class RepeatingEvent implements ITickable {
	private long delay;
	private long delayTicker;

	public RepeatingEvent(long delay) {
		this.delay = delay;
		this.reset();
	}

	public RepeatingEvent setDelay(long delay) {
		this.delay = delay;
		return this.reset();
	}

	public long getDelay() {
		return this.delay;
	}

	public RepeatingEvent reset() {
		this.delayTicker = this.delay;
		return this;
	}

	public void tick() {
		if(--this.delayTicker <= 0) {
			this.call();
			this.delayTicker = this.delay;
		}
	}

	public abstract void call();
}
