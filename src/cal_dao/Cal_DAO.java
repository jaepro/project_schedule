package cal_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import cal_bean.Cal_DTO;
import cal_service.Membership;
import cal_service.service;

public class Cal_DAO {
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "c##java";
	private String password = "1234";

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public Cal_DAO() {
		try {
			Class.forName(driver);//생성
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void getConnection() {
		try {
			con = DriverManager.getConnection(url, username, password);
		}catch (SQLException e) {
			e.printStackTrace();
		}//catch
	}

	private static Cal_DAO instance = new Cal_DAO(); // 싱글톤
	
	public static Cal_DAO getInstance() {
		return instance;
	}
	
//-------------------------------------- DB
	
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
			
			service.execute();
		}
	}

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
				if(rs != null)pstmt.close();
				if(pstmt != null)pstmt.close();
				if(con != null)con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//finally
		return no;
	}//Signup
//-------------------------------------------id 중복값 확인
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
}//Cal_DAO
