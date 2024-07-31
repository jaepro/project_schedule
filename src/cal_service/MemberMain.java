package cal_service;

import java.security.DrbgParameters.NextBytes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cal_dao.Cal_DAO;

public class MemberMain {

	Cal_DAO cal_DAO = new Cal_DAO();
	
	public void Membermenu() {
		Scanner sc = new Scanner(System.in);
		
		
		while(true) {
			System.out.println();
			System.out.println("\t1. 일정 등록");
			System.out.println("\t2. 일정 검색 및 변경");
			System.out.println("\t3. 달력 보기");
			System.out.println("\t4. 로그 아웃");
			System.out.println("**********************");
			System.out.print("/t번호 입력 : ");
			int num2 = sc.nextInt();
			
			if(num2 == 4) break;
			if(num2 == 1) 
				cal_DAO.Insert(); //상욱씨랑 메소드 명 맞추기
			if(num2 == 2) {
				System.out.print("날짜를 입력하세요(형식 : YYYY-mm-dd) : ");
				String CalDate = sc.next();
				cal_DAO.handleDate(CalDate);				
			}//num2 == 2
			
			if(num2 == 3)
				cal_DAO.printCal(); //세현씨랑 메소드 명 맞추기

		} //while
		
	}//Membermenu()

	
	
	
	
	
	
	
	
		
	

}
