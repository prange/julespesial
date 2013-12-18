package js.model;

import fj.Func;
import fj.data.Option;
import js.Sekk2;

public class CastToSekk implements Func<Object,Option<Sekk2>> {

    private final Class<Sekk2> type;

    public CastToSekk() {
        this.type = Sekk2.class;
    }

    @Override public Option<Sekk2> f(Object o) {
        if(type.isInstance( o ))
            return Option.some( type.cast(o) );
        else
            return Option.none();
    }
}
