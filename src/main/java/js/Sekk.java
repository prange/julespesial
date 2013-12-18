package js;

import fj.data.Stream;

public class Sekk {

    public final String navn;
    public final Stream<Gave> gaver;

    public Sekk(String navn, Stream<Gave> gaver) {
        this.navn = navn;
        this.gaver = gaver;
    }

    public Sekk leggTilGave(Gave gave){
        return new Sekk(navn,gaver.cons( gave ));
    }
}
