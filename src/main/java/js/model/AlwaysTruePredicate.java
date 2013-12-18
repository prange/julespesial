package js.model;

import js.Gave2;

public class AlwaysTruePredicate implements Predicate<Gave2> {
    @Override public Boolean f(Gave2 gave2) {
        return true;
    }
}
