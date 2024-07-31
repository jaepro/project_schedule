package cal_bean;

public class Cal_DTO {
	private String name, id, pwd, birth, content, calDate; //사용자 정보 변수
	private int year, month, week, lastday, num;  //달력 만들 때 필요한 변수
	
	public Cal_DTO(String name, String id, String pwd, String birth) {
		this.name = name;
		this.id = id;
		this.pwd = pwd;
		this.birth = birth;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getContent() {
		return content;
	}
	public String getCalDate() {
		return calDate;
	}
	public void setCalDate(String calDate) {
		this.calDate = calDate;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getLastday() {
		return lastday;
	}
	public void setLastday(int lastday) {
		this.lastday = lastday;
	}
	
	

}
