package nukeduck.crawler.util;

public class Variables {
	public static final String getWindowTitle() {
		return "A Game";
	}

	/** Stores Java version as a packed integer - 1.7.0_65 translates to {@code (1 & 0xFF) << 24 | (7 & 0xFF) << 16 | (0 & 0xFF) << 8 | (65 & 0xFF)}
	 * @see #getVersion(int, int, int, int) */
	public static final int JAVA_VERSION = getjavaVersion();

	public static final int VERSION_1_5 = getVersion(1, 5);
	public static final int VERSION_1_6 = getVersion(1, 6);
	public static final int VERSION_1_7 = getVersion(1, 7);
	public static final int VERSION_1_8 = getVersion(1, 8);

	public static final boolean hasVersion(int major, int minor, int update, int build) {
		return JAVA_VERSION >= getVersion(major, minor, update, build);
	}

	/** Same as {@link #getVersion(int, int, int, int)} but all variables past major are considered to be 0. */
	public static final int getVersion(int major) {return getVersion(major, 0);}
	/** Same as {@link #getVersion(int, int, int, int)} but update and build are considered to be 0. */
	public static final int getVersion(int major, int minor) {return getVersion(major, minor, 0);}
	/** Same as {@link #getVersion(int, int, int, int)} but build is considered to be 0. */
	public static final int getVersion(int major, int minor, int update) {return getVersion(major, minor, update, 0);}
	/** @return The given version as a packed integer - 1.7.0_65 translates to {@code (1 & 0xFF) << 24 | (7 & 0xFF) << 16 | (0 & 0xFF) << 8 | (65 & 0xFF)} */
	public static final int getVersion(int major, int minor, int update, int build) {
		return (major & 0xFF) << 24 | (minor & 0xFF) << 16 | (update & 0xFF) << 8 | (build & 0xFF);
	}

	private static final int getjavaVersion() {
		String version = System.getProperty("java.version");
		int a = version.indexOf('.');
		int major = Integer.parseInt(version.substring(0, a = version.indexOf('.')));
		int minor = Integer.parseInt(version.substring(++a, a = version.indexOf('.', a)));
		int update = Integer.parseInt(version.substring(++a, a = version.indexOf('_', a)));
		int build = Integer.parseInt(version.substring(++a));

		int verPacked = getVersion(major, minor, update, build);
		System.out.println("Java version: " + version + " packed " + verPacked);
		return verPacked;
	}
}
