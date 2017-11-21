package nukeduck.crawler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import nukeduck.crawler.util.Vec2;
import nukeduck.crawler.util.logging.LogFormatter;

public class Launch {
	public static final Logger logger = Logger.getLogger(Launch.class.getName());

	static {
		Logger rootLogger = Logger.getLogger("");

		Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			handlers[0].setFormatter(new LogFormatter());
		}
	}

	public static final void main(String[] args) {
		LaunchArgs data = LaunchArgs.deserialize(args);
		new Crawler().start(data);
	}

	public static class LaunchArgs {
		public String[] unsorted;

		@Shortened('w')
		private int width = 1280;
		@Shortened('h')
		private int height = 720;

		@Shortened('m')
		private String monitor = "0";
		public int monitorId = 0;

		@Shortened('f')
		public boolean fullscreen = false;

		public Vec2 size;

		@Shortened('u')
		public String username;

		public boolean vsync = false;

		public static final LaunchArgs deserialize(String[] args) {
			LaunchArgs argsObject = new LaunchArgs();
			StringBuilder logMessage = new StringBuilder().append("Deserialized launch args:\n\t");
			ArrayList<String> unsorted = new ArrayList<String>();

			for(int i = 0; i < args.length; i++) {
				String arg = args[i];

				Field field = null;
				if(arg.startsWith("--")) {
					try {
						field = LaunchArgs.class.getDeclaredField(arg.substring(2));
					} catch(NoSuchFieldException e) {continue;}
				} else if(arg.startsWith("-") && arg.length() == 2) {
					for(Field f : LaunchArgs.class.getDeclaredFields()) {
						Shortened shortened = f.getAnnotation(Shortened.class);
						if(shortened != null) {
							char c = shortened.value();
							if(c == arg.charAt(1) || Character.toUpperCase(c) == arg.charAt(1)) {
								field = f;
								break;
							}
						}
					}
				}

				if(field != null) {
					try {
						if(field.getType().equals(boolean.class)) {
							field.set(argsObject, true);
							logMessage.append("Flag ").append(field.getName()).append(" set\n\t");
						} else if(field.getType().equals(String.class)) {
							field.set(argsObject, args[++i]);
							logMessage.append("Value ").append(field.getName()).append(" set to ").append(args[i]).append("\n\t");
						} else if(field.getType().equals(int.class)) {
							field.set(argsObject, Integer.parseInt(args[++i]));
							logMessage.append("Value ").append(field.getName()).append(" set to ").append(args[i]).append("\n\t");
						}
					} catch(IllegalAccessException e) {}
				} else {
					unsorted.add(arg);
				}
			}
			argsObject.unsorted = unsorted.toArray(new String[unsorted.size()]);
			argsObject.init();

			logMessage.setLength(logMessage.length() - 1);
			logger.info(logMessage.toString());
			return argsObject;
		}

		public void init() {
			this.size = new Vec2(this.width, this.height);
			if(this.monitor.equalsIgnoreCase("primary")) {
				try {
					this.monitorId = Integer.parseInt(this.monitor);
				} catch(NumberFormatException e) {}
			}
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private static @interface Shortened {
		public char value();
	}
}
