package js.db;


import java.util.Map;

/**
 * Delete operation
 * @author atlosm
 *
 * @param <T>
 */
public class Delete<T> implements DbOp<Id<T>> {

	public final Id<T> value;

	public Delete(Id<T> value) {
		this.value = value;
	}

    @Override public Id<T> run(Map<String, Object> stor) {
        stor.remove(value.key);
        return value;
    }
}
