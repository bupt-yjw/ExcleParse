package com.main;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Main{
	public static void main(String[] args){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		while(true){
			System.out.println(df.format(new Date())+"---"+args[0]);// new Date()Ϊ��ȡ��ǰϵͳʱ��			
		}
	}
}
