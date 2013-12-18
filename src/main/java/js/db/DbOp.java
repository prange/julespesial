package js.db;

import fj.Func;

import java.util.Map;

/**
 * Superinterface for all db operations
 * @author atlosm
 *
 * @param <A>
 */
public abstract class DbOp<A> {

    public abstract A run(Map<String,Object> stor);

    public <B> DbOp<B> map(final Func<A,B> f){
        return new DbOp<B>() {
            @Override public B run(Map<String, Object> stor) {
                return f.f( DbOp.this.run( stor ) );
            }
        };
    }

    public <B> DbOp<B> bind(final Func<A,DbOp<B>> f){
        return new DbOp<B>() {
            @Override public B run(Map<String, Object> stor) {
                A a = DbOp.this.run( stor );
                DbOp <B> bOp = f.f(a);
                return bOp.run( stor );
            }
        };
    }

}
