package cal_service;

import java.util.Scanner;
import cal_dao.Cal_DAO;

public class Select implements service {
	private Cal_DAO calDAO;
	Scanner sc = new Scanner(System.in);
	
	public Select() {
		calDAO = new Cal_DAO();
	}
	
	@Override
	public void execute() {
		
		System.out.print("날짜를 입력하세요(YYYY-MM-DD) : ");
		String calDate = sc.nextLine();
		
		calDAO.displaySchedules(calDate);
        handleDate(calDate);
	}
	
	
	
	public void handleDate(String CalDate) {
		
		while(true) {
			System.out.println();
			System.out.println("\t일정 수정 메뉴");
			System.out.println("\t1. 일정 변경");
			System.out.println("\t2. 일정 삭제");
			System.out.println("\t3. 메인 화면");
			System.out.println("**********************");
			System.out.print("/t번호 입력 : ");
			int choice = sc.nextInt();
			
			if(choice == 1)
				Update(CalDate);
			else if(choice == 2) 
				Delete(CalDate);
			else if(choice == 3)
				break;
			else
				System.out.println("잘못된 번호입니다. 1, 2, 3번 중에서 다시 입력하세요.");
		} //2. 일정 검색 및 변경 눌렀을 때 나오는 메뉴의 while문
		
	} //handleDate

	public void Update(String CalDate) {
		System.out.print("변경할 일정을 입력하세요 : ");
	    int scheduleNum = sc.nextInt();
	    sc.nextLine();  // Consume newline
	    System.out.print("수정할 일정의 내용을 입력하세요 : ");
	    String newContent = sc.nextLine();

	    calDAO.Update(newContent, scheduleNum, CalDate);
		
		
	} //Update()	
		
	public void Delete(String CalDate) {
		
	    System.out.print("삭제할 일정의 번호를 입력하세요 : ");
	    int scheduleNum = sc.nextInt();

	    System.out.print("정말 삭제 하시겠습니까? (Y/N) : ");
	    char confirm = sc.next().charAt(0);

	    if (confirm == 'Y' || confirm == 'y') {
	    	calDAO.Delete(scheduleNum, CalDate);
	    } else {
	        System.out.println("Deletion canceled.");
	    }
	}
		
	}//delete()


