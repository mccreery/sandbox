package datasets;

import java.nio.ByteBuffer;

@Data(id=4, instance="instance")
public class DataLong extends IData {
	private long value;

	@Override
	public IData read(byte[] bytes) {
		this.value = ByteBuffer.wrap(bytes).getLong();
		return this;
	}

	@Override
	public byte[] write() {
		ByteBuffer buf = ByteBuffer.allocate(Long.SIZE / 8);
		buf.putLong(this.value);
		return buf.array();
	}

	@Override
	public int getLength() {
		return Long.SIZE / 8;
	}

	public long get() {
		return this.value;
	}
	public DataLong set(long value) {
		this.value = value;
		return this;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value) + "L";
	}

	public static DataLong instance() {
		return new DataLong();
	}
}
