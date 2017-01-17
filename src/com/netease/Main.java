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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	private static String number;
	private static EmployeeMessage employeeMessage;
	private static boolean changeVal = false;
	private static StringBuffer sb ;

	public static void main(String[] args) {

		final JFrame jFrame = new JFrame("����");
		final JButton jButton = new JButton("��ʼ���ɿ���ͳ�Ʊ�");
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
					jButton.setBackground(Color.green);
					jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				
			}
		});

		/*File file = new File("E:/1.xls");

		try {
			Map<String, EmployeeMessage> map1 = new HashMap<String, EmployeeMessage>();
			map1 = readExcel(file);
			System.out.println("length" + map1.size());
			wirteExcel(map1, "wyj");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public static Map<String, EmployeeMessage> readExcel(File file1)
			throws Exception {
		
		Map<String, EmployeeMessage> map = new HashMap<String, EmployeeMessage>();
		Workbook wb = null;
		try {
			InputStream inputStream = new FileInputStream(file1);
			String fileName = file1.getName();
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream); // ����xls��ʽ
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream); // ����xlsx��ʽ
			}
			Sheet sheet = wb.getSheetAt(0); // ��һ��������

			int firstRowIndex = sheet.getFirstRowNum() + 1; // �õ���һ�е��±꣬�˴���2Ŀ����Ϊ�˱����ȡ�����
			int lastRowIndex = sheet.getLastRowNum(); // �õ����һ�е��±� ������֮�����������
			sb = new StringBuffer("");
			sb.append("E:/");
			sb.append(fileName.split(".xls")[0]+"-");
			if (sheet.getRow(firstRowIndex) != null
					&& sheet.getRow(firstRowIndex).getCell(4).toString().length() > 0) {
				sb.append(sheet.getRow(firstRowIndex).getCell(0).toString().split("-")[0]);
				sb.append("-");
				sb.append(sheet.getRow(firstRowIndex).getCell(0).toString().split("-")[1]);
			}
			sb.append("���ڱ�.xls");
			// ѭ����ȡ����
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
				Row row = null;
				if (sheet.getRow(rIndex) != null
						&& sheet.getRow(rIndex).getCell(4).toString().length() > 0) {
					row = sheet.getRow(rIndex);// �õ�һ������
					if (row.getCell(4) != null) {
						number = row.getCell(4).toString();
						if (!map.containsKey(number)) {
							map.put(number, new EmployeeMessage(number, row
									.getCell(3).toString(), row.getCell(6)
									.toString(), row.getCell(7).toString(), row
									.getCell(8).toString()));
						}
					}

					/*
					 * if("������".equals(row.getCell(1).toString())||"������".equals(row
					 * .getCell(1).toString())){ continue; }else{
					 */
					employeeMessage = map.get(number);
					if (row.getCell(2).toString().contains("����")) {
						employeeMessage.setTimeAbsence(employeeMessage
								.getTimeAbsence() + 1);
						map.put(number, employeeMessage);

					} else if (row.getCell(2).toString().contains("��Ϣ")
							|| row.getCell(2).toString().contains("���ݼ�")) {
						continue;
					} else if (row.getCell(2).toString().contains("ȱ��")) {
						if (Integer.valueOf(row.getCell(15).toString()
								.split(":")[0]) >= 10) {
							employeeMessage.setTimeAfter10(employeeMessage
									.getTimeAfter10() + 1);
						}
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					}else if( row.getCell(2).toString().contains("ȱ��")){
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					} else {
						if (row.getCell(14).toString().equals("0")
								|| row.getCell(15).toString().length() <= 0) {

						} else {
							if (Double.valueOf(row.getCell(14).toString())
									.compareTo(9.0) < 0) {
								employeeMessage
										.setTimeLessThan9(employeeMessage
												.getTimeLessThan9() + 1);
								changeVal = true;
							}
							if (Integer.valueOf(row.getCell(15).toString()
									.split(":")[0]) >= 10) {
//System.out.println("-------"+row.getCell(15).toString());
								employeeMessage.setTimeAfter10(employeeMessage
										.getTimeAfter10() + 1);
								changeVal = true;
							}
							if (changeVal) {
								changeVal = false;
								map.put(number, employeeMessage);
							}
						}

					}

				} else {
					continue;
				}
/*System.out.println(row.getCell(1).toString()
						+ row.getCell(2).toString());*/

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String wirteExcel(Map<String, EmployeeMessage> map,
			String fileName) {
		HSSFRow row = null;
		// ��һ��������һ��webbook����Ӧһ��Excel��xslΪ��չ���ļ�
		HSSFWorkbook wb = new HSSFWorkbook();
		// �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet
		HSSFSheet sheet = wb.createSheet("ͳ�Ʊ�");
		// ����������sheet����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		// HSSFDataFormat dataFormat = wb.createDataFormat();����ת�������Բ���
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // ����һ��������ʽ

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("����");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("����");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("һ������");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("��������");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("��������");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("10��������");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("ˢ������9Сʱ����");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("ȱ������");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("ˢ������������");
		cell.setCellStyle(style);

		Iterator<Entry<String, EmployeeMessage>> iter = map.entrySet()
				.iterator();
		int i = 0;
		// ���岽��д��ʵ������ ʵ��Ӧ������Щ���ݴ����ݿ�õ���
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
			row.createCell(5).setCellValue(val.getTimeAfter10());
			row.createCell(6).setCellValue(val.getTimeLessThan9());
			row.createCell(7).setCellValue(val.getTimeAbsence());
			row.createCell(8).setCellValue(val.getTimeIncomplete());

		}

		// �����������ļ��浽ָ��λ��
		try {

			// path = sb.append(UrlInfo.URL_EXCEL + fileName+new
			// SimpleDateFormat(��yyyy-MM-dd_HH-mm-ss��).format(new
			// java.util.Date())+��.xls��);
			// ����Ի���ƺ�ʱ����Ϊ�ļ���,UrlInfo.URL_EXCEL������·���������ļ����Ʋ�����ת��������ַ�����������ʱ�䲻����yyyy-MM-dd_HH:mm:ss����/
//			File f = new File("E:/���ڱ�.xls");
			File f = new File(sb.toString());
			if (f.exists()) {
				f.delete();
			}
			// FileOutputStream fout = new FileOutputStream(path.toString());
			//FileOutputStream fout = new FileOutputStream("E:/���ڱ�.xls");
System.out.println(sb.toString());
			FileOutputStream fout = new FileOutputStream(sb.toString());
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

}
