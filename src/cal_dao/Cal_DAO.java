package cal_dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cal_service.MemberMain;

public class Cal_DAO{
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String ur1 = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "c##java";
	private String password = "1234";
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	MemberMain memberMain = new MemberMain();
	
	public Cal_DAO() {
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
				Update();
			else if(num2 == 2) 
				delete();
			else if(num2 == 3)
				break;
			else
				System.out.println("잘못된 번호입니다. 1, 2, 3번 중에서 다시 입력하세요.");
		} //2. 일정 검색 및 변경 눌렀을 때 나오는 메뉴의 while문
		
	} //handleDate
	
	private void displaySchedules(String CalDate) {
		getConnection();
		String sql = "select num, content from Calendar where CalDate = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, CalDate);
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

	public void Update() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("변경할 일정을 입력하세요 : ");  
		//일정 변경 하기 전에 날짜를 입력했잖아요? 그럼 여기서 날짜 입력하지 않고 번호만 비교해서 수정하는 방법...가능할까요...?
		String date = sc.next();
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
			pstmt.setString(3, date);
			
			int su = pstmt.executeUpdate();
			
			if(su > 0)
				System.out.println("수정이 완료되었습니다.");
			else {
				System.out.println("일정이 없습니다.");
				memberMain.Membermenu();  //이거 일정이 없으니까 다시 멤버메뉴로 돌아가서 일정 입력하라는 의도입니다. 근데...맞을라나..??
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
		
	public void delete() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("날짜를 입력하세요 : ");
		String date = sc.next();
		System.out.print("삭제할 일정의 번호를 입력하세요 : ");
		int num = sc.nextInt();
		System.out.print("정말 삭제 하시겠습니까? (Y/N) : ");
		try {
			char confirm = (char) System.in.read();
			System.in.read();
			if(confirm == 'Y' || confirm == 'y') {
				getConnection();
				String sql = "delete from Calendar where calDate = ? AND num = ?";
				
				try {
					pstmt = con.prepareStatement(sql);
					
					pstmt.setString(1, date);
					pstmt.setInt(2, num);
					
					int su = pstmt.executeUpdate();
					
					if(su > 0) {
						  System.out.println("일정이 삭제되었습니다.");
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

