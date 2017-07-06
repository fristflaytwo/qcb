package com.xionger.qcb.common;

public class Test {

	private static String v_sz300413="51~立立电子~002257~0.00~0.00~0.00~0~0~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~~20170706150136~0.00~0.00~0.00~0.00~0.00/0/0~0~0~~~~0.00~0.00~0.00~~~0.00~0.00~0.00~0.00";
	
	public static void main(String[] args) {
		String[] infos=v_sz300413.split("~");
		int i=0;
		for(String str:infos){
			System.out.println(i+"\t:"+str);
			i++;
		}
		
	}
}
