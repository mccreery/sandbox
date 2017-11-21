package datasets;

import java.nio.ByteBuffer;

@Data(id=2, instance="instance")
public class DataShort extends IData {
	private short value;

	@Override
	public IData read(byte[] bytes) {
		this.value = ByteBuffer.wrap(bytes).getShort();
		return this;
	}

	@Override
	public byte[] write() {
		ByteBuffer buf = ByteBuffer.allocate(Short.SIZE / 8);
		buf.putShort(this.value);
		return buf.array();
	}

	@Override
	public int getLength() {
		return Short.SIZE / 8;
	}

	public short get() {
		return this.value;
	}
	public DataShort set(short value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(value) + "S";
	}

	public static DataShort instance() {
		return new DataShort();
	}
}
