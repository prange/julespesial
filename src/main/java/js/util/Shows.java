package js.util;

import fj.Show;
import js.Weight;

public class Shows {

    public final Show<Weight> weightShow = Show.showS( (weight) -> weight.value+" kg" );

}
