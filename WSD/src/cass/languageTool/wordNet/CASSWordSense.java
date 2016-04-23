package cass.languageTool.wordNet;

public class CASSWordSense {
	
	private String full_id;
	private String target;
	private String part_of_speech;
	
	public CASSWordSense(String target, String id, String pos) {
		this.target = target;
		this.full_id = id;
		this.part_of_speech = pos;
	}

	public String getTarget() {
		return target;
	}
	
	public String getId() {
		return full_id;
	}
	
	public String getPOS() {
		return part_of_speech;
	}
}
