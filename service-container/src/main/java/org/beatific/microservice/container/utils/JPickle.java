package org.beatific.microservice.container.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

public class JPickle {

	public static void clear(String filename) throws IOException {
		try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false), false)) {
			pw.flush();
		}
	}
	
	public static void dump(String filename, Object object) throws FileNotFoundException, IOException {
		try (ObjectOutputStream oos =
				new ObjectOutputStream(new FileOutputStream(filename))) {

			oos.writeObject(object);
		}
	}
	
	public static Object load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
		Object object = null;

		File file  = new File(filename);
		
		if(!file.exists())
			FileUtils.touch(file);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			
			object = ois.readObject();
		} catch (EOFException ex) {
		}

		return object;
	}
	
}
