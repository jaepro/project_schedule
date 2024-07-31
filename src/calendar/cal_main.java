package calendar;

import java.util.Scanner;

import cal_service.service;

public class cal_main {
	
	public void menu() {
		Scanner sc = new Scanner(System.in);
		service service1 = null;
		int num; //번호 입력의 숫자
		
		while(true) {
			System.out.println();
			System.out.println("\t1.회원가입");
			System.out.println("\t2.로그인");
			System.out.println("\t3.달력보기");
			System.out.println("\t4.종료");
			System.out.println("*************************");
			System.out.print("\t번호 입력 : ");
			num = sc.nextInt();
			
			if(num == 4) break;
			//if(num == 1)
				
				
			
			
			
			
		} //while
	} //menu()
	
	

	public static void main(String[] args) {
	
		//달력 출력 이후
		//

	}

}
