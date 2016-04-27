//class for storing information about the relationship between tour and guide
public class TourAndGuide {
	private String tour;
	private String guide;
	private String phone;
	private int direct;

	TourAndGuide(String tour, String guide, String phone, int direct){
		this.tour = tour;
		this.guide = guide;
		this.phone = phone;
		this.direct = direct;
	}

	public String getTourName(){
		return tour;
	}

	public String getGuideName(){
		if(guide.equals("") || guide.equals(null)){
			guide="No Guide";
		}
		return guide;
	}
	public String getGuidePhone(){
		return phone;
	}

	public int getDirect(){
		return direct;
	}
}
