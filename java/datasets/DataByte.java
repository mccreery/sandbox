package datasets;

@Data(id=1, instance="instance")
public class DataByte extends IData {
	private byte value;
	
	@Override
	public IData read(byte[] bytes) {
		this.value = bytes[0];
		return this;
	}

	@Override
	public byte[] write() {
		return new byte[] {value};
	}

	@Override
	public int getLength() {
		return Byte.SIZE / 8;
	}

	public byte get() {
		return this.value;
	}
	public DataByte set(byte value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(value) + "B";
	}

	public static DataByte instance() {
		return new DataByte();
	}
}
