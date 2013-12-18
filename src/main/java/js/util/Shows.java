package js.util;

import fj.Show;
import js.Weight2;

public class Shows {

    public final Show<Weight2> weightShow = Show.showS( (weight) -> weight.value+" kg" );

}
