package datasets;

import java.util.ArrayList;

@Data(id=6, instance="instance")
public class DataIntArray extends IData {
	ArrayList<DataInt> values = new ArrayList<DataInt>();
	byte size;

	@Override
	public IData read(byte[] bytes) {
		int readPos = 1;
		this.values.clear();
		for(int i = 0; i < this.size; i++) {
			DataInt data = new DataInt();
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
		int writePos = 1;
		for(int i = 0; i < this.size; i++) {
			DataInt dataInt = values.get(i);
			int size = dataInt.getLength();
			data = untrim(data, dataInt.write(), writePos);
			writePos += size;
		}
		return data;
	}

	@Override
	public int getLength() {
		return this.size * new DataInt().getLength() + 1;
	}

	@Override
	public int getLength(byte[] bytes, int start) {
		this.size = bytes[start];
		return this.size * new DataInt().getLength() + 1;
	}
	
	public int getCount() {
		return this.size;
	}
	public int get(int i) {
		return this.values.get(i).get();
	}
	public DataIntArray set(int i, int value) {
		values.set(i, new DataInt().set(value));
		this.updateSize();
		return this;
	}
	public DataIntArray push(int value) {
		values.add(new DataInt().set(value));
		this.updateSize();
		return this;
	}
	public DataIntArray pop() {
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
		for(int i = 0; i < this.size; i++) {
			s += this.values.get(i).toString() + ", ";
		}
		if(s.length() > 1) s = s.substring(0, s.length() - 2);
		s += "]IA";
		return s;
	}

	public static DataIntArray instance() {
		return new DataIntArray();
	}
}
