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
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * �������°汾����һЩ�޸�û�������¼�
 * @author bjweiyongjun
 *
 */
public class MainTest {
	
	private static String number;  //����
	private static EmployeeMessage employeeMessage;   //�洢ÿ��Ա������Ϣ
	private static boolean changeVal = false; 	//�����ж��Ƕ������ݸ��£��Ӷ�ȷ���Ƿ����employeeMessage����ֶ�
	private static StringBuffer sb ;
	private static int i=0;//��λ����ʹ��
	private static double workTimeOfTotal = 0;   //������ʱ��
	private static int normalDay = 0;		//������������
	private static String jobNumber = null;	//�洢����
	private static BigDecimal b;
	private static Map<String, EmployeeMessage> map = new HashMap<String, EmployeeMessage>();//���ÿ��Ա��ͳ�ƺõ���Ϣ
	private static int firstRowIndex; // ��¼��һ�е��±꣬�˴���2Ŀ����Ϊ�˱����ȡ�����
	private static int lastRowIndex; // ��¼���һ�е��±� ������֮�����������
	
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
					//Map<String, EmployeeMessage> map1 = new HashMap<String, EmployeeMessage>();
					try {
						//map = readExcel(readfile);
						 readExcel(readfile);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					wirteExcel(map, "wyj");
					jButton.setEnabled(false);
					jButton.setBackground(Color.green);
					jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				
			}
		});

	}

	public static Map<String, EmployeeMessage> readExcel(File file)
			throws Exception {
		
		Workbook wb = null;
		try {
			InputStream inputStream = new FileInputStream(file);
			String fileName = file.getName();
			if (fileName.endsWith("xls")) {
				wb = new HSSFWorkbook(inputStream); // ����xls��ʽ
			} else if (fileName.endsWith("xlsx")) {
				wb = new XSSFWorkbook(inputStream); // ����xlsx��ʽ
			}
			
			Sheet sheet = wb.getSheetAt(0); // ��һ��������

			creatExcleName(sheet,fileName);
			
			// ѭ����ȡ����
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
			
				Row row = null;
				try {
				if (sheet.getRow(rIndex) != null
						&& sheet.getRow(rIndex).getCell(4).toString().length() > 0) {
					row = sheet.getRow(rIndex);// �õ�һ������
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
					
					avgWorkTime(row);
					
					if (row.getCell(2).toString().contains("����")) {
						employeeMessage.setTimeAbsence(employeeMessage
								.getTimeAbsence() + 1);
						map.put(number, employeeMessage);

					} else if (row.getCell(2).toString().contains("���")||row.getCell(2).toString().contains("��Ϣ")
							|| row.getCell(2).toString().contains("���ݼ�")||row.getCell(2).toString().contains("��н����")) {
						continue;
					} else if (row.getCell(2).toString().contains("ȱ��")) {
						lateDay(row);
						
						
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					}else if( row.getCell(2).toString().contains("ȱ��")){
						employeeMessage.setTimeIncomplete(employeeMessage
								.getTimeIncomplete() + 1);
						map.put(number, employeeMessage);
					} else {
						
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
//��Ҫ�������Զ�λ������				
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
	 * ����������ɵ�excle������
	 * @param sheet
	 * @param fileName
	 */
	private static void creatExcleName(Sheet sheet,String fileName) {
		 firstRowIndex = sheet.getFirstRowNum() + 1; // �õ���һ�е��±꣬�˴���2Ŀ����Ϊ�˱����ȡ�����
		 lastRowIndex = sheet.getLastRowNum(); // �õ����һ�е��±� ������֮�����������
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
	}

	/**
	 * ͳ��ƽ������ʱ��
	 * (��������ʱ��-1)��ƽ��ֵ
	 * @param row
	 */
	private static void avgWorkTime(Row row){
		if(jobNumber == null || jobNumber.length()<=0){
			jobNumber = number;
		}
		if(number.equals(jobNumber)){
			if (row.getCell(14)==null||row.getCell(14).toString().equals("0.0")
					||row.getCell(14).toString().equals("")) {
			}else{
				workTimeOfTotal += Double.valueOf(row.getCell(14).toString());
				normalDay +=1;
			}
		/*	if(row.getCell(14)!= null){
				System.out.println(row.getCell(14).toString());
			}*/
		}else{
			if(normalDay == 0){
				map.get(jobNumber).setAvgWorkTime(0);
			}else{
			b = new BigDecimal((workTimeOfTotal-normalDay-map.get(jobNumber).getTimeAfter10())/normalDay);  
			map.get(jobNumber).setAvgWorkTime(b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
		}
			jobNumber = number;
			workTimeOfTotal = 0;
			normalDay = 0;
		}
	}
	
	/**
	 * ͳ�Ƴٵ�����
	 * @param row
	 */
	private static void lateDay(Row row) {
		 if(Integer.valueOf(row.getCell(15).toString()
					.split(":")[0]) >= 10) {
				 if(Integer.valueOf(row.getCell(15).toString()
					.split(":")[1]) > 0){
					 employeeMessage.setTimeAfter10(employeeMessage
								.getTimeAfter10() + 1); 
				 }else{
					 employeeMessage.setTimeAfter950(employeeMessage
								.getTimeAfter950() + 1);
				 }
			}else if (Integer.valueOf(row.getCell(15).toString()
					.split(":")[0]) == 9 && Integer.valueOf(row.getCell(15).toString()
							.split(":")[1]) > 50 ) {
				employeeMessage.setTimeAfter950(employeeMessage
						.getTimeAfter950() + 1);
			}else if (Integer.valueOf(row.getCell(15).toString()
					.split(":")[0]) == 9 && Integer.valueOf(row.getCell(15).toString()
							.split(":")[1]) > 40 ) {
				employeeMessage.setTimeAfter940(employeeMessage
						.getTimeAfter940() + 1);
			}else if (Integer.valueOf(row.getCell(15).toString()
					.split(":")[0]) == 9 && Integer.valueOf(row.getCell(15).toString()
							.split(":")[1]) > 30 ) {
				employeeMessage.setTimeAfter930(employeeMessage
						.getTimeAfter930() + 1);
			}
		 changeVal = true;
	}

	/**
	 * ��ͳ�ƺõ�����д��excle�У��ļ�������sb����
	 * @param map
	 * @param fileName  Ԥ���ֶ�
	 * @return
	 */
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
		cell.setCellValue("9:30�������");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("9:40�������");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("9:50�������");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("10��������");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("��Ч����ʱ�䲻��8Сʱ");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("��Ч����ʱ�����9Сʱ");
		cell.setCellStyle(style);
		cell = row.createCell(11);
		cell.setCellValue("ȱ������");
		cell.setCellStyle(style);
		cell = row.createCell(12);
		cell.setCellValue("ˢ������������");
		cell.setCellStyle(style);
		cell = row.createCell(13);
		cell.setCellValue("ƽ����Ч����ʱ��");
		cell.setCellStyle(style);

		Iterator<Entry<String, EmployeeMessage>> iter = map.entrySet()
				.iterator();
		int i = 0;
		// ���岽��д��ʵ������ 
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
			row.createCell(5).setCellValue(val.getTimeAfter930());
			row.createCell(6).setCellValue(val.getTimeAfter940());
			row.createCell(7).setCellValue(val.getTimeAfter950());
			row.createCell(8).setCellValue(val.getTimeAfter10());
			row.createCell(9).setCellValue(val.getTimeLessThan9());
			row.createCell(10).setCellValue(val.getTimeMoreThan9());
			row.createCell(11).setCellValue(val.getTimeAbsence());
			row.createCell(12).setCellValue(val.getTimeIncomplete());
			row.createCell(13).setCellValue(val.getAvgWorkTime());

		}

		// �����������ļ��浽ָ��λ��
		try {

			File f = new File(sb.toString());
			if (f.exists()) {
				f.delete();
			}
			// FileOutputStream fout = new FileOutputStream(path.toString());
			//FileOutputStream fout = new FileOutputStream("E:/���ڱ�.xls");
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
