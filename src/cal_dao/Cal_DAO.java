package cal_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import cal_bean.Cal_DTO;
import cal_service.Cal_print;
import cal_service.Insert;
import cal_service.Login;
import cal_service.Membership;
import cal_service.Print;
import cal_service.Select;
import cal_service.service;

public class Cal_DAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String ur1 = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "c##java";
	private String password = "1234";
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;

	Scanner sc = new Scanner(System.in);

//--DB-------------------------------------------------------
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
//--menu----------------------------------------------------

	public void menu() {
		service service = null;
		// 오늘날짜를 인식하여 달력 출력
		while (true) {
			System.out.println();
			System.out.println("  일정관리 프로그램입니다.");
			System.out.println("\t<메뉴>");
			System.out.println("**********************");
			System.out.println("\t1. 회원가입");
			System.out.println("\t2. 로그인");
			System.out.println("\t3. 달력보기");
			System.out.println("\t4. 종료");
			System.out.println("**********************");
			System.out.print("\t번호 입력 : ");

			int bunho = -1;

			try {
				bunho = sc.nextInt();

				if (bunho == 4)
					break;
				if (bunho == 1)
					service = new Membership();
				else if (bunho == 2)
					service = new Login();
				else if (bunho == 3)
					service = new Cal_print();
				else {
					System.out.println();
					System.out.println("-- 메뉴 중에서 다시 선택하세요. --");
					System.out.println();
					continue;
				}
				service.execute();

			} catch (InputMismatchException e) {
				// e.printStackTrace();
				System.out.println();
				System.out.println("-- 숫자 형식으로 입력하세요 --");
				System.out.println();
				sc.nextLine(); // 남아있는 버퍼를 지울려고 작성함
			} // try~catch문
		} // while 문
	} // menu
//--회원 메인메뉴----------------------------------------------

	public void Mainmenu(String id) {

		service service = null;
		while (true) {
			System.out.println();
			System.out.println("\t<일정 메뉴>");
			System.out.println("**********************");
			System.out.println("\t1. 일정 등록");
			System.out.println("\t2. 일정 검색 및 변경");
			System.out.println("\t3. 달력 보기");
			System.out.println("\t4. 로그 아웃");
			System.out.println("**********************");
			System.out.print("\t번호 입력 : ");

			int num = -1;

			try {
				num = sc.nextInt();
				if (num == 4)
					break;
				if (num == 1)
					service = new Insert(id);
				else if (num == 2)
					service = new Select(id);
				else if (num == 3)
					service = new Print(id);
				else {
					System.out.println();
					System.out.println("-- 메뉴 중에서 다시 선택해주세요 --");
					System.out.println();
					continue;
				}
				service.execute();

			} catch (InputMismatchException e) {
				// e.printStackTrace();
				System.out.println();
				System.out.println("-- 숫자 형식으로 입력하세요 --");
				System.out.println();
				sc.nextLine(); // 남아있는 버퍼를 지울려고 작성함
			} // try~catch문
		} // while
	}// Mainmenu()

