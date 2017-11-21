package datasets;

import java.util.ArrayList;
import java.util.Iterator;

@Data(id=0, instance="instance")
public class DataObject extends IData {
	private final ArrayList<NamedData> children = new ArrayList<NamedData>();

	public DataObject addChild(String name, IData child) {
		this.children.add(new NamedData(name, child));
		return this;
	}
	
	public int getCount() {
		return this.children.size();
	}

	public DataObject removeChild(String name) {
		return removeChild(name, IData.class);
	}
	public DataObject removeChild(String name, Class<? extends IData> type) {
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			NamedData namedData = it.next();
			if(namedData.matches(name, type)) {
				it.remove();
				return this;
			}
		}
		throw new IllegalArgumentException("name must be a valid name identifier");
	}

	public boolean hasChild(String name) {
		return this.hasChild(name, IData.class);
	}
	public boolean hasChild(String name, Class<? extends IData> type) {
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			if(it.next().matches(name, type)) return true;
		}
		return false;
	}

	public NamedData getChild(int i) {
		return this.children.get(i);
	}
	public IData getChild(String name) {
		return this.getChild(name, IData.class);
	}
	public <T extends IData> T getChild(String name, Class<T> type) {
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			NamedData namedData = it.next();
			if(namedData.matches(name, type)) return type.cast(namedData.getData());
		}
		throw new IllegalArgumentException("name must be a valid name identifier");
	}

	public IData read(byte[] bytes) {
		int readPos = 0;
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			IData data = it.next().getData();
			int size = data.getLength(bytes, readPos);
			data.read(trim(bytes, readPos, readPos + size));
			readPos += size;
		}
		return this;
	}

	public byte[] write() {
		byte[] parentData = new byte[this.getLength()];

		int writePos = 0;
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			IData data = it.next().getData();
			parentData = untrim(parentData, data.write(), writePos);
			writePos += data.getLength();
		}
		return parentData;
	}

	public int getLength() {
		int size = 0;
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			size += it.next().getData().getLength();
		}
		return size;
	}
	public int getLength(byte[] data, int start) {
		int size = 0;
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			size += it.next().getData().getLength(data, start + size);
		}
		return size;
	}

	@Override
	public String toString() {
		String s = "{";
		Iterator<NamedData> it = this.children.iterator();
		while(it.hasNext()) {
			s += it.next().toString() + ", ";
		}
		if(s.length() > 1) s = s.substring(0, s.length() - 2);
		s += "}O";
		return s;
	}

	public static DataObject instance() {
		return new DataObject();
	}
}
