package cal_service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import cal_dao.Cal_DAO;

public class Select implements service {
	private Cal_DAO calDAO;
	private String id;
	Scanner sc = new Scanner(System.in);
	
	public Select(String id) {
		calDAO = new Cal_DAO();
		this.id = id;
	}
	
	@Override
	public void execute() {
		
		String calDate = null;
		String db_calDate = null;
		
		while(true) {
			System.out.print("\t날짜를 입력하세요(yyyymmdd) : ");
			calDate = sc.next();
			
			 if (calDate.length() == 8 && calDate.matches("\\d+")) {
			        try {
			            // 입력된 문자열을 LocalDate로 변환
			            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			            LocalDate date = LocalDate.parse(calDate, formatter);
			            
			         // 연도 제한 (1990년부터 현재 연도 + 30년까지만 허용)
			            int year = date.getYear();
			            int currentYear = LocalDate.now().getYear();
			            if (year < 1990 || year > currentYear + 30) {
			                System.out.println();
			                System.out.println("-- 연도는 1990년부터 " + (currentYear + 30) + "년까지만 입력 가능합니다. 다시 입력하세요. --");
			                System.out.println();
			                continue;
			            }
			            
			            db_calDate = date.toString(); // yyyy-MM-dd 형식으로 자동 변환
			            
			            System.out.println();
			            calDAO.displaySchedules(db_calDate, id);
			            break;
			            
			        } catch (DateTimeParseException e) {
			            // 유효하지 않은 날짜일 경우 예외 발생
			            System.out.println();
			            System.out.println("-- 유효하지 않은 날짜입니다. 다시 입력하세요. --");
			            System.out.println();
			        }
			    } else {
			        System.out.println();
			        System.out.println("-- 8자리 숫자를 제대로 입력하세요. --");
			        System.out.println();
			    }
        } //while
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //calDate를 date형식으로 바꿈
		Date inputDate = null;
			
		try {
			inputDate = sdf.parse(db_calDate);
		} catch (ParseException e) {
					
			e.printStackTrace();
		} 
		
		//현재 시스템의 날짜를 가져옴
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);// 시스템의 시간을 date 저장
		
		calendar.add(Calendar.DATE, -1);
			    
		Date day = calendar.getTime();  //gettime으로 가져와ㅣ서 day에 넣음
		
		//calDate랑 sysDate랑 비교  sysDate > sdf => 다시 "날짜를 입력하라는 창이 나오게
		if (inputDate.before(day)) {// inputDate가 sysDate보다 작으면 TRUE 리턴
			System.out.println();
			System.out.println("-- 입력한 날짜는 지났으므로 수정이 불가합니다. --");
		} else {
		        // 입력된 날짜가 현재 날짜와 같거나 이후일 경우에만 handleDate 실행
			handleDate(db_calDate);
		}
	} //execute()
	
	
	
	public void handleDate(String db_calDate) {
		
		while(true) {
			System.out.println();
			System.out.println("\t<일정 수정 메뉴>");
			System.out.println("**********************");
			System.out.println("\t1. 일정 변경");
			System.out.println("\t2. 날짜 변경");
			System.out.println("\t3. 일정 삭제");
			System.out.println("\t4. 메인 화면");
			System.out.println("**********************");
			System.out.print("\t번호 입력 : ");
			
			int num = -1;
			
			try {
				 num = sc.nextInt();
				 
				if(num == 1) {
					calDAO.Update(db_calDate, id); 
					break;
				}
				else if(num == 2) {
					calDAO.UpdateDate(db_calDate, id); 
					break;
				}
				else if(num == 3) {
					calDAO.Delete(db_calDate, id); 
					break;
				}
				else if(num == 4)
					break;
				else {
					System.out.println();
					System.out.println("-- 메뉴 중에서 다시 선택하세요. --");
					System.out.println();
					continue;
				}
				
			}catch(InputMismatchException e) {
				//e.printStackTrace();
				System.out.println();
				System.out.println("-- 숫자 형식으로 입력하세요 --");
				System.out.println();
				sc.nextLine(); //남아있는 버퍼를 지울려고 작성함
			}
		}                             
	}		
}


