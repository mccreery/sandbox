package nukeduck.crawler.graphics;

import nukeduck.crawler.Crawler;

public class HashedText {
	public static final long mulp = 2654435789L;

	public static final long quickHash64(CharSequence text) {
		long mix = 104395301L;
		for(int i = 0; i < text.length(); i++) {
			mix += (text.charAt(i) * mulp) ^ (mix >> 23);
		}
		return mix ^ (mix << 37);
	}

	private int cacheSize;
	public HashedText(int cacheSize) {
		this.cacheSize = cacheSize;
		this.hashCache = new long[this.cacheSize];
		this.vboCache = new VBO[this.cacheSize];
	}

	private int index = -1;

	private long[] hashCache;
	private VBO[] vboCache;

	public VBO get(CharSequence text) {
		long hash = quickHash64(text);
		int max = ++this.index >= this.cacheSize ? this.cacheSize : this.index;

		int i;
		for(i = 0; i < max; i++) {
			if(this.hashCache[i] == hash) return this.vboCache[i];
		}

		i = this.index % this.cacheSize;
		this.hashCache[i] = hash;
		return this.vboCache[i] = this.generate(text);
	}

	protected VBO generate(CharSequence text) {
		VBO vbo = new VBO(new TextMesh(Crawler.INSTANCE.font, text));
		vbo.prepare();
		return vbo;
	}
}
