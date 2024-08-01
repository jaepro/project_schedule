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
		String birth;
		System.out.println();
		System.out.println("\t<회원가입>");
		System.out.println();
		System.out.print("\t이름 : ");
		String name = sc.next();
		
		while(true) {
			System.out.print("\t생년월일 6자리 (YYMMDD) : ");
			birth = sc.next();
			if (birth.matches("\\d{6}")) { // 6자리 확인
                int year = Integer.parseInt(birth.substring(0, 2));
                int month = Integer.parseInt(birth.substring(2, 4));
                int day = Integer.parseInt(birth.substring(4, 6));
                
                if (month >= 1 && month <= 12 && day >= 1 && day <= 31) { // 월과 일의 유효한 값 확인
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                        System.out.println("형식에 맞게 입력해주세요. (해당 월에는 30일까지 있습니다.)");
                    } else {
                        break; // 올바른 날짜인 경우 루프 종료
                    }
                } else {
                    System.out.println("형식에 맞게 입력해주세요. (유효한 월과 일을 입력해주세요.)");
                }
            } else {
                System.out.println("형식에 맞게 입력해주세요. (6자리 숫자로 입력해주세요.)");
            }
		}
		while(true){//while -- 아이디 중복 확인
			System.out.print("\t아이디 : ");
			id = sc.next();
			boolean exist = cal_DAO.isExistId(id);
			if(exist) System.out.println("-- 사용중인 아이디 입니다. --");
			else {
				System.out.println("-- 사용 가능한 아이디 입니다. --"); 
				break;
			}
		}//while -- 아이디 중복 확인
		
		System.out.print("\t비밀번호 : ");
		String pwd = sc.next();
		
		System.out.println();
	
		cal_DTO.setName(name);
		cal_DTO.setBirth(birth);
		cal_DTO.setId(id);
		cal_DTO.setPwd(pwd);
		
		int no = cal_DAO.Signup(cal_DTO);

        /*
		if (no > 0) {
            System.out.println("회원가입이 완료되었습니다.");
        } else {
            System.out.println("회원가입에 실패했습니다.");
        }
        System.out.println();
        */
		//윤년 계산과 2월에 조건문은 안넣었음.
		//회원가입시 현재날짜 기준으로 넘어가는 날짜는 가입하지 못하게는 못했음.
		
	}
}

