package js;

import fj.data.Stream;

public class Sekk2 {

    public final String navn;
    public final Stream<Gave2> gaver;

    public Sekk2(String navn, Stream<Gave2> gaver) {
        this.navn = navn;
        this.gaver = gaver;
    }

    public static Sekk2 nyTomSekk(String navn){
        return new Sekk2( navn,Stream.<Gave2>nil() );
    }

    public Sekk2 leggTilGave(Gave2 gave2){
        return new Sekk2(navn,gaver.cons( gave2 ));
    }
}
