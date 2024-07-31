package cal_service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Select implements service {
	@Override
	public void execute() {
		
	}
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String ur1 = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "c##java";
	private String password = "1234";

	private Connection con;
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	
	//DB
	public Select() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	} //MemberMain()
			
	public void getConnection() {
		try {
			con = DriverManager.getConnection(ur1, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// getConnection
	
	public void Insert() { //상욱씨가 작성한 코드 넣기
		System.out.print("날짜를 입력하세요 : ");
		
		
	}//Insert()
	
	public void handleDate(String CalDate) {
		Scanner sc = new Scanner(System.in);
		
		displaySchedules(CalDate);  //해당 날짜의 일정을 출력하는 메소드
		
		while(true) {
			System.out.println();
			System.out.println("\t일정 수정 메뉴");
			System.out.println("\t1. 일정 변경");
			System.out.println("\t2. 일정 삭제");
			System.out.println("\t3. 메인 화면");
			System.out.println("**********************");
			System.out.print("/t번호 입력 : ");
			int num2 = sc.nextInt();
			
			if(num2 == 1)
				Update(CalDate);
			else if(num2 == 2) 
				delete(CalDate);
			else if(num2 == 3)
				break;
			else
				System.out.println("잘못된 번호입니다. 1, 2, 3번 중에서 다시 입력하세요.");
		} //2. 일정 검색 및 변경 눌렀을 때 나오는 메뉴의 while문
		
	} //handleDate
	
	private void displaySchedules(String CalDate) {
		getConnection();
		String sql = "select num, content from Calendar where CalDate = ?";
		
		String in_CalDate = CalDate; 
		
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, in_CalDate);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getInt("num") + rs.getString("content"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{ 
			try {
				if(rs != null) rs.close(); 
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}//try~catch, finally
		
	}//displaySchedules
	
	public void printCal() {  //세현씨 코드 넣기
		
		
	}//printCal()

	public void Update(String CalDate) {
		
		String in_CalDate = CalDate; 
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("변경할 일정의 번호를 입력하세요 : ");
		int schedule_num = sc.nextInt();
		System.out.print("수정할 일정의 내용을 입력하세요 : ");
		String newContnet = sc.next();
		
		getConnection();
		String sql = "update Calendar set content = ? where num = ? and CALDATE = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, newContnet);
			pstmt.setInt(2, schedule_num);
			pstmt.setString(3, in_CalDate);
			
			int su = pstmt.executeUpdate();
			
			if(su > 0)
				System.out.println("수정이 완료되었습니다.");
			else {
				System.out.println("일정이 없습니다.");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{ 
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}//try~catch, finally
		
		
	} //Update()	
		
	public void delete(String CalDate) {
		
		String in_CalDate = CalDate; 
		Scanner sc = new Scanner(System.in);
		
		System.out.print("삭제할 일정의 번호를 입력하세요 : ");
		int num = sc.nextInt();
		System.out.print("정말 삭제 하시겠습니까? (Y/N) : ");
		try {
			char confirm = (char) System.in.read();
			System.in.read();
			if(confirm == 'Y' || confirm == 'y') {
				getConnection();
				String deletesql = "delete from Calendar where calDate = ? and num = ?";
				String updatesql = "update Calendar set num = num-1 where calDate = ? and num = ?";
				
				try {
					pstmt = con.prepareStatement(deletesql);
					
					pstmt.setString(1, in_CalDate);
					pstmt.setInt(2, num);
					
					int su = pstmt.executeUpdate();
					
					if(su > 0) {
						  System.out.println("일정이 삭제되었습니다.");
						  //---------삭제한 일정 뒤의 번호를 1씩 줄임
						  pstmt2 = con.prepareStatement(updatesql);
						  
						  pstmt2.setString(1, in_CalDate);
						  pstmt2.setInt(2, num);
						  
						  pstmt.executeUpdate();
						  
						  System.out.println("일정 번호가 업데이트되었습니다.");
						  
	                } else {
	                    System.out.println("해당 일정을 찾을 수 없습니다.");
					}		
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}finally{ 
					try {
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
				}//try~catch , finally문
				
				
				
			}else {
	            System.out.println("삭제가 취소되었습니다.");//
			}//if~else문
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //try~catch문
		
	}//delete()

}
