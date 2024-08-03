package cal_service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
		Date date2 = new Date();
		Calendar cal = Calendar.getInstance();
		LocalDate date = LocalDate.now();
		Cal_DAO cal_DAO = new Cal_DAO();
		Cal_DTO cal_DTO = new Cal_DTO();
		String calDate;
		
		cal.setTime(date2);
		cal.add(Calendar.YEAR, 30);
		int getYear = cal.get(Calendar.YEAR);
		int getLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int getFirstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		
			
		while(true) {
			System.out.print("\t날짜 8자리를 입력하세요 (YYYYMMDD) : ");
			calDate = sc.next(); 
			
			if (calDate.matches("\\d{8}")) {
				int year = Integer.parseInt(calDate.substring(0, 4));
            	int month = Integer.parseInt(calDate.substring(4, 6));
            	int day = Integer.parseInt(calDate.substring(6, 8));
            	if (year < date.getYear() ||
                   (year <= date.getYear() && month < date.getMonthValue()) ||
                   (year <= date.getYear() && month <= date.getMonthValue()) && day < date.getDayOfMonth()) {System.out.println("\t지난 날짜에는 입력이 불가합니다."); continue;}
                   //현재 날짜 기준으로 넘어가는 날짜는 가입불가.
            	if(year >= 1900 && getYear > year) { // 입력한 연도가 관리자가 설정한 연도안에 들어오는지 확인
            		if(month > 0 && month < 13) { // 입력한 월이 1~12 중에 선택되었는지 확인
            			if(day < getLastDay && day > getFirstDay) break; // 입력한 날짜가 해당 달에 있는 날짜인지 확인
            			else {System.out.println("\t유효한 일을 입력해주세요.");continue;}
            		}else {System.out.println("\t유요한 월을 입력해주세요.");continue;}
            	}else {System.out.println("\t유효한 년도를 입력해주세요.");continue;}
			}else {System.out.println("\t형식에 맞게 입력해주세요.(8자리 숫자로 입력해주세요.)");}
		}
		
		
		
		cal_DTO.setId(id);
		cal_DTO.setNum(num);
		cal_DTO.setCalDate(fomattedCalDate(calDate));
		
		int result = cal_DAO.getInsert(cal_DTO);
		if(result>0) {
			System.out.println("-- 일정이 추가되었습니다. --");
		}else System.out.println("-- 일정 추가에 실패하였습니다. --");
		
		//현재날짜 기준으로 지난 날짜들에 일정추가하는건 못막음.
	}
	
}