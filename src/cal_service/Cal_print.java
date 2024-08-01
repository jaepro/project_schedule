package cal_service;

import java.text.ParseException;

import cal_dao.Cal_DAO;

public class Cal_print implements service {

	@Override
	public void execute() {
		String id = null;
		Cal_DAO dao = new Cal_DAO();
		try {
			dao.Calenderprint(id);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
