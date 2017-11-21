package datasets;

import java.util.ArrayList;
import java.util.Iterator;

@Data(id=8, instance="instance")
public class DataMap extends IData {
	private final ArrayList<DataKeyVal> children = new ArrayList<DataKeyVal>();
	private int count;

	public DataMap addChild(String name, IData child) {
		this.children.add(new DataKeyVal().setName(name).setData(child));
		return this;
	}
	
	public int getCount() {
		return this.children.size();
	}

	public DataMap removeChild(String name) {
		return removeChild(name, IData.class);
	}
	public DataMap removeChild(String name, Class<? extends IData> type) {
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			DataKeyVal namedData = it.next();
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
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			if(it.next().matches(name, type)) return true;
		}
		return false;
	}

	public DataKeyVal getChild(int i) {
		return this.children.get(i);
	}
	public DataKeyVal getChild(String name) {
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			DataKeyVal namedData = it.next();
			if(namedData.name.equals(name)) return namedData;
		}
		throw new IllegalArgumentException("name must be a valid name identifier");
	}

	public IData read(byte[] bytes) {
		getLength(bytes, 0);
		int readPos = 1;
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			DataKeyVal data = it.next();
			int size = data.getLength();
			data.read(trim(bytes, readPos, readPos + size));
			readPos += size;
		}
		return this;
	}
	
	public byte[] write() {
		byte[] parentData = new byte[this.getLength()];
		parentData[0] = (byte) this.children.size();
		
		int writePos = 1;
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			DataKeyVal data = it.next();
			parentData = untrim(parentData, data.write(), writePos);
			
			writePos += data.getLength();
		}
		return parentData;
	}

	public int getLength() {
		int size = 0;
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			size += it.next().getLength();
		}
		return 1 + size;
	}
	
	public int getLength(byte[] data, int start) {
		int size = 0;
		this.count = data[start];
		this.children.clear();
		for(int i = 0; i < this.count; i++) {
			DataKeyVal keyVal = new DataKeyVal();
			size += keyVal.getLength(data, start + 1 + size);
			this.children.add(keyVal);
		}
		return 1 + size;
	}

	@Override
	public String toString() {
		String s = "{";
		Iterator<DataKeyVal> it = this.children.iterator();
		while(it.hasNext()) {
			s += it.next().toString() + ", ";
		}
		if(s.length() > 1) s = s.substring(0, s.length() - 2);
		s += "}M";
		return s;
	}

	public static DataMap instance() {
		return new DataMap();
	}
}
