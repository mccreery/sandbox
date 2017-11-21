package datasets;

import java.nio.ByteBuffer;

@Data(id=3, instance="instance")
public class DataInt extends IData {
	private int value;

	@Override
	public IData read(byte[] bytes) {
		this.value = ByteBuffer.wrap(bytes).getInt();
		return this;
	}

	@Override
	public byte[] write() {
		ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE / 8);
		buf.putInt(this.value);
		return buf.array();
	}

	@Override
	public int getLength() {
		return Integer.SIZE / 8;
	}

	public int get() {
		return this.value;
	}
	public DataInt set(int value) {
		this.value = value;
		return this;
	}

	@Override
	public int getLength(byte[] data, int start) {
		return getLength();
	}

	@Override
	public String toString() {
		return String.valueOf(value) + "I";
	}

	public static DataInt instance() {
		return new DataInt();
	}
}
