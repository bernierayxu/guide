


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Servlet implementation class fileUploadServlet
 */
@WebServlet("/fileUploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2,
				 maxFileSize=1024*1024*20,
				 maxRequestSize=1024*1024*100)

public class fileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public fileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//Connection Variables.
		Connection con = null;
		Statement sm = null;

		//Connect To Server And Create Statement
		String url = "jdbc:mysql://127.4.100.130:3306/gci4";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,"admin5pLIyrz","Emf4kZS4Mtdi");
			sm=con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}


		//Global Variables
		ArrayList<TourAndGuide> tgArr = new ArrayList<TourAndGuide>();
		ArrayList<PaxList> paxList = new ArrayList<PaxList>();
		String paxRes = "";
		String helpRes = "";
		String finalRes = "";
		String fileRes = "";

		//Table Start Here
		paxRes += "<table id=\"paxTable\" name=\"paxTable\">";
		paxRes += "<tr>";
		paxRes += "<th>Tour</th><th>Leader</th><th>Group</th><th>Confirmation</th><th>Pax</th><th>Pickup</th><th>Phone</th>";
		paxRes += "</tr>";

		//Upload File To Location.
		//String year = Integer.toString((int)Calendar.getInstance().get(Calendar.YEAR)) + "-";
		Part filePart = request.getPart("file");
		String fileName = getSubmittedFileName(filePart);
		String tFileName = getTrueFileName(fileName);
		System.out.println("New getTrueFileName:" + tFileName);
		String tDate = tFileName;
		InputStream fileContent = filePart.getInputStream();

		//Check If File Exist. If Yes. Delete The Old One File And SQL Server Data.
		File oldCheck = new File(System.getenv("OPENSHIFT_DATA_DIR")+tFileName);
		if(oldCheck.exists() && !oldCheck.isDirectory()){
			oldCheck.delete();
			System.out.println("Old File Deleted!");
			String commandDelete = String.format("DELETE FROM gci4.paxinfo WHERE date='%s'", tDate);
			String commandDeleteBus = String.format("DELETE FROM gci4.businfo WHERE date='%s'", tDate);
			try {
				sm.executeUpdate(commandDelete);
				sm.executeUpdate(commandDeleteBus);
				System.out.println("Old Data&BusData Deleted!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		//Start Write Shuttle Bus Info For Each Pick Location.
		String[] locs = {"FLU","BRK","JCC","EDI"};
		for(int i=0; i<locs.length; i++){
			for(int j=1; j<=10; j++){
				String commandLoc = String.format("INSERT INTO gci4.businfo (date, pickloc, bus) VALUE('%s','%s','%s')", tDate, locs[i], "Shuttle#"+j);
				try{
					sm.executeUpdate(commandLoc);
				}
				catch(SQLException e){
					e.printStackTrace();
				}
			}
		}


		//Start Excel File Processing.
		File upD = new File(System.getenv("OPENSHIFT_DATA_DIR"));
		File outFile = new File(upD,tFileName);
		try{
			Files.copy(fileContent, outFile.toPath());
			System.out.println("Done! File Uploaded");
		}
		catch(Exception e){
			System.out.println(e);
		}

		//Handle Uploaded File
		InputStream ExcelFileToRead = new FileInputStream(System.getenv("OPENSHIFT_DATA_DIR")+tFileName);
		XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
		int NumOfSheets = wb.getNumberOfSheets();


		//For Loop Though Each Sheet
		for(int i=0; i<NumOfSheets; ++i){
			XSSFSheet sheet = wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();

			//GUIDE Sheet Work
			if(sheetName.contains("GUIDE")){
				for(Row r : sheet){

					//Locate Cell in Column 0
					Cell c = r.getCell(0, Row.CREATE_NULL_AS_BLANK);

					//Find Row Start with "LL"
					try{
					if(c.getStringCellValue().equals("LL")){
						Cell cLocation = r.getCell(1, Row.CREATE_NULL_AS_BLANK);
						Cell cTourName = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
						Cell cTourGuide = r.getCell(5, Row.CREATE_NULL_AS_BLANK);
						Cell cTourPhone = r.getCell(7, Row.CREATE_NULL_AS_BLANK);
						//Store Data In ArrayList<TourAndGuide>
						tgArr.add(new TourAndGuide(getTrueTourName(cTourName),cTourGuide.getStringCellValue(),cTourPhone.getStringCellValue(),getTrueDirect(cLocation)));
					}
					}
					catch(Throwable e){
						System.out.println(e.getMessage()+"In Row:"+r.getRowNum());
					}

					/*
					//Find Row Start With "EDI幫忙"
					if(c.getStringCellValue().equals("EDI幫忙")){
						int rowNum = c.getRowIndex()+1;

						//Add Information To HelpRes
						helpRes += "<table>";
						helpRes += "<tr>";
						helpRes += "<th>Helper</th><th>Phone</th>";
						helpRes += "</tr>";
						for(int j=rowNum; j<rowNum+4; j++){
							CellReference cr = new CellReference("F"+j);
							Row tempRow = sheet.getRow(cr.getRow());
							Cell tempHelper = tempRow.getCell(5, Row.CREATE_NULL_AS_BLANK);
							Cell tempPhone = tempRow.getCell(7, Row.CREATE_NULL_AS_BLANK);
							String strHelper = tempHelper.getStringCellValue();
							String strPhone = tempPhone.getStringCellValue();
							if(!strHelper.equals("") && !strHelper.equals("")){
								helpRes += "<tr>";
								helpRes += "<td>" + strHelper + "</td>" + "<td>" + strPhone + "</td>"; //Add Help Information To helpRes.
								helpRes += "</tr>";
							}
						}
						helpRes += "</table>";
						helpRes += "<br>";
					}
					*/
				}
			}
			
			/*
			//BRK+EDI Sheet Work
			if(sheetName.contains("BRK+EDI")){
				String tourGuide = "", groupNum = "", conNum = "", paxNum = "", pickLoc = "", phoneNum = "", tourName = "", tourBus = "";
				FormulaEvaluator evtor = wb.getCreationHelper().createFormulaEvaluator();

				//Loop Each Row In Sheet
				for(Row r : sheet){

					//Get Row Cell0 And Cell2 String Value
					Cell c = r.getCell(0, Row.CREATE_NULL_AS_BLANK);
					Cell cCon = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
					Cell cPax = r.getCell(3, Row.CREATE_NULL_AS_BLANK);
					String cStr = "";
					String cStrCon = "";
					String cStrPax = "";

					//Cell0
					switch(c.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStr = c.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStr = Integer.toString((int)c.getNumericCellValue());
						break;
					}

					//Cell2
					switch(cCon.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrCon = cCon.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrCon = Integer.toString((int)cCon.getNumericCellValue());
						break;
					}

					//Cell3
					switch(cPax.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrPax = cPax.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrPax = Integer.toString((int)cPax.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						CellValue tPaxNum = evtor.evaluate(cPax);
						cStrPax = Integer.toString((int)tPaxNum.getNumberValue());
					}

					//Location Array List
					ArrayList<Integer> marks = new ArrayList<Integer>();
					ArrayList<String> strMarks = new ArrayList<String>();

					//If Row Is Location
					if(cStr.contains("*")){
						marks.add(r.getRowNum());
						strMarks.add(cStr);
						paxRes += "<tr>";
						paxRes += "<td colspan=\"7\">"+cStr+"</td>";
						paxRes += "<tr>";
					}

					//If Row Is Pax List
					else if(!cStr.contains("日期") && !cStr.contains("組號") && !cStrCon.equals("")){

						//Get Confirmation Cell Value
						Cell conNumCell = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
						switch(conNumCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							conNum = conNumCell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							conNum = Integer.toString((int) conNumCell.getNumericCellValue());
							break;
						}

						//Get Phone Cell Value
						Cell phoneNumCell = r.getCell(6, Row.CREATE_NULL_AS_BLANK);
						switch(phoneNumCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							phoneNum = phoneNumCell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							phoneNum = Integer.toString((int) phoneNumCell.getNumericCellValue());
							break;
						}

						//Get Pax Number
						paxNum = Integer.toString((int) r.getCell(3, Row.CREATE_NULL_AS_BLANK).getNumericCellValue());;

						//Get Pick Up Location
						pickLoc = r.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

						//Get Tour Name
						tourName = r.getCell(7, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
						if(tourName.length() >= 3){
							tourName = tourName.substring(0,3);  //Get True Tour Name (3 Letters).
						}
						else{
							tourName = "No Tour";
						}


						//Search For Group Number (Must Do)
						String[] rawStr = findGroupNum(wb,conNum,tourName).split("@@@");//Return 0groupNum plus 1tourBus. So I have to split them.
						groupNum = rawStr[0];
						tourBus = rawStr[1];


						//Search For Tour Guide
						for(TourAndGuide tg : tgArr){
							if(tourName.equals("No Tour")){
								tourGuide = "No Tour Guide";
							}
							else{
								if(tg.getTourName().equals(tourBus)){
									tourGuide = tg.getGuideName();
									break;
								}
							}
						}



						//Add To Final Pax List
						paxList.add(new PaxList(tourGuide,groupNum,conNum,paxNum,pickLoc,phoneNum,tourName));
						paxRes += "<tr onclick=\"changeColor(this)\" style=\"background: rgb(255, 178, 178);\">"; //Add JS here
						paxRes += "<td>"+tourName+"</td>";
						paxRes += "<td>"+tourGuide+"</td>";
						paxRes += "<td>"+groupNum+"</td>";
						paxRes += "<td>"+conNum+"</td>";
						paxRes += "<td>"+paxNum+"</td>";
						paxRes += "<td>"+pickLoc+"</td>";
						paxRes += "<td>"+phoneNum+"</td>";
						paxRes += "</tr>";
					}

					//If Row Is Total Formula
					else if(cStr.equals("") && cStrCon.equals("") && !cStrPax.equals("") ){
						paxRes += "<tr>";
						paxRes += "<td></td><td></td><td></td><td>Total:</td><td>"+cStrPax+"</td>";
						paxRes += "</tr>";
					}
				}
				paxRes += "</table>"; //Pax Table Ends If This Row Is Pax List. The Start Of This Table At Global Variables.
			}//BRK+EDI Sheet Ends Here.
			*/


			//If sheet is XX#X With Out NY And WP And WT And Other Special With No Room
			if(sheetName.contains("#") /*&& !sheetName.contains("NY") && !sheetName.contains("WP") && !sheetName.contains("WT") && !sheetName.contains("FP1")*/){
				System.out.println("Not In special sheet Handler:"+sheetName);
				String groupNum = "", tourGuide = "No Tour Guide", tourPhone = "", conNum = "", paxNum = "", paxRoom = "", pickLoc = "", phoneNum = "", tourName = "", tourBus = "", date = "", remark = "";
				int tourDirect = 0;

				//Get TourBus
				tourBus = getTrueTourNameFromSheetName(sheetName);

				//Search For Tour Guide
				for(TourAndGuide tg : tgArr){
					if(tg.getTourName().equals(tourBus)){
						tourGuide = tg.getGuideName();
						tourPhone = tg.getGuidePhone();
						tourDirect = tg.getDirect();
						break;
					}
				}

				//Loop Each Row In Sheet
				for(Row r : sheet){

					//Get Row Cell0 And Cell2 String Value
					Cell c = r.getCell(0, Row.CREATE_NULL_AS_BLANK);
					Cell cCon = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
					Cell cPax = r.getCell(4, Row.CREATE_NULL_AS_BLANK);
					String cStr = "";
					String cStrCon = "";
					String cStrPax = "";

					//Cell0 Group Number
					switch(c.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStr = c.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStr = Integer.toString((int)c.getNumericCellValue());
						break;
					}

					//Cell2 Confirmation
					switch(cCon.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrCon = cCon.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrCon = Integer.toString((int)cCon.getNumericCellValue());
						break;
					}

					//Cell4 Pax Number
					switch(cPax.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrPax = cPax.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrPax = Integer.toString((int)cPax.getNumericCellValue());
						break;
					}

					//If It's Pax List.
					if(!cStr.contains("組號") && !cStr.contains("日期") && !cStrCon.isEmpty() && !cStrPax.isEmpty()){
						//Get Date
						date = tDate;

						//Get Group Number.
						groupNum = cStr;

						//Get Confirmation Number.
						conNum = cStrCon;

						//Get Pax Number.
						paxNum = cStrPax;

						//Get Pax Room.
						Cell paxRoomCell = r.getCell(5, Row.CREATE_NULL_AS_BLANK);
						switch(paxRoomCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							paxRoom = paxRoomCell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							paxRoom = Integer.toString((int) paxRoomCell.getNumericCellValue());
							break;
						}

						//Get Pick Up Location
						pickLoc = r.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

						//Get Phone Cell Value
						Cell phoneNumCell = r.getCell(3, Row.CREATE_NULL_AS_BLANK);
						switch(phoneNumCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							phoneNum = phoneNumCell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							phoneNum = Integer.toString((int) phoneNumCell.getNumericCellValue());
							break;
						}

						//Get Tour Name
						tourName = r.getCell(7, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
						if(tourName.length() >= 3){
							tourName = tourName.substring(0,3);  //Get True Tour Name (3 Letters).
						}
						else{
							tourName = "No Tour";
						}

						//Get Remark
						Cell remarkCell = r.getCell(10, Row.CREATE_NULL_AS_BLANK);
						switch(remarkCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							remark = removeSpecial(remarkCell.getStringCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							remark = removeSpecial(Integer.toString((int) remarkCell.getNumericCellValue()));
							break;
						}

						//Add To Server Database.
						String command = String.format("INSERT INTO gci4.paxinfo (date, bus, tourguide, tourphone, groupnum, agent, conf, paxnum, paxroom, pickloc, phone, groupcode, invoice, remark, direct) VALUE('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", date, sheetName, tourGuide, tourPhone, groupNum, "", conNum, paxNum, paxRoom, pickLoc, phoneNum, tourName, "", remark, tourDirect);
						try {
							sm.executeUpdate(command);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}//For # Sheet With Out NY And WP

			/*//For NY#X AND WP#X AND WT#X AND FP1 AND FP1
			if(sheetName.contains("#") && (sheetName.contains("NY") || sheetName.contains("WP") || sheetName.contains("WT") || sheetName.contains("FP1"))){

				System.out.println("In special sheet Handler:"+sheetName);

				String groupNum = "", tourGuide = "No Tour Guide", tourPhone="", conNum = "", paxNum = "", paxRoom = "0", pickLoc = "", phoneNum = "", tourName = "", tourBus = "", date = "", remark = "";
				int tourDirect = 0;
				//Get TourBus
				tourBus = getTrueTourNameFromSheetName(sheetName);

				//Search For Tour Guide
				for(TourAndGuide tg : tgArr){
					if(tg.getTourName().equals(tourBus)){
						tourGuide = tg.getGuideName();
						tourPhone = tg.getGuidePhone();
						tourDirect = tg.getDirect();
						break;
					}
				}

				//Loop Each Row In Sheet
				for(Row r : sheet){

					//Get Row Cell0 And Cell2 String Value
					Cell c = r.getCell(0, Row.CREATE_NULL_AS_BLANK);
					Cell cCon = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
					Cell cPax = r.getCell(4, Row.CREATE_NULL_AS_BLANK);
					String cStr = "";
					String cStrCon = "";
					String cStrPax = "";

					//Cell0 Group Number
					switch(c.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStr = c.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStr = Integer.toString((int)c.getNumericCellValue());
						break;
					}

					//Cell2 Confirmation
					switch(cCon.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrCon = cCon.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrCon = Integer.toString((int)cCon.getNumericCellValue());
						break;
					}

					//Cell3 Pax Number
					switch(cPax.getCellType()){
					case Cell.CELL_TYPE_STRING:
						cStrPax = cPax.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						cStrPax = Integer.toString((int)cPax.getNumericCellValue());
						break;
					}

					//If It's Pax List.
					if(!cStr.contains("日期") && !cStr.contains("組號") && !cStrCon.isEmpty() && !cStrPax.isEmpty()){
						//Get Date
						date = tDate;

						//Get Group Number.
						groupNum = cStr;

						//Get Confirmation Number.
						conNum = cStrCon;

						//Get Pax Information.
						paxNum = cStrPax;

						//Get Pick Up Location
						pickLoc = r.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

						//Get Phone Cell Value
						Cell phoneNumCell = r.getCell(3, Row.CREATE_NULL_AS_BLANK);
						switch(phoneNumCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							phoneNum = phoneNumCell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							phoneNum = Integer.toString((int) phoneNumCell.getNumericCellValue());
							break;
						}

						//Get Tour Name
						tourName = r.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
						if(tourName.length() >= 3){
							tourName = tourName.substring(0,3);  //Get True Tour Name (3 Letters).
						}
						else{
							tourName = "No Tour";
						}

						//Get Remark
						Cell remarkCell = r.getCell(9, Row.CREATE_NULL_AS_BLANK);
						switch(remarkCell.getCellType()){
						case Cell.CELL_TYPE_STRING:
							remark = removeSpecial(remarkCell.getStringCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							remark = removeSpecial(Integer.toString((int) remarkCell.getNumericCellValue()));
							break;
						}

						//Add To Server.
						String command = String.format("INSERT INTO gci4.paxinfo (date, bus, tourguide, tourphone, groupnum, agent, conf, paxnum, paxroom, pickloc, phone, groupcode, invoice, remark, direct) VALUE('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", date, sheetName, tourGuide, tourPhone, groupNum, "", conNum, paxNum, paxRoom, pickLoc, phoneNum, tourName, "", remark, tourDirect);
						try {
							sm.executeUpdate(command);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}*/
			
		}//Loop All Sheets End
		//Disconnect From Database.
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	  	//Prepare Final Response Data And Send
	  	finalRes = helpRes+paxRes;
	  	fileRes = tFileName+"Uploaded!";
	  	response.setContentType("text/plain; charset=GB18030");
	    response.setCharacterEncoding("GB18030");
	  	response.getOutputStream().write(fileRes.getBytes());
	}

	public static String getTrueFileName(String oName){
		String rName = null;
		String[] temp = null;
		if(oName.length()>10){
			temp = oName.split("\\\\");
		}
		temp = temp[temp.length-1].split("\\.");
		temp = temp[0].split("-"); 
		rName = String.format("%s-%s-%s", temp[2], temp[0], temp[1]);
		return rName;
	}

	public static int getTrueDirect(Cell cTourDirect){
		String cDirect = cTourDirect.getStringCellValue();
		System.out.println("The cTourDirect Value:" + cDirect);
		int res = 0;
		if(cDirect.contains("Direct") || cDirect.contains("direct")){
			System.out.println("In Direct");
			res = 1;
		}
		return res;
	}

	public static String getTrueTourName(Cell cTourName){
		String tourName = cTourName.getStringCellValue();
		String strPart;
		String numPart;

		if(tourName.length() > 2){
			if(tourName.contains("\\+")){
				String temp = tourName.split("\\+")[0];
				tourName = temp;
			}
			int nNum = 0;
			int sNum = 0;
			while(Character.isDigit(tourName.charAt(nNum))){ nNum++; }
			numPart = tourName.substring(0,nNum);
			sNum = nNum;
			while(!Character.isDigit(tourName.charAt(sNum))){ sNum++; }
			strPart = tourName.substring(nNum,sNum);
		}
		else{
			numPart = "";
			strPart = "No Tour";
		}

		return strPart+numPart;
	}

	public static String getTrueTourNameFromSheetName(String sheetName){
		String strPart;
		String numPart;
		String[] temp = sheetName.split("#");

		if(temp[0].contains("+")){
			strPart = temp[0].split("\\+")[0];
		}
		else{
			strPart = temp[0];
		}

		numPart = temp[1];

		return strPart+numPart;

	}

	public static String findGroupNum(XSSFWorkbook wb, String conNum, String tourName){
		String trueTourName = tourName.substring(0,2);
		int numOfSheets = wb.getNumberOfSheets();
		for(int i=0; i<numOfSheets; i++){
			XSSFSheet sheet = wb.getSheetAt(i);
			if(sheet.getSheetName().contains(trueTourName) && !sheet.getSheetName().contains("BRK+EDI")){ //Found That Sheet
				for(Row r : sheet){

					//For Confirmation Cell
					Cell c = r.getCell(2, Row.CREATE_NULL_AS_BLANK);
					String tempCValue = "";
					String tempGValue = "";
					switch(c.getCellType()){
					case Cell.CELL_TYPE_STRING:
						tempCValue = c.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						tempCValue = Integer.toString((int)c.getNumericCellValue());
						break;
					}

					//For Group Number Cell
					Cell cGroupNumCell = r.getCell(0, Row.CREATE_NULL_AS_BLANK);
					switch(cGroupNumCell.getCellType()){
					case Cell.CELL_TYPE_STRING:
						tempGValue = cGroupNumCell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						tempGValue = Integer.toString((int) cGroupNumCell.getNumericCellValue());
						break;
					}


					if(tempCValue.equals(conNum)){ //Found Confirmation Number
						String tourBus = getTrueTourNameFromSheetName(sheet.getSheetName());
						return tempGValue+"@@@"+tourBus;
					}
				}
			}
		}
		return "No Group#@@@No Bus#";
	}


	private static String getSubmittedFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}

	private static String removeSpecial(String text){
		StringBuilder rs = new StringBuilder();
		for(char c : text.toCharArray()){
			if(c == '\'' || c == '\"'){
				rs.append("");
			}
			else{
				rs.append(c);
			}
		}
		return rs.toString();
	}

}
