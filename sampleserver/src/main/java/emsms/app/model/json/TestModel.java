package emsms.app.model.json;

public class TestModel {
	private String name;
	private String data;
	public TestModel(String name, String data) {
		this.name = name;
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public String getData() {
		return data;
	}
}
