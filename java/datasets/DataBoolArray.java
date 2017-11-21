package datasets;

import java.util.ArrayList;

@Data(id=9, instance="instance")
public class DataBoolArray extends IData {
	ArrayList<Boolean> values = new ArrayList<Boolean>();
	/** How many booleans this array holds */
	byte size;

	@Override
	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int readPos = 0;
		this.values.clear();
		for(int i = 0; i < this.size; i++) {
			if(i % 8 == 0) readPos++;
			boolean b = (bytes[readPos] >> (i % 8) & 1) == 1;
			this.values.add(b);
		}
		return this;
	}

	@Override
	public byte[] write() {
		byte[] data = new byte[this.getLength()];
		data[0] = this.size;
		int writePos = 0;
		for(int i = 0; i < this.size; i++) {
			if(i % 8 == 0) writePos++;
			data[writePos] |= (this.values.get(i) ? 1 : 0) << (i % 8);
		}
		return data;
	}

	@Override
	public int getLength() {
		return (int) (Math.ceil((double) this.size / 8) + 1);
	}

	@Override
	public int getLength(byte[] bytes, int start) {
		this.size = bytes[start];
		return (int) (Math.ceil((double) this.size / 8) + 1);
	}

	public int getCount() {
		return this.size;
	}
	public boolean get(int i) {
		return this.values.get(i);
	}
	public DataBoolArray set(int i, boolean value) {
		values.set(i, value);
		this.updateSize();
		return this;
	}
	public DataBoolArray push(boolean value) {
		values.add(value);
		this.updateSize();
		return this;
	}
	public DataBoolArray pop() {
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
		s += "]BNS";
		return s;
	}

	public static DataBoolArray instance() {
		return new DataBoolArray();
	}
}
