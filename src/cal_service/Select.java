package cal_service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
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
		
		String calDate = null;
		System.out.print("\t날짜를 입력하세요(YYYY-MM-DD) : ");
		try {
			calDate = sc.nextLine();
			
			calDAO.displaySchedules(calDate);
			
		}catch(InputMismatchException e) {
			//e.printStackTrace();
			System.out.println();
			System.out.println("-- 형식에 맞게 입력하세요. --");
			System.out.println();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //calDate를 date형식으로 바꿈
		Date inputDate = null;
			
		try {
			inputDate = sdf.parse(calDate);
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
			handleDate(calDate);
		}
	} //execute()
	
	
	
	public void handleDate(String CalDate) {
		
		while(true) {
			System.out.println();
			System.out.println("\t<일정 수정 메뉴>");
			System.out.println("**********************");
			System.out.println("\t1. 일정 변경");
			System.out.println("\t2. 일정 삭제");
			System.out.println("\t3. 메인 화면");
			System.out.println("**********************");
			System.out.print("\t번호 입력 : ");
			
			int num = -1;
			
			try {
				 num = sc.nextInt();
				 
				if(num == 1)
					Update(CalDate); 
				else if(num == 2) 
					Delete(CalDate);
				else if(num == 3)
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
		} //2. 일정 검색 및 변경 눌렀을 때 나오는 메뉴의 while문                                
		
	} //handleDate

	public void Update(String CalDate) {
		System.out.print("\t변경할 일정을 입력하세요 : ");
	    int scheduleNum = sc.nextInt();
	    sc.nextLine();  
	    System.out.print("\t수정할 일정의 내용을 입력하세요 : ");
	    String newContent = sc.nextLine();

	    calDAO.Update(newContent, scheduleNum, CalDate);
		
		
	} //Update()	
		
	public void Delete(String CalDate) {
		
	    System.out.print("\t삭제할 일정의 번호를 입력하세요 : ");
	    int scheduleNum = sc.nextInt();

	    System.out.print("\t정말 삭제 하시겠습니까? (Y/N) : ");
	    char confirm = sc.next().charAt(0);

	    if (confirm == 'Y' || confirm == 'y') {
	    	calDAO.Delete(scheduleNum, CalDate);
	    } else {
	        System.out.println("-- 삭제를 취소합니다. --");
	    }
	}
		
	}//delete()


