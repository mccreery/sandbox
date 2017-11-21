package nukeduck.crawler.util;

import java.io.File;
import java.nio.file.Paths;

import nukeduck.crawler.util.LegacyFunction.IDelegate;

public class ResourcePath {
	public static final String RESOURCES = "resources";
	public static final String TEXTURES = "textures";

	public String[] parts;

	public ResourcePath(String... parts) {
		this.parts = parts;
	}

	public File getFile() {
		File file = null;
		try {
			file = GET_FILE.invoke((Object) this.parts);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static final LegacyFunction<File> GET_FILE = new LegacyFunction<File>()
		.add(Variables.VERSION_1_7, new IDelegate<File>() {
			public File invoke(Object... args) {
				String[] parts = (String[]) args[0];

				return Paths.get(RESOURCES, parts).toFile();
			}
		})
		.add(0, new IDelegate<File>() {
			public File invoke(Object... args) {
				String[] parts = (String[]) args[0];

				File file = new File(RESOURCES);
				for(String part : parts) {
					file = new File(file, part);
				}
				return file;
			}
		});
}
