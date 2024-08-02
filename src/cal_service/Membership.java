package cal_service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import cal_bean.Cal_DTO;
import cal_dao.Cal_DAO;

public class Membership implements service {
	
	public void execute() {
		LocalDate date = LocalDate.now();
		Date date2 = new Date();
		Scanner sc = new Scanner(System.in);
		Calendar cal = Calendar.getInstance();
		
		Cal_DAO cal_DAO = new Cal_DAO();
		Cal_DTO cal_DTO = new Cal_DTO();
		
		String id = null;
		String birth;
		
		System.out.println();
		System.out.println("\t<회원가입>");
		System.out.println();
		System.out.print("\t이름 : ");
		String name = sc.next();
		
		cal.setTime(date2);
		cal.add(Calendar.YEAR, 30);
		int getYear = cal.get(Calendar.YEAR);
		int getLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int getFirstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		
		while(true) {
			System.out.print("\t생년월일 8자리 (YYYYMMDD) : ");
			birth = sc.next(); 
			
			if (birth.matches("\\d{8}")) {
				int year = Integer.parseInt(birth.substring(0, 4));
            	int month = Integer.parseInt(birth.substring(4, 6));
            	int day = Integer.parseInt(birth.substring(6, 8));
            	if (year > date.getYear() ||
                   (year >= date.getYear() && month > date.getMonthValue()) ||
                   (year >= date.getYear() && month >= date.getMonthValue()) && day > date.getDayOfMonth()) {
            		System.out.println("\t현재 날짜보다 높은 값을 입력할 수 없습니다."); 
            		continue;
            		}
                   //현재 날짜 기준으로 넘어가는 날짜는 가입불가.
            	if(year >= 1900 && getYear > year) { // 입력한 연도가 관리자가 설정한 연도안에 들어오는지 확인
            		if(month > 0 && month < 13) { // 입력한 월이 1~12 중에 선택되었는지 확인
            			if(day < getLastDay && day > getFirstDay) break; // 입력한 날짜가 해당 달에 있는 날짜인지 확인
            			else {
            				System.out.println("\t유효한 일을 입력해주세요.");
            				continue;
            				}
            		}else {
            			System.out.println("\t유요한 월을 입력해주세요.");
            			continue;
            			}
            	}else {
            		System.out.println("\t유효한 년도를 입력해주세요.");
            		continue;
            		}
			}else {
				System.out.println("\t형식에 맞게 입력해주세요.(8자리 숫자로 입력해주세요.)");
				}
		}//while 생년월일 입력
		
//		while(true) {
//			System.out.print("\t생년월일 8자리 (YYYYMMDD) : ");
//			birth = sc.next();
//			if (birth.matches("\\d{8}")) { // 8자리 확인
//                int year = Integer.parseInt(birth.substring(0, 4));
//                int month = Integer.parseInt(birth.substring(4, 6));
//                int day = Integer.parseInt(birth.substring(6, 8));
//                if (year > date.getYear() ||
//                   (year >= date.getYear() && month > date.getMonthValue()) ||
//                   (year >= date.getYear() && month >= date.getMonthValue()) && day > date.getDayOfMonth()) {System.out.println("\t잘못된 입력입니다."); continue;}
//                //현재 날짜 기준으로 넘어가는 날짜는 가입불가.
//                if (month >= 1 && month <= 12 && day >= 1 && day <= 31) { // 월과 일의 유효한 값 확인
//                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
//                        System.out.println("형식에 맞게 입력해주세요. (해당 월에는 30일까지 있습니다.)");
//                    } else {
//                        break; // 올바른 날짜인 경우 루프 종료
//                    }
//                } else {
//                    System.out.println("형식에 맞게 입력해주세요. (유효한 월과 일을 입력해주세요.)");
//                }
//            } else {
//                System.out.println("형식에 맞게 입력해주세요. (8자리 숫자로 입력해주세요.)");
//            }
//		}
		//if 사용해서 달과 월을 검사함
		
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
		//윤년 계산과 2월에 조건문은 안넣었음.
	}
}

