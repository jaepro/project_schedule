package cal_service;

import java.util.Scanner;

import cal_bean.Cal_DTO;
import cal_dao.Cal_DAO;

public class Insert implements service {
   private String id;
   private int num;
   
   private String fomattedCalDate (String calDate) {//날짜값 입력할 때 숫자만 입력해도 알아서 -가 들어가게
	   if(calDate.length() == 8) {
		   int year = Integer.parseInt(calDate.substring(0, 4));
           int month = Integer.parseInt(calDate.substring(4, 6));
           int day = Integer.parseInt(calDate.substring(6, 8));
           
           if(day < 10 && month < 10) return year + "-0" +  + month + "-0" + day;
           else if(month < 10) return year + "-0" +  + month + "-" + day;
           else if(day < 10) return year + "-" +  + month + "-0" + day;
           else return year + "-" +  + month + "-" + day;
	   }
	   return calDate;
   }
   
    public Insert(String id) {
        this.id = id;
    }
    
	public void execute() {
		Scanner sc = new Scanner(System.in);
		Cal_DAO cal_DAO = new Cal_DAO();
		Cal_DTO cal_DTO = new Cal_DTO();
		String calDate;
		
			
		while(true) {
			System.out.print("날짜 8자리를 입력하세요 (YYYYMMDD) : ");
			calDate = sc.next();
			if (calDate.matches("\\d{8}")) { // 8자리 확인
	            int year = Integer.parseInt(calDate.substring(0, 4));
	            int month = Integer.parseInt(calDate.substring(4, 6));
	            int day = Integer.parseInt(calDate.substring(6, 8));
	            
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
	            System.out.println("형식에 맞게 입력해주세요. (8자리 숫자로 입력해주세요.)");
	        }
		}
		
		System.out.print("일정을 입력하세요 : ");
		String content = sc.next();
		
		cal_DTO.setId(id);
		cal_DTO.setNum(num);
		cal_DTO.setContent(content);
		cal_DTO.setCalDate(fomattedCalDate(calDate));
		
		int result = cal_DAO.getInsert(cal_DTO);
		if(result>0) {
			System.out.println("일정이 추가되었습니다.");
		}else System.out.println("일정 추가에 실패하였습니다.");
		
		//현재날짜 기준으로 지난 날짜들에 일정추가하는건 못막음.
	}
	
}