package datasets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class IData {
	/** Loads this data from a bytes representation.
	 * @param bytes The byte representation to load from
	 * @return This {@link IData}
	 * @see #write() */
	public abstract IData read(byte[] bytes);
	/** Write this {@link IData} to a byte array and return it.<br/>
	 * @return The bytes representation of this {@link IData}
	 * @see #read() */
	public abstract byte[] write();
	
	/** Finds the size of this {@link IData}. Used in reading, where the object may not yet know its size.<br/>
	 * Should be equal to the size of the array returned by {@link #write()}
	 * @param data The byte array to extract a size from, if necessary
	 * @param start The starting point to read from the array
	 * @return The size in bytes */
	public int getLength(byte[] data, int start) {
		return this.getLength();
	}
	/** Finds the size of this {@link IData}. Used in writing, when an object should always know its size.<br/>
	 * Should be equal to the size of the array returned by {@link #write()}
	 * @return The size in bytes */
	public abstract int getLength();

	private static final ArrayList<Class<? extends IData>> ID_MAPPING = new ArrayList<Class<? extends IData>>();
	static {
		register(DataObject.class);
		register(DataByte.class);
		register(DataShort.class);
		register(DataInt.class);
		register(DataLong.class);
		register(DataByteArray.class);
		register(DataIntArray.class);
		register(DataKeyVal.class);
		register(DataMap.class);
		register(DataBoolArray.class);
		register(DataWrapper.class);
		register(DataArray.class);
		register(DataBool.class);
		register(DataFixedArray.class);
	}

	public static final boolean register(Class<? extends IData> type) {
		if(!type.isAnnotationPresent(Data.class)) throw new IllegalArgumentException("type must be annotated with @Data");
		Data data = type.getAnnotation(Data.class);

		short id = data.id();
		if(id < ID_MAPPING.size()) {
			Class<? extends IData> current = getTypeFromID(id);
			if(current != null) {
				if(current.equals(type)) return false; // This data type has already been registered
				System.err.println("Warning: ID " + id + " already has an IData type (" + current.toString() + "). Overwriting with " + type.toString());
			}
		} else {
			for(int i = ID_MAPPING.size(); i <= id; i++) {
				ID_MAPPING.add(null); // The ArrayList isn't long enough - we need to pad it out to the correct length
			}
		}
		ID_MAPPING.set(id, type);
		return true;
	}

	public static final Class<? extends IData> getTypeFromID(short id) {
		return ID_MAPPING.size() <= id ? null : ID_MAPPING.get(id);
	}
	public static final short getIDFromType(Class<? extends IData> type) {
		for(short i = 0; i < Short.MAX_VALUE; i++) {
			Class<? extends IData> testType = getTypeFromID(i);
			if(testType != null && testType.equals(type)) return i;
		}
		return -1;
	}

	public static final IData createInstance(short id) {
		Class<? extends IData> type = getTypeFromID(id);
		if(type == null) throw new IllegalArgumentException("id must be a valid registered id");

		Data data = type.getAnnotation(Data.class);
		try {
			Method method = type.getDeclaredMethod(data.instance());
			if(IData.class.isAssignableFrom(method.getReturnType())) {
				method.setAccessible(true);
				return (IData) method.invoke(null);
			}
		} catch (NoSuchMethodException e) {}
			catch (SecurityException e) {}
			catch (IllegalAccessException e) {}
			catch (IllegalArgumentException e) {}
			catch (InvocationTargetException e) {}
		
		return null;
	}

	/** Trims a byte array down to the specified range, where {@code start} is inclusive and {@code end} is exclusive.
	 * @param bytes The original array to trim
	 * @param start The start index in the array to copy, inclusive
	 * @param end The end index in the array to copy, exclusive
	 * @return A trimmed byte array. */
	public static final byte[] trim(byte[] bytes, int start, int end) {
		if(start < 0) throw new IllegalArgumentException("start must be greater than 0");
		if(end > bytes.length) throw new IllegalArgumentException("end must be less than or equal to the length of bytes (byte length is " + bytes.length + ", end input is " + end + ", start is " + start + ")");

		byte[] newBytes = new byte[end - start];
		for(int i = start; i < end; i++) {
			newBytes[i- start] = bytes[i];
		}
		return newBytes;
	}

	/** Inserts the {@code source} array into the {@code dest} array, beginning from {@code start} in the {@code dest} array, inclusive.
	 * @param dest The destination array to copy to
	 * @param source The source array to copy from
	 * @return An array which has been partially replaced by the source array, starting from the specified point */
	public static final byte[] untrim(byte[] dest, byte[] source, int start) {
		if(start < 0) throw new IllegalArgumentException("start must be greater than 0");
		if(start + source.length > dest.length) throw new IllegalArgumentException("source array must fit inside destination array");

		byte[] newBytes = dest.clone();
		for(int i = 0; i < source.length; i++) {
			newBytes[start + i] = source[i];
		}
		return newBytes;
	}
}
