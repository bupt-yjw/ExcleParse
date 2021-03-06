package com.netease;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	
	private static String number;  //工号
	private static EmployeeMessage employeeMessage;   //存储每个员工的信息
	private static boolean changeVal = false; 	//用来判断是都有数据更新，从而确定是否更新employeeMessage里的字段
	private static StringBuffer sb ;
	private static int i=0;//定位错误使用
	private static String jobNumber = null;	//存储工号
	private static BigDecimal b;
	private static boolean lastData = false;
	private static Map<String, EmployeeMessage> map = new HashMap<String, EmployeeMessage>();
	
	public static void main(String[] args) {

		final JFrame jFrame = new JFrame("标题");
		final JButton jButton = new JButton("开始生成考勤统计表");
		jFrame.getContentPane().add(jButton);
		jFrame.setSize(400, 300);
		jFrame.setVisible(true);
		jButton.setBackground(Color.LIGHT_GRAY);
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String filepath = "E:/excle/";
				File file = new File(filepath);
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					Map<String, EmployeeMessage> map1 = new HashMap<String, EmployeeMessage>();
					try {
						map1 = readExcel(readfile);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					wirteExcel(map1, "wyj");
					jButton.setEnabled(false);
					jButton.setBackground(Color.green);
					jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				
			}
		});

	}

	public static Map<String, EmployeeMessage> readExcel(File file1)
			throws Exception {
		 map = new HashMap<String, EmployeeMessage>();
		Workbook wb = null;
		try {
			InputStream inputStream = new FileInputStream(file1);
			String fileName = file1.getName();
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream); // 解析xls格式
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream); // 解析xlsx格式
			}
			Sheet sheet = wb.getSheetAt(0); // 第一个工作表

			int firstRowIndex = sheet.getFirstRowNum() + 1; // 得到第一行的下标，此处加2目的是为了避免读取活动首行
			int lastRowIndex = sheet.getLastRowNum(); // 得到最后一行的下标 ，他们之差代表总行数
			sb = new StringBuffer("");
			sb.append("E:/");
			sb.append(fileName.split(".xls")[0]+"-");
			if (sheet.getRow(firstRowIndex) != null
					&& sheet.getRow(firstRowIndex).getCell(4).toString().length() > 0) {
				sb.append(sheet.getRow(firstRowIndex).getCell(0).toString().split("-")[0]);
				sb.append("-");
				sb.append(sheet.getRow(firstRowIndex).getCell(0).toString().split("-")[1]);
			}
			sb.append("考勤表.xls");
			// 循环读取数据
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
			
				if(rIndex == lastRowIndex){
					lastData = true;
				}
				Row row = null;
				try {
				if (sheet.getRow(rIndex) != null
						&& sheet.getRow(rIndex).getCell(4).toString().length() > 0) {
					row = sheet.getRow(rIndex);// 得到一行数据
					if (row.getCell(4) != null) {
						number = row.getCell(4).toString();
						if (!map.containsKey(number)) {
							String s = null;
							if(row.getCell(8)==null){
								s="";
							}else{
								s=row.getCell(8).toString();
							}
							map.put(number, new EmployeeMessage(number, row
									.getCell(3).toString(), row.getCell(6)
									.toString(), row.getCell(7).toString(), s));
						}
					}

					
					employeeMessage = map.get(number);
					
					//avgWorkTime(row);
					
					if (row.getCell(2).toString().contains("旷工")) {
						employeeMessage.setTimeAbsence(employeeMessage
								.getTimeAbsence() + 1);
						map.put(number, employeeMessage);

					} else if (row.getCell(2).toString().contains("年假")||row.getCell(2).toString().contains("休息")
							|| row.getCell(2).toString().contains("调休假")||row.getCell(2).toString().contains("有薪病假")) {
						continue;
					} else if (row.getCell(2).toString().contains("缺下")) {
						lateDay(row);
						
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					}else if( row.getCell(2).toString().contains("缺上")){
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					} else {
						avgWorkTime(row);
							if (row.getCell(14)==null||row.getCell(14).toString().equals("0")
									||row.getCell(14).toString().equals("")|| row.getCell(15).toString().length() <= 0) {
							} else {
								if( (Double.valueOf(row.getCell(14).toString())
										.compareTo(9.0) < 0)||(Integer.valueOf(row.getCell(15).toString()
												.split(":")[0]) >= 10 && Double.valueOf(row.getCell(14).toString())
												.compareTo(10.0) < 0 ) ){
									employeeMessage
											.setTimeLessThan9(employeeMessage
													.getTimeLessThan9() + 1);
									changeVal = true;
								}else if( (Double.valueOf(row.getCell(14).toString())
										.compareTo(10.0) > 0 && Integer.valueOf(row.getCell(15).toString()
												.split(":")[0]) < 10)||(Integer.valueOf(row.getCell(15).toString()
												.split(":")[0]) >= 10 && Double.valueOf(row.getCell(14).toString())
												.compareTo(11.0) > 0 ) ){
									employeeMessage
											.setTimeMoreThan9(employeeMessage
													.getTimeMoreThan9() + 1);
									changeVal = true;
								}
								
							lateDay(row);
								if (changeVal) {
									changeVal = false;
									map.put(number, employeeMessage);
								}
							}

					}

				} else {
					continue;
				}
//主要用做调试定位错误行				
/*System.out.println(row.getCell(0).toString()+row.getCell(1).toString()
						+ row.getCell(2).toString()+row.getCell(3).toString());
System.out.println("---------"+i);
i++;*/
				} catch (Exception e) {
System.out.println("wyj"+row.getCell(0)+"---"+row.getCell(1)+"---"+row.getCell(2)+"---"+row.getCell(3));
System.out.println(e);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 统计平均工作时长
	 * (正常考勤时间-1)的平均值
	 * @param row
	 */
	private static void avgWorkTime(Row row){
		if (row.getCell(14)==null||row.getCell(14).toString().equals("0.0")
				||row.getCell(14).toString().equals("")) {
		}else{
			//先按正常时间统计，在写入的时候在减去1小时
			employeeMessage.setWorkTimeOfTotal(employeeMessage.getWorkTimeOfTotal()+ Double.valueOf(row.getCell(14).toString())) ;
			employeeMessage.setWorkTime(employeeMessage.getWorkTime()+1);
		}




//原先统计依赖于顺序
	/*	if(jobNumber == null || jobNumber.length()<=0){
			jobNumber = number;
		}
		if(lastData){	//最后一个人的数据，由于后面没有别的工号，所以需要单独处理
			lastData = false;

			if(number.equals(jobNumber)){
				if (row.getCell(14)==null||row.getCell(14).toString().equals("0.0")
						||row.getCell(14).toString().equals("")) {
				}else{
					workTimeOfTotal += Double.valueOf(row.getCell(14).toString());
					normalDay +=1;
				}
			}
			
			if(normalDay == 0){
				map.get(jobNumber).setAvgWorkTime(0);
			}else{
			b = new BigDecimal((workTimeOfTotal-normalDay-map.get(jobNumber).getTimeAfter10())/normalDay);  
			map.get(jobNumber).setAvgWorkTime(b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
		}
			jobNumber = null;//最后一条数据将jobNumber设为null，否则在解析下一个文件的时候会报错。
			workTimeOfTotal = 0;
			normalDay = 0;
			
		}else{
			if(number.equals(jobNumber)){
				if (row.getCell(14)==null||row.getCell(14).toString().equals("0.0")
						||row.getCell(14).toString().equals("")) {
				}else{
					workTimeOfTotal += Double.valueOf(row.getCell(14).toString());
					normalDay +=1;
				}
			}else{
				if(normalDay == 0){
					map.get(jobNumber).setAvgWorkTime(0);
				}else{
				b = new BigDecimal((workTimeOfTotal-normalDay-map.get(jobNumber).getTimeAfter10())/normalDay);  
				map.get(jobNumber).setAvgWorkTime(b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
			}
				jobNumber = number;//把工号变为下一个人的工号
				workTimeOfTotal = 0;
				normalDay = 0;
			}
		}*/
		
	}
	
	/**
	 * 统计迟到天数
	 * 	如果刷卡时间在十点以后，那么9：30，9：40，9：50都要进行统计,以此类推
	 * @param row
	 */
	private static void lateDay(Row row) {
		if(Integer.valueOf(row.getCell(15).toString()
				.split(":")[0]) >= 10) {
			 employeeMessage.setTimeAfter950(employeeMessage
						.getTimeAfter950() + 1);
			 employeeMessage.setTimeAfter940(employeeMessage
						.getTimeAfter940() + 1);
			 employeeMessage.setTimeAfter930(employeeMessage
						.getTimeAfter930() + 1);
			 if(Integer.valueOf(row.getCell(15).toString()
						.split(":")[1]) > 0){
						 employeeMessage.setTimeAfter10(employeeMessage
									.getTimeAfter10() + 1); 
					 }
		}else if(Integer.valueOf(row.getCell(15).toString()
				.split(":")[0]) == 9){
			if(Integer.valueOf(row.getCell(15).toString()
					.split(":")[1]) > 50){
				employeeMessage.setTimeAfter950(employeeMessage
						.getTimeAfter950() + 1);
			 employeeMessage.setTimeAfter940(employeeMessage
						.getTimeAfter940() + 1);
			 employeeMessage.setTimeAfter930(employeeMessage
						.getTimeAfter930() + 1);
			}else if(Integer.valueOf(row.getCell(15).toString()
					.split(":")[1]) > 40){
			 employeeMessage.setTimeAfter940(employeeMessage
						.getTimeAfter940() + 1);
			 employeeMessage.setTimeAfter930(employeeMessage
						.getTimeAfter930() + 1);
			}else if(Integer.valueOf(row.getCell(15).toString()
					.split(":")[1]) > 30){
				 employeeMessage.setTimeAfter930(employeeMessage
							.getTimeAfter930() + 1);
			}else{
				employeeMessage.setTimeBefore930(employeeMessage
						.getTimeBefore930() + 1);
			}
		}else{
			employeeMessage.setTimeBefore930(employeeMessage
					.getTimeBefore930() + 1);
		}
		 changeVal = true;
	}

	public static String wirteExcel(Map<String, EmployeeMessage> map,
			String fileName) {
		HSSFRow row = null;
		// 第一步，创建一个webbook，对应一个Excel以xsl为扩展名文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("统计表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		// HSSFDataFormat dataFormat = wb.createDataFormat();日期转换，可以不用
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个左对齐格式
		
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("工号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("一级部门");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("二级部门");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("三级部门");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("9:30前打卡天数");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("9:30后打卡天数");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("9:40后打卡天数");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("9:50后打卡天数");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("10点后打卡天数");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("有效工作时间不满8小时");
		cell.setCellStyle(style);
		cell = row.createCell(11);
		cell.setCellValue("有效工作时间大于9小时");
		cell.setCellStyle(style);
		cell = row.createCell(12);
		cell.setCellValue("缺勤天数");
		cell.setCellStyle(style);
		cell = row.createCell(13);
		cell.setCellValue("刷卡不完整天数");
		cell.setCellStyle(style);
		cell = row.createCell(14);
		cell.setCellValue("平均有效工作时长");
		cell.setCellStyle(style);

		Iterator<Entry<String, EmployeeMessage>> iter = map.entrySet()
				.iterator();
		int i = 0;
		// 第五步，写入实体数据 
		while (iter.hasNext()) {
			Entry<String, EmployeeMessage> entry = iter.next();
			EmployeeMessage val = entry.getValue();
			row = sheet.createRow((int) i + 1);
			i++;
			row.createCell(0).setCellValue(val.getNumber());
			row.createCell(1).setCellValue(val.getName());
			row.createCell(2).setCellValue(val.getFirstLevDep());
			row.createCell(3).setCellValue(val.getSecondLevDep());
			row.createCell(4).setCellValue(val.getThirdLevDep());
			row.createCell(5).setCellValue(val.getTimeBefore930());
			row.createCell(6).setCellValue(val.getTimeAfter930());
			row.createCell(7).setCellValue(val.getTimeAfter940());
			row.createCell(8).setCellValue(val.getTimeAfter950());
			row.createCell(9).setCellValue(val.getTimeAfter10());
			row.createCell(10).setCellValue(val.getTimeLessThan9());
			row.createCell(11).setCellValue(val.getTimeMoreThan9());
			row.createCell(12).setCellValue(val.getTimeAbsence());
			row.createCell(13).setCellValue(val.getTimeIncomplete());
			if(val.getWorkTime() == 0){
				row.createCell(14).setCellValue(0);
			}else{
				b = new BigDecimal((val.getWorkTimeOfTotal()-val.getWorkTime()-val.getTimeAfter10())/val.getWorkTime());
				row.createCell(14).setCellValue(b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
			}

		}

		// 第六步，将文件存到指定位置
		try {

			// path = sb.append(UrlInfo.URL_EXCEL + fileName+new
			// SimpleDateFormat(“yyyy-MM-dd_HH-mm-ss”).format(new
			// java.util.Date())+”.xls”);
			// 最好以活动名称和时间作为文件名,UrlInfo.URL_EXCEL代表存放路径，由于文件名称不能有转义等特殊字符，所以日期时间不能用yyyy-MM-dd_HH:mm:ss命名/
//			File f = new File("E:/考勤表.xls");
			File f = new File(sb.toString());
			if (f.exists()) {
				f.delete();
			}
			// FileOutputStream fout = new FileOutputStream(path.toString());
			//FileOutputStream fout = new FileOutputStream("E:/考勤表.xls");
//System.out.println(sb.toString());
			FileOutputStream fout = new FileOutputStream(sb.toString());
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
