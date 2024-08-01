package cal_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
	
	Scanner sc = new Scanner(System.in);
	
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
			else if(num2 == 3) service = new Print(id);
			else {System.out.println("1~4까지 선택해주세요"); continue;}
			
			service.execute();
		} //while
	}//Mainmenu()	
//-----------------------------------------Signup 회원가입
	public int Signup(Cal_DTO cal_DTO) {
		int no = 0;
		getConnection();
		try {
			pstmt = con.prepareStatement("insert into cal_member values(?,?,?,To_Date(?, 'YYMMDD'))");
			
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
			pstmt = con.prepareStatement("select * from cal_member where id=?");
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
			pstmt = con.prepareStatement("select * from cal_member where id=? and pwd=?");
			
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
//---------------------------------displaySchedules
	public void displaySchedules(String CalDate) {
        getConnection();
        String sql = "SELECT num, content FROM Calendar WHERE CalDate = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, CalDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("num") + ". " + rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{ 
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//try~catch, finally
    }

//-------------------------------------------Update
	public void Update(String newContent, int scheduleNum, String CalDate) {
        getConnection();
        String sql = "update Calendar set content = ? where num = ? and CALDATE = ?";;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newContent);
            pstmt.setInt(2, scheduleNum);
            pstmt.setString(3, CalDate);

            int su = pstmt.executeUpdate();
            if (su > 0) {
                System.out.println("일정 업데이트가 성공적했습니다.");
            } else {
                System.out.println("일정을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{ 
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//try~catch, finally
    }	
//----------------------------------------delete	
	public void Delete(int num, String CalDate) {
        getConnection();
        String deleteSql = "delete from Calendar where calDate = ? and num = ?";
        String updateSql = "update Calendar set num = num - 1 where calDate = ? and num > ?";

        try {
            pstmt = con.prepareStatement(deleteSql);
            pstmt.setString(1, CalDate);
            pstmt.setInt(2, num);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("삭제가 성공적으로 완료되었습니다.");
                pstmt = con.prepareStatement(updateSql);
               
                pstmt.setString(1, CalDate);
                pstmt.setInt(2, num);
                pstmt.executeUpdate();
                System.out.println("일정 번호가 업데이트되었습니다.");
            } else {
                System.out.println("해당 일정을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{ 
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}//try~catch, finally
    }
// 김세현 - print
//----print 달력 출력----------------------------------------------------
	public void Calenderprint(String id) throws ParseException{
		String year, month;
		Date date;
		Scanner scan = new Scanner(System.in);
		
		System.out.println();
		System.out.print("년도 입력 : ");
		year = scan.next();
		System.out.print("월 입력 : ");
		month = scan.next();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMd");
		date = sdf.parse(year+month+"1");
		
		String calc = calc(date);
		
		String[] cal = calc.split("/");
		int start = Integer.parseInt(cal[0]);
		int last = Integer.parseInt(cal[1]);
		
		String yesCal= printDB(id, year, month);
		
		display(start, last, yesCal);
		
	}
	public String calc(Date date) throws ParseException {
		int last=0, start=0;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		start = cal.get(Calendar.DAY_OF_WEEK);
		last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return start + "/" + last;
	}
	
	public void display(int start, int last, String yesCal) throws ParseException {
		System.out.println();
		
		String[] date = yesCal.split(" ");
		
		System.out.println("일\t월\t화\t수\t목\t금\t토");
		
		int[] check = new int[date.length];
		for(int j=0; j<date.length; j++) {
			
			Date day = new SimpleDateFormat("yyyy/MM/dd").parse(date[j]);
			String eday = new SimpleDateFormat("d").format(day);
			check[j] = Integer.parseInt(eday);
		}
			

		for(int i=1; i<start; i++) {
			System.out.print("\t");
		}
		
		for(int i=1; i<=last; i++) {
			boolean ischeckDay = false;
			for(int j=0; j < check.length; j++) {
				if(i ==check[j]) {
					ischeckDay = true;
					break;
				}
			}
		
			if(ischeckDay){
				System.out.print("*" + i + "\t");
			}else {
				System.out.print(i + "\t");
			}
			
			if(start%7 == 0) {
				System.out.println();
			}
			start++;
		}
	}	
//----print 달력 출력---------------------------------------------------
//----print DB-------------------------------------------------------
	public String printDB(String id, String year, String month) {
		StringBuilder calDate = new StringBuilder();
		String dateAll = null;

		int monthInt = Integer.parseInt(month);
		if(monthInt < 10 && monthInt > 0) {
			dateAll = year + "/0" + month;
		}else if(monthInt >= 10 && monthInt <= 12) {
			dateAll = year + "/" + month;
		}
		
		getConnection();
		
		String sql = "SELECT DISTINCT CALDATE FROM CALENDAR WHERE ID = ? AND CALDATE LIKE ?";

		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, dateAll + "%");
			
			rs = pstmt.executeQuery();
			
			
            while (rs.next()) {
            	calDate.append(rs.getString("CALDATE")).append(" ");
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 if (calDate.length() > 0) {
		        calDate.setLength(calDate.length() - 1); // Remove the trailing '/'
		    }

	    return calDate.toString();
	}
//----print DB-------------------------------------------------------
}// Cal_DAO


