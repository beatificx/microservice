package org.beatific.microservice.container.executor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StreamTest {

//	@Test
	public void test() {

		List<String> list = new ArrayList<String>();

		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");

		System.out.println("list " + list);

		Thread t1 = new Thread() {
			public void run() {
				for (int i = 0; i < 5; i++){
					List l2 = new ArrayList();
					l2.addAll(list);
					l2.stream().forEach(s -> {
						try {
							Thread.sleep(1000);
							System.out.println(s);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
			}
		};

		Thread t2 = new Thread() {
			public void run() {
				for (int i = 0; i < 10; i++)
					try {
						Thread.sleep(500);
						list.add(new Integer(i * 100).toString());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};

//		Thread t3 = new Thread() {
//			public void run() {
//				for (Integer i = 0; i < 10; i++)
//					try {
//						Thread.sleep(500);
//						list.remove(i.toString());
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			}
//		};

		t1.start();
		t2.start();
//		t3.start();
		
		try {
			t1.join();
			t2.join();
//			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
