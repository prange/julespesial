package js.db;

import java.util.Map;

/**
 * Puts an object
 * @author atlosm
 *
 * @param <T>
 */
public class Put<T> extends DbOp<Id<T>>{
	public final Id<T> value;
	
	public Put(Id<T> value) {
		this.value = value;
	}

    @Override public Id<T> run(Map<String, Object> stor) {
        stor.put(value.key,value.value);
        return value;
    }
}
