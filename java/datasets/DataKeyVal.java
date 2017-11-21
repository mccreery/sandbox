package datasets;

@Data(id=7, instance="instance")
public class DataKeyVal extends IData {
	public String name;
	byte nameLength;
	short dataId;
	public IData data;
	
	public DataKeyVal setName(String name) {
		this.name = name;
		this.nameLength = (byte) name.length();
		return this;
	}
	public DataKeyVal setData(IData data) {
		this.data = data;
		this.dataId = IData.getIDFromType(data.getClass());
		return this;
	}

	@Override
	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int readPos = 3;
		char[] name = new char[this.nameLength];
		for(int i = 0; i < name.length; i++) {
			name[i] = (char) bytes[readPos];
			readPos++;
		}
		this.name = String.valueOf(name);
		int size = this.data.getLength();
		this.data.read(trim(bytes, readPos, readPos + size));
		return this;
	}

	@Override
	public byte[] write() {
		int writePos = 3;
		byte[] bytes = new byte[this.getLength()];
		bytes[0] = this.nameLength;
		bytes = untrim(bytes, new DataShort().set(this.dataId).write(), 1);
		for(int i = 0; i < name.length(); i++) {
			bytes[writePos] = (byte) name.charAt(i);
			writePos++;
		}
		bytes = untrim(bytes, this.data.write(), writePos);
		return bytes;
	}

	@Override
	public int getLength() {
		return 3 + this.nameLength + this.data.getLength();
	}

	@Override
	public int getLength(byte[] bytes, int start) {
		this.nameLength = bytes[start];
		DataShort id = new DataShort();
		id.read(trim(bytes, start + 1, start + 3));
		this.dataId = id.get();
		this.data = IData.createInstance(this.dataId);
		return 3 + this.nameLength + this.data.getLength(bytes, start + 3 + this.nameLength);
	}

	@Override
	public String toString() {
		return "(\"" + this.name + "\":" + this.data.toString() + ")KV";
	}

	public static DataKeyVal instance() {
		return new DataKeyVal();
	}

	public boolean matches(String name, Class<? extends IData> type) {
		return this.name.equals(name) && this.data.getClass().equals(type);
	}
}
