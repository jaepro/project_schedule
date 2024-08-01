package cal_service;

import java.util.Scanner;
import cal_bean.Cal_DTO;
import cal_dao.Cal_DAO;

public class Membership implements service {
	
	public void execute() {
		Scanner sc = new Scanner(System.in);
		Cal_DAO cal_DAO = new Cal_DAO();
		Cal_DTO cal_DTO = new Cal_DTO();
		String id = null;
		System.out.println();
		System.out.println("회원가입");
		
		System.out.print("이름 : ");
		String name = sc.next();
		System.out.print("생년월일 6자리 : ");
		String birth = sc.next();
		while(true){//while -- 아이디 중복 확인
			System.out.print("아이디 : ");
			id = sc.next();
			boolean exist = cal_DAO.isExistId(id);
			if(exist) 
				System.out.println("사용중인 아이디 입니다.");
			else {
				System.out.println("사용 가능한 아이디 입니다.");
				break;
				}
		}//while -- 아이디 중복 확인
		System.out.print("비밀번호 : ");
		String pwd = sc.next();
		
		System.out.println();
	
		cal_DTO.setName(name);
		cal_DTO.setBirth(birth);
		cal_DTO.setId(id);
		cal_DTO.setPwd(pwd);
		
		int no = cal_DAO.Signup(cal_DTO);
        
		if (no > 0) {
            System.out.println("회원가입이 완료되었습니다.");
        } else {
            System.out.println("회원가입에 실패했습니다.");
        }
        System.out.println();
    }
	
}
