package calendar;

import cal_dao.Cal_DAO;

public class cal_main {	

	public static void main(String[] args) {

		Cal_DAO cal_DAO = new Cal_DAO();
		cal_DAO.menu();
		System.out.println("-- 시스템이 종료되었습니다. --");
	}

}
