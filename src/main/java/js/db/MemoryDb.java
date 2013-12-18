package js.db;


import java.util.HashMap;

public class MemoryDb {

    private final HashMap<String,Object> store = new HashMap<>();


    public <A> A run(final DbOp<A> op){
        return op.run( store );
    }

}
