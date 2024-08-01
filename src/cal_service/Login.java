package cal_service;

import java.util.Scanner;

import cal_dao.Cal_DAO;

public class Login implements service {
	@Override
	public void execute() {
		Scanner sc = new Scanner(System.in);
		Cal_DAO cal_DAO = new Cal_DAO();
		
		while(true) {
			System.out.print("\t아이디 : ");
			String id = sc.next();
			System.out.print("\t비밀번호 : ");
			String pwd = sc.next();
			String name = cal_DAO.LoginCal(id, pwd);
			if(name == null) {
				System.out.println("-- 아이디 또는 비밀번호가 일치하지 않습니다.\n다시 입력해주세요. --");
				System.out.println();
			}else {
				System.out.println();
				System.out.println("-- " + name + "님 환영합니다. --");
				cal_DAO.Mainmenu(id);
				break;
			}
		}//while
	}
}