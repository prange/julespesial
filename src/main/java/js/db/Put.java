package js.db;

import fj.Func;

import java.util.Map;

/**
 * Puts an object
 * @author atlosm
 *
 * @param <T>
 */
public class Put<T> extends DbOp<T>{
	public String id;
    public final T value;
	
	public Put(String id,T value) {
        this.id = id;
		this.value = value;
	}

    @Override public T run(Map<String, Object> stor) {
        stor.put(id,value);
        return value;
    }

    public static <A> DbOp<A> put(String id, A value){
        return new Put<>(id,value);
    }

    public static <A> Func<A,DbOp<A>> put(final String id){
        return (idA) -> new Put<>(id, idA);
    }
}
