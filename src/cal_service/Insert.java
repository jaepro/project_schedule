package cal_service;

import java.util.Scanner;

import cal_bean.Cal_DTO;
import cal_dao.Cal_DAO;

public class Insert implements service {
   private String id;
   private int num;

    public Insert(String id) {
        this.id = id;
        this.num = 1;
    }
    
	public void execute() {
		Scanner sc = new Scanner(System.in);
		Cal_DAO cal_DAO = new Cal_DAO();
		Cal_DTO cal_DTO = new Cal_DTO();
		
			
		
		System.out.print("날짜를 입력하세요 : ");
		String calDate = sc.next();
		System.out.println("일정을 입력하세요");
		String content = sc.next();
		
		cal_DTO.setId(id);
		cal_DTO.setNum(num);
		cal_DTO.setContent(content);
		cal_DTO.setCalDate(calDate);
		
		int result = cal_DAO.getInsert(cal_DTO);
		if(result>0) {
			System.out.println("일정이 추가되었습니다.");
			num++;
		}else System.out.println("일정 추가에 실패하였습니다.");
	}
	
}