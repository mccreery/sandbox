package datasets;

import java.util.ArrayList;
import java.util.Iterator;

@Data(id=11, instance="instance")
public class DataArray extends IData {
	private final ArrayList<DataWrapper> children = new ArrayList<DataWrapper>();
	private int count;

	public DataArray push(IData child) {return this.insert(this.getCount() - 1, child);}
	public DataArray insert(int i, IData child) {
		this.children.add(i, new DataWrapper().setData(child));
		this.updateCount();
		return this;
	}

	public DataArray pop() {return this.remove(this.getCount() - 1);}
	public DataArray remove(int i) {
		this.children.remove(i);
		this.updateCount();
		return this;
	}

	public DataArray clear() {
		this.children.clear();
		this.updateCount();
		return this;
	}

	public IData get(int i) {
		return this.children.get(i).getData();
	}
	public int getCount() {
		return this.count;
	}

	private void updateCount() {
		this.count = this.children.size();
	}

	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int readPos = 4;
		Iterator<DataWrapper> it = this.children.iterator();
		while(it.hasNext()) {
			DataWrapper data = it.next();
			int size = data.getLength();
			data.read(trim(bytes, readPos, readPos + size));
			readPos += size;
		}
		return this;
	}

	public byte[] write() {
		byte[] parentData = new byte[this.getLength()];
		DataInt count = new DataInt().set(this.count);
		parentData = untrim(parentData, count.write(), 0);

		int writePos = 4;
		Iterator<DataWrapper> it = this.children.iterator();
		while(it.hasNext()) {
			DataWrapper data = it.next();
			parentData = untrim(parentData, data.write(), writePos);
			
			writePos += data.getLength();
		}
		return parentData;
	}

	public int getLength() {
		int size = 0;
		Iterator<DataWrapper> it = this.children.iterator();
		while(it.hasNext()) {
			size += it.next().getLength();
		}
		return 4 + size;
	}

	public int getLength(byte[] data, int start) {
		int size = 0;
		DataInt count = new DataInt();
		count.read(trim(data, start, start + 4));
		this.count = count.get();

		this.children.clear();
		for(int i = 0; i < this.count; i++) {
			DataWrapper keyVal = new DataWrapper();
			size += keyVal.getLength(data, start + 4 + size);
			this.children.add(keyVal);
		}
		return 4 + size;
	}

	@Override
	public String toString() {
		String s = "{";
		Iterator<DataWrapper> it = this.children.iterator();
		while(it.hasNext()) {
			s += it.next().toString() + ", ";
		}
		if(s.length() > 1) s = s.substring(0, s.length() - 2);
		s += "}A";
		return s;
	}

	public static DataArray instance() {
		return new DataArray();
	}
}
