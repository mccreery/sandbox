package nukeduck.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOUtil {
	public static ByteBuffer readFile(ResourcePath resource) throws IOException {
		return readFile(resource.getFile());
	}
	public static ByteBuffer readFile(File file) throws IOException {
		FileInputStream fileStream = new FileInputStream(file);
		FileChannel fileChannel = fileStream.getChannel();
		long fileSize = fileChannel.size();

		ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

		fileChannel.read(buffer);
		buffer.rewind();
		fileChannel.close(); 
		fileStream.close();

		return buffer;
	}
}
