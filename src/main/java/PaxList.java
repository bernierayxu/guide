//This is a class for storing pas infomation
public class PaxList {
	private String tourGuide;
	private String groupNum;
	private String conNum;
	private String paxNum;
	private String pickLoc;
	private String phoneNum;
	private String tourName;

	PaxList(String tourGuide, String groupNum, String conNum, String paxNum, String pickLoc, String phoneNum, String tourName){
		this.tourGuide = tourGuide;
		this.groupNum = groupNum;
		this.conNum = conNum;
		this.paxNum = paxNum;
		this.pickLoc = pickLoc;
		this.phoneNum = phoneNum;
		this.tourName = tourName;
	}

	public String getTourGuide(){
		return tourGuide;
	}

	public String getGroupNum(){
		return groupNum;
	}

	public String getConNum(){
		return conNum;
	}

	public String getPaxNum(){
		return paxNum;
	}

	public String getPickLoc(){
		return pickLoc;
	}

	public String getPhoneNum(){
		return phoneNum;
	}

	public String getTourName(){
		return tourName;
	}
}
