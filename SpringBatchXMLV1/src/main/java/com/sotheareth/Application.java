package com.sotheareth;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Application {
	public static void main(String[] args) {
		// test data
		AbstractApplicationContext  context = new ClassPathXmlApplicationContext("application-context.xml");
		context.registerShutdownHook();
		System.out.println("end");
	}
}
