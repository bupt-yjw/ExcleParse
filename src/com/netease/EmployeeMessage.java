package com.netease;

public class EmployeeMessage {
	private String number;
	private String name;
	private String firstLevDep;
	private String secondLevDep;
	private String thirdLevDep;
	private int timeBefore930;
	private int timeAfter930;
	private int timeAfter940;
	private int timeAfter950;
	private int timeAfter10;
	private int timeLessThan9;
	private int timeAbsence;
	private int timeIncomplete;
	private int timeMoreThan9;
	private double workTimeOfTotal;//记录平均有效工作总时长
	private int workTime;//记录有效工作的天数
	
	public EmployeeMessage(String number, String name,String firstLevDep,String secondLevDep,String thirdLevDep){
		setNumber(number);
		setName(name);
		setFirstLevDep(firstLevDep);
		setSecondLevDep(secondLevDep);
		setThirdLevDep(thirdLevDep);
	}
	
	
	
	public int getTimeBefore930() {
		return timeBefore930;
	}
	public void setTimeBefore930(int timeBefore930) {
		this.timeBefore930 = timeBefore930;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstLevDep() {
		return firstLevDep;
	}
	public void setFirstLevDep(String firstLevDep) {
		this.firstLevDep = firstLevDep;
	}
	public String getSecondLevDep() {
		return secondLevDep;
	}
	public void setSecondLevDep(String secondLevDep) {
		this.secondLevDep = secondLevDep;
	}
	public String getThirdLevDep() {
		return thirdLevDep;
	}
	public void setThirdLevDep(String thirdLevDep) {
		this.thirdLevDep = thirdLevDep;
	}
	public int getTimeAfter10() {
		return timeAfter10;
	}
	public void setTimeAfter10(int timeAfter10) {
		this.timeAfter10 = timeAfter10;
	}
	public int getTimeLessThan9() {
		return timeLessThan9;
	}
	public void setTimeLessThan9(int timeLessThan9) {
		this.timeLessThan9 = timeLessThan9;
	}
	public int getTimeAbsence() {
		return timeAbsence;
	}
	public void setTimeAbsence(int timeAbsence) {
		this.timeAbsence = timeAbsence;
	}
	public int getTimeIncomplete() {
		return timeIncomplete;
	}
	public void setTimeIncomplete(int timeIncomplete) {
		this.timeIncomplete = timeIncomplete;
	}

	public int getTimeAfter930() {
		return timeAfter930;
	}

	public void setTimeAfter930(int timeAfter930) {
		this.timeAfter930 = timeAfter930;
	}

	public int getTimeAfter940() {
		return timeAfter940;
	}

	public void setTimeAfter940(int timeAfter940) {
		this.timeAfter940 = timeAfter940;
	}

	public int getTimeAfter950() {
		return timeAfter950;
	}

	public void setTimeAfter950(int timeAfter950) {
		this.timeAfter950 = timeAfter950;
	}

	public int getTimeMoreThan9() {
		return timeMoreThan9;
	}

	public void setTimeMoreThan9(int timeMoreThan9) {
		this.timeMoreThan9 = timeMoreThan9;
	}

	public double getWorkTimeOfTotal() {
		return workTimeOfTotal;
	}

	public void setWorkTimeOfTotal(double workTimeOfTotal) {
		this.workTimeOfTotal = workTimeOfTotal;
	}

	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}
}
