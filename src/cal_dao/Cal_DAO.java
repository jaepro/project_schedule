package cal_dao;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cal_bean.Cal_DTO;
import cal_service.Insert;
import cal_service.Login;
import cal_service.Membership;
import cal_service.Print;
import cal_service.Select;
import cal_service.service;

public class Cal_DAO{
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String ur1 = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "c##java";
	private String password = "1234";

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//DB
	public Cal_DAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getConnection() {
			try {
				con = DriverManager.getConnection(ur1, username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}// getConnection
//---------------------------------------------------menu
	public void menu() {
		service service = null;
		Scanner sc = new Scanner(System.in);
		//오늘날짜를 인식하여 달력 출력
		while(true) {
			System.out.println("<메뉴>");
			System.out.println("1. 회원가입");
			System.out.println("2. 로그인");
			System.out.println("3. 달력보기");
			System.out.println("4. 종료");
			System.out.print("번호 입력 : ");
			int bunho = sc.nextInt();
			
			if(bunho == 4) break;
			if(bunho == 1) service = new Membership();
			else if(bunho == 2) service = new Login();
			
			service.execute();
		}
	} //menu
//------------------------------------------------login할시 메인메뉴 출력
	public void Mainmenu(String id) {
		Scanner sc = new Scanner(System.in);

		service service = null;
		while(true) {
			System.out.println();
			System.out.println("\t1. 일정 등록");
			System.out.println("\t2. 일정 검색 및 변경");
			System.out.println("\t3. 달력 보기");
			System.out.println("\t4. 로그 아웃");
			System.out.println("**********************");
			System.out.print("\t번호 입력 : ");
			int num2 = sc.nextInt();
			
			if(num2 == 4) break;
			if(num2 == 1) service = new Insert(id);
			else if(num2 == 2) service = new Select();
			else if(num2 == 3) service = new Print();
			else {System.out.println("1~4까지 선택해주세요"); continue;}
			
			service.execute();
		} //while
	}//Membermenu()	
//-----------------------------------------Signup 회원가입
	public int Signup(Cal_DTO cal_DTO) {
		int no = 0;
		getConnection();
		try {
			pstmt = con.prepareStatement("insert into Member values(?,?,?,?)");
			
			pstmt.setString(1, cal_DTO.getId());
			pstmt.setString(2, cal_DTO.getPwd());
			pstmt.setString(3, cal_DTO.getName());
			pstmt.setString(4, cal_DTO.getBirth());
			
			no = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)pstmt.close();
				if(con != null)con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//finally
		return no;
	}//Signup
//---------------------------------------------------isExistId 회원가입시 아이디 중복확인
	public boolean isExistId(String id) {
		boolean exist = false;
		getConnection();
		try {
			pstmt = con.prepareStatement("select * from member where id=?");
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) exist = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)rs.close();
				if(pstmt != null)pstmt.close();
				if(con != null)con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//finally
		return exist;
	}//isExistId
//---------------------------------------------------------getInsert 로그인 후 일정 입력
	public int getInsert(Cal_DTO cal_DTO) {
		int no=0;
		getConnection();
		try {
			pstmt = con.prepareStatement("insert into calendar values(?,?,?,?)");

			pstmt.setString(1, cal_DTO.getId());
			pstmt.setInt(2, cal_DTO.getNum());
			pstmt.setString(3, cal_DTO.getContent());
			pstmt.setString(4, cal_DTO.getCalDate());
			
			no = pstmt.executeUpdate();
			
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
		return no;
	}//Insert()
//----------------------------------------LoginCal 로그인
	public String LoginCal(String id, String pwd) {
		String name = null;
		getConnection();
		try {
			pstmt = con.prepareStatement("select * from member where id=? and pwd=?");
			
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			
			rs = pstmt.executeQuery();
			if(rs.next()) name = rs.getString("name");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)rs.close();
				if(pstmt != null)pstmt.close();
				if(con != null)con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//finally
				
		return name;
	}//LoginCal
//------------------------------------handleDate
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
//---------------------------------displaySchedules
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
//-------------------------------------------Update
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
//----------------------------------------delete	
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



}// Cal_DAO


