package js;

import fj.Equal;
import fj.data.Stream;
import fj.test.Arbitrary;
import fj.test.Property;
import fj.test.reflect.Check;
import fj.test.reflect.Name;
import org.junit.Test;

public class Tests {

    public final Stream<Integer> ints =
            Stream.range( 1, 100 );

    public final Arbitrary<Weight2> arbWeight =
            Arbitrary.arbitrary( Arbitrary.arbDouble.gen.map( Weight2::new ) ); //Constructor reference

    @Name("En gave skal kunne ha en tilfeldig string som navn")
    Property p1 = Property.property( Arbitrary.arbAlphaNumString, arbWeight, (str) -> (weight) -> { //Curried function
        Gave2 gave2 = new Gave2( str, weight );
        return Property.prop( Equal.stringEqual.eq( gave2.betegnelse,str ));
    } );

    @Test
    public void runTest() {
        CheckResults.assertAndPrintResults( Check.check( Tests.class ) );
    }


}
