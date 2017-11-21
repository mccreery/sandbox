package datasets;

public class NamedData {
	private String name;
	private IData data;

	public NamedData(String name, IData data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return this.name;
	}
	public IData getData() {
		return this.data;
	}
	public Class<? extends IData> getType() {
		return this.data.getClass();
	}
	
	public boolean matches(String name, IData data) {
		return this.matches(name, data.getClass()) && data.equals(this.data);
	}
	public boolean matches(String name, Class<? extends IData> type) {
		return this.getName().equals(name) && this.getType().equals(type);
	}

	@Override
	public String toString() {
		return "\"" + this.getName() + "\":" + this.getData().toString();
	}
}
