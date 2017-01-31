package org.beatific.microservice.container.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CopyUtils {

	public static <T> Collection<T> copy(Collection<T> l1) {
		List<T> l2 = new ArrayList<T>();
		l2.addAll(l1);
		return l2;
	}
	
	
}
