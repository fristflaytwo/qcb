package com.xionger.qcb.common;

public class Test {

	private static String v_sz300413="51~快乐购~300413~0.00~20.29~0.00~0~0~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~0.00~0~~20170704150133~0.00~0.00~0.00~0.00~0.00/0/0~0~0~0.00~128.14~S~0.00~0.00~0.00~40.31~81.36~0.00~22.32~18.26~0.00";
	
	public static void main(String[] args) {
		String[] infos=v_sz300413.split("~");
		int i=0;
		for(String str:infos){
			System.out.println(i+"\t:"+str);
			i++;
		}
		
	}
}
