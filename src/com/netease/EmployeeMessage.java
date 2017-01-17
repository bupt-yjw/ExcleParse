package com.netease;

public class EmployeeMessage {
	private String number;
	private String name;
	private String firstLevDep;
	private String secondLevDep;
	private String thirdLevDep;
	private int timeAfter10;
	private int timeLessThan9;
	private int timeAbsence;
	private int timeIncomplete;
	
	public EmployeeMessage(String number, String name,String firstLevDep,String secondLevDep,String thirdLevDep){
		setNumber(number);
		setName(name);
		setFirstLevDep(firstLevDep);
		setSecondLevDep(secondLevDep);
		setThirdLevDep(thirdLevDep);
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

}
