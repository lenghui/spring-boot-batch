package com.test;


public class MainTest {

	String time = null;
	
	final String test = "tiem:"+ time;
	
	public static void main(String[] args) {
//		MainTest test = new MainTest();
//		test.check("hello");
		System.out.println("hello");
	}
	
	private String check(String time) {
		this.time = time;
		System.out.println(test);
		return null;
	}
}
