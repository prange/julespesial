package js.model;

import js.Gave2;

public class OverWeightPredicate implements Predicate<Gave2> {

    private final double threshold;

    public OverWeightPredicate(double threshold) {
        this.threshold = threshold;
    }

    @Override public Boolean f(Gave2 gave2) {
        return gave2.vekt.value>threshold;
    }
}
