package js.model;

import fj.data.Option;
import js.Gave2;

public class Predicates {

    public static Predicate<Gave2> sekkPredicateFor(Option<Double> maybeD){
        return maybeD.fold( () -> new AlwaysTruePredicate(), OverWeightPredicate::new );
    }

}
