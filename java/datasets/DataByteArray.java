package datasets;

import java.util.ArrayList;

@Data(id=5, instance="instance")
public class DataByteArray extends IData {
	ArrayList<DataByte> values = new ArrayList<DataByte>();
	byte size;

	@Override
	public IData read(byte[] bytes) {
		byte readPos = 1;
		this.values.clear();
		for(byte i = 0; i < this.size; i++) {
			DataByte data = new DataByte();
			int size = data.getLength();
			data.read(trim(bytes, readPos, readPos + size));
			this.values.add(data);
			readPos += size;
		}
		return this;
	}

	@Override
	public byte[] write() {
		byte[] data = new byte[this.getLength()];
		data[0] = this.size;
		byte writePos = 1;
		for(byte i = 0; i < this.size; i++) {
			DataByte dataByte = values.get(i);
			int size = dataByte.getLength();
			data = untrim(data, dataByte.write(), writePos);
			writePos += size;
		}
		return data;
	}

	@Override
	public int getLength() {
		return this.size * new DataByte().getLength() + 1;
	}

	@Override
	public int getLength(byte[] bytes, int start) {
		this.size = bytes[start];
		return this.size * new DataByte().getLength() + 1;
	}
	
	public byte getCount() {
		return this.size;
	}
	public byte get(byte i) {
		return this.values.get(i).get();
	}
	public DataByteArray set(byte i, byte value) {
		values.set(i, new DataByte().set(value));
		this.updateSize();
		return this;
	}
	public DataByteArray push(byte value) {
		values.add(new DataByte().set(value));
		this.updateSize();
		return this;
	}
	public DataByteArray pop() {
		values.remove(values.size() - 1);
		this.updateSize();
		return this;
	}
	
	public void updateSize() {
		this.size = (byte) this.values.size();
	}
	
	@Override
	public String toString() {
		String s = "[";
		for(byte i = 0; i < this.size; i++) {
			s += this.values.get(i).toString() + ", ";
		}
		s = s.substring(0, s.length() - 2) + "]BA";
		return s;
	}

	public static DataByteArray instance() {
		return new DataByteArray();
	}
}
