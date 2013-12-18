package js.db;

import java.util.Map;

/**
 * Does nothing
 * @author atlosm
 *
 * @param <T>
 */
public class NoOp<T> extends DbOp<T> {

	public final T value;

	public NoOp(T value) {
		this.value = value;
	}


    @Override public T run(Map<String, Object> stor) {
        return value;
    }
}
