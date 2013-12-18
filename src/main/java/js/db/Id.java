package js.db;

/**
 * Represent an id.
 * @author atlosm
 *
 */
public class Id<A> {

    public final String key;
    public final A value;


    public Id(String key, A value) {
        this.key = key;
        this.value = value;
    }
}
