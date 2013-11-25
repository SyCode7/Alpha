package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import biz.source_code.base64Coder.Base64Coder;

public abstract class DataTransmittingClass implements Serializable {

	private static final long serialVersionUID = 6219602439312390523L;

	public abstract String getClassAsString() throws DataTransmittingException;

	/** Read the object from Base64 string. */
	public static Object fromString(String s) throws DataTransmittingException {

		try {
			byte[] data = Base64Coder.decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (IOException | ClassNotFoundException e) {
			throw new DataTransmittingException(e.getMessage(), e.getCause());
		}
	}

	/** Write the object to a Base64 string. */
	protected static String toString(DataTransmittingClass o) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}

}
