package datasets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Data {
	/** @return A unique ID for this type of {@link IData} */
	public short id();
	/** A method name, used to generate default instances.<br/>
	 * The method must not take any arguments and must return an instance of {@link IData}.
	 * @return The method name which will yield a default instance of this {@link IData} */
	public String instance();
}
