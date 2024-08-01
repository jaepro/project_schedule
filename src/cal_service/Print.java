package cal_service;

import java.text.ParseException;

import cal_dao.Cal_DAO;

public class Print implements service {
	private String id;
	
	public Print(String id) {
		this.id = id;
	}
	
	@Override
	public void execute() {
		Cal_DAO dao = new Cal_DAO();
		try {
			dao.Calenderprint(id);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
