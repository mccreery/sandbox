package nukeduck.crawler.util;

import java.util.HashMap;
import java.util.Map;

public class LegacyFunction<R> {
	private Map<Integer, IDelegate<R>> delegates = new HashMap<Integer, IDelegate<R>>();
	private int targetVersion;

	public LegacyFunction() {
		this(Variables.JAVA_VERSION);
	}
	public LegacyFunction(int targetVersion) {
		this.targetVersion = targetVersion;
	}

	public LegacyFunction<R> add(int version, IDelegate<R> delegate) {
		this.delegates.put(version, delegate);
		return this;
	}

	public R invoke(Object... args) throws NoSuchMethodException {
		return this.invoke(this.getTargetVersion(), args);
	}
	public R invoke(int targetVersion, Object... args) throws NoSuchMethodException {
		int version = this.getTargetVersion();

		int supportedVersion = -1;
		IDelegate<R> delegate = null;

		for(int key : this.delegates.keySet()) {
			if(key > supportedVersion && key <= version) {
				delegate = this.delegates.get(key);
				if((supportedVersion = key) == version) break;
			}
		}
		if(delegate == null) throw new NoSuchMethodException("Could not find any supported method");

		return delegate.invoke(args);
	}

	public int getTargetVersion() {
		return this.targetVersion;
	}

	public static interface IDelegate<R> {
		public R invoke(Object... args);
	}
}
