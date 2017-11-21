package datasets;

import java.util.ArrayList;
import java.util.Iterator;

@Data(id=13, instance="instance")
public class DataFixedArray extends IData {
	private final ArrayList<IData> children = new ArrayList<IData>();
	private int count;
	private short type;

	public DataFixedArray push(IData child) {
		if(IData.getIDFromType(child.getClass()) != this.getType()) throw new IllegalArgumentException("child ID must be equal to this array's type ID");
		this.children.add(child);
		return this;
	}
	public DataFixedArray pop() {
		this.children.remove(this.children.size() - 1);
		return this;
	}

	public DataFixedArray setType(short type) {
		if(this.getCount() > 0) throw new IllegalArgumentException("array must be empty to change the type ID");
		this.type = type;
		return this;
	}
	public short getType() {
		return this.type;
	}

	public int getCount() {
		return this.children.size();
	}

	public DataFixedArray removeChild(int i) {
		this.children.remove(i);
		return this;
	}
	
	public IData getChild(int i) {
		return this.children.get(i);
	}

	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int readPos = 6;
		Iterator<IData> it = this.children.iterator();
		while(it.hasNext()) {
			IData data = it.next();
			int size = data.getLength();
			data.read(trim(bytes, readPos, readPos + size));
			readPos += size;
		}
		return this;
	}

	public byte[] write() {
		byte[] parentData = new byte[this.getLength()];
		DataInt count = new DataInt().set(this.children.size());
		parentData = untrim(parentData, count.write(), 0);
		DataShort id = new DataShort().set(this.getType());
		parentData = untrim(parentData, id.write(), 4);

		int writePos = 6;
		Iterator<IData> it = this.children.iterator();
		while(it.hasNext()) {
			IData data = it.next();
			parentData = untrim(parentData, data.write(), writePos);

			writePos += data.getLength();
		}
		return parentData;
	}

	public int getLength() {
		int size = 0;
		Iterator<IData> it = this.children.iterator();
		while(it.hasNext()) {
			size += it.next().getLength();
		}
		return 6 + size;
	}
	
	public int getLength(byte[] data, int start) {
		int size = 0;
		DataInt count = new DataInt();
		count.read(trim(data, start, start + 4));
		this.count = count.get();

		this.children.clear();
		DataShort id = new DataShort();
		id.read(trim(data, start + 4, start + 6));
		this.setType(id.get());

		for(int i = 0; i < this.count; i++) {
			IData child = IData.createInstance(this.getType());
			size += child.getLength(data, start + 6 + size);
			this.children.add(child);
		}
		return 6 + size;
	}

	@Override
	public String toString() {
		String s = "{";
		Iterator<IData> it = this.children.iterator();
		while(it.hasNext()) {
			s += it.next().toString() + ", ";
		}
		if(s.length() > 1) s = s.substring(0, s.length() - 2);
		s += "}A";
		return s;
	}

	public static DataFixedArray instance() {
		return new DataFixedArray();
	}
}
