package js.db;

import fj.data.Option;

import java.util.Map;

/**
 * Fetch one object
 * @author atlosm
 *
 * @param <T>
 */
public class Get<T> extends DbOp<Option<Id<T>>> {

	public final Class<T> type;
	public final String key;

	public Get(Class<T> type, String key) {
		this.key = key;
		this.type = type;
	}

    @Override public Option<Id<T>> run(Map<String, Object> stor) {
        Object value = stor.get(key);
        if(type.isInstance( value ))
            return Option.some(new Id<>(key,type.cast( value )));
        return Option.none();
    }
}
