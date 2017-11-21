package datasets;

@Data(id=12, instance="instance")
public class DataBool extends IData {
	private boolean value;

	public boolean get() {
		return this.value;
	}
	public DataBool set(boolean value) {
		this.value = value;
		return this;
	}

	@Override
	public IData read(byte[] bytes) {
		this.value = DataFuncs.toBool(bytes[0]);
		return this;
	}

	@Override
	public byte[] write() {
		return new byte[] {(byte) DataFuncs.toInt(this.value)};
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static DataBool instance() {
		return new DataBool();
	}
}
