package nukeduck.crawler.util;

public class Profiler {
	long start = 0;

	int pos = 0;
	long[] laps;

	public Profiler(int laps) {
		this.laps = new long[laps];
		this.pos = 0;
	}

	public Profiler reset() {
		this.start = System.nanoTime();
		this.pos = 0;
		return this;
	}

	public void lap() {
		long end = System.nanoTime();
		this.laps[this.pos++] = end - this.start;
		this.start = end;
	}

	public long[] raw() {
		return this.laps;
	}

	public double[] normalizedAverage() {
		double avg = 0.0;
		for(int i = 0; i < this.pos; i++) {
			avg += this.laps[i];
		}
		avg /= (double) this.pos;

		double[] laps = new double[this.pos];
		for(int i = 0; i < this.pos; i++) {
			laps[i] = (double) this.laps[i] / avg;
		}
		return laps;
	}

	public double[] normalizedTotal() {
		double total = 0.0;
		for(int i = 0; i < this.pos; i++) {
			total += this.laps[i];
		}

		double[] laps = new double[this.pos];
		for(int i = 0; i < this.pos; i++) {
			laps[i] = (double) this.laps[i] / total;
		}
		return laps;
	}
}