//---Signup 회원가입---------------------------------------------
	public int Signup(Cal_DTO cal_DTO) {
		int no = 0;
		getConnection();
		try {
			pstmt = con.prepareStatement("insert into cal_member values(?,?,?,To_Date(?, 'yyyyMMdd'))");

			pstmt.setString(1, cal_DTO.getId());
			pstmt.setString(2, cal_DTO.getPwd());
			pstmt.setString(3, cal_DTO.getName());
			pstmt.setString(4, cal_DTO.getBirth());

			no = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // finally
		return no;
	}// Signup

//---isExistId 회원가입시 아이디 중복확인------------------------------------------------
	public boolean isExistId(String id) {
		boolean exist = false;
		getConnection();
		try {
			pstmt = con.prepareStatement("select * from cal_member where id=?");
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next())
				exist = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // finally
		return exist;
	}// isExistId
//---getInsert 로그인 후 일정 입력------------------------------------------------------

	public int getInsert(Cal_DTO cal_DTO) {
		int no = 0;
		int num = 0;

		System.out.print("\t일정을 입력하세요 : ");
		String content = sc.nextLine();
		sc.nextLine();

		getConnection();
		try {
			con.setAutoCommit(false);// 트랜젝션
			String getNum = "select coalesce(max(num), 0) from calendar where id=? and calDate=?";// coalesce함수는 null이
																									// 아닌 첫번째 인자의 값을
																									// 반환시켜줌
			// max(num)의 값이 null 일때 0값이 나올 수 있게해줌. null값이 들어가게 되면 오류가 발생할수도 있기 때문
			pstmt = con.prepareStatement(getNum);
			pstmt.setString(1, cal_DTO.getId());
			pstmt.setString(2, cal_DTO.getCalDate());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				num = rs.getInt(1) + 1;
			}
			rs.close();
			pstmt.close();

			String setNum = "insert into calendar values(?,?,?,?)";
			pstmt = con.prepareStatement(setNum);
			pstmt.setString(1, cal_DTO.getId());
			pstmt.setInt(2, num);
			pstmt.setString(3, content);
			pstmt.setString(4, cal_DTO.getCalDate());

			no = pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			if (con != null) { // 오류 발생시 돌아가기
				try {
					con.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // try~catch, finally
		return no;
	}// Insert()
//----------------------------------------LoginCal 로그인

	public String LoginCal(String id, String pwd) {
		String name = null;
		getConnection();
		try {
			pstmt = con.prepareStatement("select * from cal_member where id=? and pwd=?");

			pstmt.setString(1, id);
			pstmt.setString(2, pwd);

			rs = pstmt.executeQuery();
			if (rs.next())
				name = rs.getString("name");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // finally

		return name;
	}// LoginCal
//---------------------------------displaySchedules

	public void displaySchedules(String db_calDate, String id) {
		getConnection();
		String sql = "SELECT num, content FROM Calendar WHERE CalDate = ? AND ID = ? ORDER BY NUM";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, db_calDate);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println("\t" + rs.getInt("num") + ". " + rs.getString("content"));
			} // ----------------se
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // try~catch, finally
	}

// 김세현 - update
//---UpdateDate 날짜변경--------------------------------------------------
	// ---날짜 입력받기--------------------------------------------
	public void UpdateDate(String db_calDate, String id) {
		while (true) {
            System.out.println();
            
			int scheduleNum = -1;
			String newDate = null;
		
			while (scheduleNum < 0) {
		        try {
		            System.out.print("\t이동시킬 일정의 번호를 입력하세요 : ");
		            scheduleNum = sc.nextInt();
		            sc.nextLine(); // Consume newline
		        } catch (InputMismatchException e) {
		            System.out.println("\t잘못된 입력입니다. 숫자만 입력하세요.");
		            sc.nextLine(); // Clear the invalid input
		            System.out.println();
		        }
		    }
			
			while (newDate == null) {
		        System.out.print("\t이동할 날짜를 8글자로 입력하세요(ex. 20240801) : ");
		        newDate = sc.nextLine();
		        
		        // 날짜 형식 및 유효성 검사
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		        sdf.setLenient(false);
		        try {
		            Date chaDate = sdf.parse(newDate);
		        } catch (ParseException e) {
		            System.out.println("\t입력 형식 오류, 다시 입력하세요");
		            newDate = null;
		            System.out.println();
		        }
		    }
		
			System.out.println();
			
			Date date = new Date();
			Calendar now = Calendar.getInstance();
			now.setTime(date);
		
			int nowYear = now.get(Calendar.YEAR);
			int nowMonth = now.get(Calendar.MONTH) + 1;
			int nowDay = now.get(Calendar.DATE);
		
			int lastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
		
			try {
				Date chaDate = sdf.parse(newDate);
				cal.setTime(chaDate);
		
				int newYear = cal.get(Calendar.YEAR);
				int newMonth = cal.get(Calendar.MONTH) + 1;
				int newDay = cal.get(Calendar.DATE);
		
				Calendar cal2 = Calendar.getInstance();
				cal2.add(Calendar.YEAR, 30);
				int sysYear = cal2.get(Calendar.YEAR);
		
				if (newYear >= nowYear && newYear <= sysYear) {
					if (newMonth >= nowMonth && newMonth <= 12) {
						if (newMonth == nowMonth && newDay >= nowDay && newDay <= lastDay
								|| newMonth >= nowMonth && newDay <= lastDay && newDay > 0) {
							
							int checknum = ChangeDate(scheduleNum, newYear, newMonth, newDay, db_calDate, id);
							
							if(checknum == 1) {
								continue;
							} else {
								System.out.println("\t일정이 이동되었습니다");
								break;
							}
							
						}
						System.out.println("\t유효하지 않는 월입니다");
						continue;
					}
					System.out.println("\t유효하지 않는 년도입니다");
					continue;
				}
		
			} catch (ParseException e) {
				System.out.println("\t입력 형식 오류, 다시 입력하세요");
				System.out.println();
			} //try~catch
		}
	}

	// ---changeDate 날짜 변경--------------------------------------------
	public int ChangeDate(int scheduleNum, int newYear, int newMonth, int newDay, String calDate, String id) {
		
		Cal_DTO cal_DTO = new Cal_DTO();
		int checknum = 0;

		String changeDate = String.format("%d-%02d-%02d", newYear, newMonth, newDay);

		getConnection();
		String sql = "SELECT CONTENT FROM CALENDAR WHERE ID = ? AND CALDATE = ? AND NUM = ?";

		try {
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, calDate);
			pstmt.setInt(3, scheduleNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String content = rs.getString("content");
				
				String sql2 = "SELECT MAX(NUM) AS cal_Num FROM CALENDAR WHERE ID = ? AND CALDATE = ?";
				
				pstmt = con.prepareStatement(sql2);

				pstmt.setString(1, id);
				pstmt.setString(2, calDate);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					int max = rs.getInt("cal_Num");
					
					cal_DTO.setId(id);
					cal_DTO.setNum(scheduleNum);
					cal_DTO.setCalDate(changeDate);
					cal_DTO.setMax(max);
					
					if(scheduleNum > 0 && scheduleNum <= max) {
						ChangeInsert(cal_DTO, content);
						changeDelete(calDate, id, scheduleNum);
						checknum = 0;
					} else {
						System.out.println("\t없는 일정입니다. 번호를 다시 입력하세요.");
						checknum = 1;
					}
				}

			} else {
				System.out.println("\t없는 일정입니다. 번호를 다시 입력하세요.");
				checknum = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return checknum;
	}

	// --날짜변경-------------------------------------------------------------
	public int ChangeInsert(Cal_DTO cal_DTO, String content) {
		int no = 0;
		int num = 0;

		getConnection();
		
		try {
			con.setAutoCommit(false);// 트랜젝션
			String getNum = "select coalesce(max(num), 0) from calendar where id=? and calDate=?";
			
			pstmt = con.prepareStatement(getNum);
			pstmt.setString(1, cal_DTO.getId());
			pstmt.setString(2, cal_DTO.getCalDate());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				num = rs.getInt(1) + 1;
			}
			rs.close();
			pstmt.close();

			String setNum = "insert into calendar values(?,?,?,?)";
			pstmt = con.prepareStatement(setNum);

			pstmt.setString(1, cal_DTO.getId());
			pstmt.setInt(2, num);
			pstmt.setString(3, content);
			pstmt.setString(4, cal_DTO.getCalDate());

			no = pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			if (con != null) { // 오류 발생시 돌아가기
				try {
					con.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // try~catch, finally
		return no;
	}// Insert()
	
	//내용 삭제
	public void changeDelete(String calDate, String id, int scheduleNum) {
		String deleteSql = "DELETE FROM CALENDAR WHERE ID = ? AND CALDATE = ? AND NUM = ?";
		String updateSql = "UPDATE CALENDAR SET NUM = NUM - 1 WHERE CALDATE = ? AND NUM > ? AND ID = ?";

		try {
			getConnection();
			
            pstmt = con.prepareStatement(deleteSql);
            pstmt.setString(1, id);
            pstmt.setString(2, calDate);
            pstmt.setInt(3, scheduleNum);
            
            pstmt.executeUpdate();
            
            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, calDate);
            pstmt.setInt(2, scheduleNum);
            pstmt.setString(3, id);
            
            pstmt.executeUpdate();
            
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
				try {
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
					if (con != null) con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
           
	}//

//---UpdateDate 날짜변경--------------------------------------------------
//-------------------------------------------Update
	public void Update(String db_calDate, String id) {
	    String newContent = null;
	    int scheduleNum = 0;
	    int maxNum = 0;

	    try {
	        getConnection();

	        while (true) {
	            String sql2 = "select max(num) from calendar where id = ? and caldate = ?";
	            pstmt = con.prepareStatement(sql2);
	            pstmt.setString(1, id);
	            pstmt.setString(2, db_calDate);
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                maxNum = rs.getInt(1);  // max(num) 값을 가져옴
	            }

	            System.out.print("\t변경할 일정을 입력하세요 : ");
	            try {
	                scheduleNum = sc.nextInt();
	                sc.nextLine();  // 개행 문자 처리

	                // 일정 번호가 max(num)보다 큰지 확인
	                if (scheduleNum > maxNum) {
	                	System.out.println();
	                    System.out.println("-- 해당 날짜에 존재하는 최대 일정 번호는 " + maxNum + "입니다. 다시 입력하세요. --");
	                    System.out.println();
	                } else {
	                    break; // 일정 번호가 유효하면 루프를 종료
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("-- 유효한 숫자를 입력하세요. --");
	                sc.nextLine(); // 잘못된 입력 제거
	            }
	        }

	        // 일정 내용 입력
	        System.out.print("\t수정할 일정의 내용을 입력하세요 : ");
	        newContent = sc.nextLine();

	        // 일정 업데이트
	        String sql = "update Calendar set content = ? where num = ? and caldate = ?";
	        String updatenum = "update Calendar set num = num-1 where num > ? and caldate  = ?";
	        
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, newContent);
	        pstmt.setInt(2, scheduleNum);
	        pstmt.setString(3, db_calDate);
	        int su = pstmt.executeUpdate();

	        if (su > 0) {
	            System.out.println("-- 일정 업데이트가 성공적이었습니다. --");
	            pstmt = con.prepareStatement(updatenum);
		        
		        pstmt.setInt(1, scheduleNum);
		        pstmt.setString(2, db_calDate);
		        int su2 = pstmt.executeUpdate();
	        } else {
	            System.out.println("-- 일정을 찾을 수 없습니다. --");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (pstmt != null) {
	            try {
	                pstmt.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (con != null) {
	            try {
	                con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
//----------------------------------------delete	
	
	public void Delete(String db_calDate, String id) {
		
		int maxNum = 0;
	    int scheduleNum = 0;
		
	    
		String deleteSql = "delete from Calendar where calDate = ? and num = ? and id =?";
		String updateSql = "update Calendar set num = num - 1 where calDate = ? and num > ? and id = ?";
		
		try {
			getConnection();
			
			 while (true) {
				 String sql2 = "select max(num) from calendar where id = ? and caldate = ?";
		            pstmt = con.prepareStatement(sql2);
		            pstmt.setString(1, id);
		            pstmt.setString(2, db_calDate);
		            rs = pstmt.executeQuery();

		            if (rs.next()) {
		                maxNum = rs.getInt(1);  // max(num) 값을 가져옴
		            }
		            
		            System.out.print("\t삭제할 일정의 번호를 입력하세요 : ");
		            try {
		            	scheduleNum = sc.nextInt();
		            	
		            	if (scheduleNum > maxNum) {
		            		System.out.println();
			                System.out.println("-- 해당 날짜에 존재하는 최대 일정 번호는 " + maxNum + "입니다. 다시 입력하세요. --");
			                System.out.println();
		            	} else {
			                 break; // 일정 번호가 유효하면 루프를 종료
			            }
		            } catch (InputMismatchException e) {
		            	System.out.println();
		                System.out.println("-- 유효한 숫자를 입력하세요. --");
		                System.out.println();
		                sc.nextLine(); // 잘못된 입력 제거
		            }
			 }//while
			 
			 // 삭제 확인
		        char confirm = ' ';
		        while (true) {
		            System.out.print("\t정말 삭제 하시겠습니까? (Y/N) : ");
		            String input = sc.next().trim().toUpperCase(); // 입력을 대문자로 변환

		            if (input.equals("Y")) {
		                confirm = 'Y';
		                break;
		            } else if (input.equals("N")) {
		                confirm = 'N';
		                break;
		            } else {
		                System.out.println("-- Y 또는 N을 입력하세요. --");
		            }
		        }

		        if (confirm == 'Y') {
		            pstmt = con.prepareStatement(deleteSql);
		            pstmt.setString(1, db_calDate);
		            pstmt.setInt(2, scheduleNum);
		            pstmt.setString(3, id);
		            int rowsDeleted = pstmt.executeUpdate();
		            
		            if (rowsDeleted > 0) {
		                System.out.println("삭제가 성공적으로 완료되었습니다.");
		                
		                pstmt = con.prepareStatement(updateSql);
		                pstmt.setString(1, db_calDate);
		                pstmt.setInt(2, scheduleNum);
		                pstmt.setString(3, id);
		                pstmt.executeUpdate();
		                System.out.println("-- 일정 번호가 업데이트되었습니다. --");
		            } else {
		                System.out.println("-- 해당 일정을 찾을 수 없습니다. --");
		            }
		        } else {
		            System.out.println("-- 삭제가 취소되었습니다. --");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (rs != null) {
		                rs.close();
		            }
		            if (pstmt != null) {
		                pstmt.close();
		            }
		            if (con != null) {
		                con.close();
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }	
	}
// 김세현 - print
//----print 달력 출력----------------------------------------------------
	public void Calenderprint(String id) throws ParseException {
		String year, month;
		Scanner scan = new Scanner(System.in);

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);

		while (true) {
			System.out.println();
			System.out.print("\t년도 입력 : ");
			year = scan.next();

			cal.add(Calendar.YEAR, 30);

			int sysYear = cal.get(Calendar.YEAR);

			int checkYear;
			try {
				checkYear = Integer.parseInt(year);
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해 주세요");
				return;
			}

			if (checkYear >= 1900 && checkYear <= sysYear) {
				System.out.print("\t월 입력 : ");
				month = scan.next();

				int checkMonth;

				try {
					checkMonth = Integer.parseInt(month);
				} catch (NumberFormatException e) {
					System.out.println("숫자를 입력해 주세요");
					return;
				}

				if (checkMonth > 0 && checkMonth <= 12) {
					ChangePrint(id, year, month);
					break;
				} else {
					System.out.println("\t지원하지 않는 달 입니다.");
					continue;
				}

			} else {
				System.out.println("\t지원하지 않는 년도 입니다.");
				continue;
			}

		}
	}

	// 날짜 변형해서 보내기---------------------------
	public void ChangePrint(String id, String year, String month) throws ParseException {
		Date date;

		int monthInt = Integer.parseInt(month);

		System.out.println();
		System.out.println(year + "년 " + monthInt + "월 달력");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMd");
		date = sdf.parse(year + monthInt + "1");

		String calc = calc(date);

		String[] cal = calc.split("/");
		int start = Integer.parseInt(cal[0]);
		int last = Integer.parseInt(cal[1]);

		String[][] result = printDB(id, year, monthInt);

		String[] yesCal = result[0];
		String[] count = result[1];
		String[] content = printContent(id, year, monthInt);

		display(start, last, yesCal, count, content);
	}

	// ----print calc-------------------
	public String calc(Date date) throws ParseException {
		int last = 0, start = 0;

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		start = cal.get(Calendar.DAY_OF_WEEK);
		last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return start + "/" + last;
	}

	// ----print display-------------------
	public void display(int start, int last, String[] yesCal, String[] count, String[] content) throws ParseException {
		System.out.println();

		System.out.println("일\t월\t화\t수\t목\t금\t토");

		int[] check = new int[yesCal.length];
		for (int j = 0; j < yesCal.length; j++) {

			Date day = new SimpleDateFormat("yyyy-MM-dd").parse(yesCal[j]);
			String eday = new SimpleDateFormat("d").format(day);
			check[j] = Integer.parseInt(eday);
		}

		for (int i = 1; i < start; i++) {
			System.out.print("\t");
		}

		StringBuilder lineBelow = new StringBuilder();
		StringBuilder lineBelow2 = new StringBuilder();

		for (int i = 1; i < start; i++) {
			lineBelow.append("\t");
			lineBelow2.append("\t");
		}

		int n = 0;

		for (int i = 1; i <= last; i++) {
			boolean ischeckDay = false;
			for (int j = 0; j < check.length; j++) {
				if (i == check[j]) {
					ischeckDay = true;
					break;
				}
			}
			if (ischeckDay) {
				System.out.print(i + "\t");
				if(n < count.length) {
					lineBelow.append(count[n]).append("개의 일정\t");
				}else {
					lineBelow2.append(content[n]).append("\t");
				}
				if (n < content.length) {
	                lineBelow2.append(content[n]).append("\t");
	            } else {
	                lineBelow2.append("\t");
	            }
				
				n++;
			} else {
				System.out.print(i + "\t");
				lineBelow.append("\t");
				lineBelow2.append("\t");
			}

			if (start % 7 == 0) {
				System.out.println();
				System.out.println(lineBelow.toString()); // Print the `--` line
				lineBelow.setLength(0);
				System.out.println(lineBelow2.toString()); // Print the `--` line
				lineBelow2.setLength(0);
			}
			start++;
		}

		if (start % 7 != 1) {
			System.out.println();
			System.out.println(lineBelow.toString());
			System.out.println(lineBelow2.toString());
		}
	}

//----print 달력 출력---------------------------------------------------
//----print DB-------------------------------------------------------
	public String[][] printDB(String id, String year, int month) {
		List<String> dateList = new ArrayList<>();
		List<String> countList = new ArrayList<>();

		String dateAll = null;
		if (month < 10 && month > 0) {
			dateAll = year + "-0" + month;
		} else if (month >= 10 && month <= 12) {
			dateAll = year + "-" + month;
		}

		getConnection();

		String sql = "SELECT COUNT(*) AS COUNT, CALDATE FROM CALENDAR WHERE ID = ? AND CALDATE LIKE ? GROUP BY CALDATE order by caldate";
		try {
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, dateAll + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				String calDate = rs.getString("CALDATE");
				int count = rs.getInt("COUNT");

				dateList.add(calDate);
				countList.add(String.valueOf(count));

				// calDate.append(rs.getString("CALDATE")).append(" ");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String[] yesCal = dateList.toArray(new String[0]);
		String[] count = countList.toArray(new String[0]);

		return new String[][] { yesCal, count };
	}

	// ----print Content-------------------
	public String[] printContent(String id, String year, int month) {
		List<String> contentList = new ArrayList<String>();

		String dateAll = null;
		if (month < 10 && month > 0) {
			dateAll = year + "-0" + month;
		} else if (month >= 10 && month <= 12) {
			dateAll = year + "-" + month;
		}

		getConnection();

		String sql = "SELECT CONTENT FROM CALENDAR WHERE ID = ? AND CALDATE LIKE ? AND NUM = 1 ORDER BY CALDATE, num";
		try {
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, dateAll + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				String content = rs.getString("CONTENT");

				if (content != null && content.length() > 4) {
					contentList.add(content.substring(0, 3) + "...");
				} else {
					contentList.add(content + "...");
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String[] postContent = contentList.toArray(new String[0]);
		return postContent;
	}
//----print DB-------------------------------------------------------
}// Cal_DAO
