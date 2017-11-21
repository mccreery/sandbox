package datasets;

@Data(id=10, instance="instance")
public class DataWrapper extends IData {
	private short dataId;
	private IData data;
	
	public DataWrapper setData(IData data) {
		this.data = data;
		this.dataId = IData.getIDFromType(data.getClass());
		return this;
	}

	public IData getData() {
		return this.data;
	}
	public short getType() {
		return this.dataId;
	}

	@Override
	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int size = this.data.getLength();
		this.data.read(trim(bytes, 2, 2 + size));
		return this;
	}

	@Override
	public byte[] write() {
		byte[] bytes = new byte[this.getLength()];
		bytes = untrim(bytes, new DataShort().set(this.dataId).write(), 0);
		bytes = untrim(bytes, this.data.write(), 2);
		return bytes;
	}

	@Override
	public int getLength() {
		return 2 + this.data.getLength();
	}

	@Override
	public int getLength(byte[] bytes, int start) {
		DataShort id = new DataShort();
		id.read(trim(bytes, start, start + 2));
		this.dataId = id.get();
		this.data = IData.createInstance(this.dataId);
		return 2 + this.data.getLength(bytes, start + 2);
	}

	@Override
	public String toString() {
		return "(" + this.data.toString() + ")W";
	}

	public static DataWrapper instance() {
		return new DataWrapper();
	}
}
