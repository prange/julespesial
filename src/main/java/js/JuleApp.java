package js;


import fj.Func;
import fj.data.Stream;
import js.db.*;
import peak.RouteBuilder;
import peak.WebApplication;
import peak.lifecycle.Register;
import peak.response.StringResponse;

import javax.servlet.ServletContext;

public class JuleApp extends WebApplication {

    protected Func<ServletContext, ServletContext> initContext() {
        return Register.inContext( new MemoryDb() );
    }

    protected RouteBuilder init(RouteBuilder builder) {
        return builder
                .get( "/", (request) -> new StringResponse( "Julespesial" ) )
                .get( "/hello", (request) ->
                        request
                                .getParameter( "navn" )
                                .<StringResponse>option( //Fold, dvs vi lager en ny verdi for Option avhengig av den interne tilstanden
                                        () -> new StringResponse( "Hei ukjente" ), //Lazy verdi. Hvis option er None()
                                        (navn) -> new StringResponse( "Hei " + navn ) ) //Funksjon. Hvis option er Some(navn)
                )
                .post( "/sekk", (request) -> {
                    //Skriv ut en requestparameter til skjerm.

                    //Hent sekken for norge

                    //Hent databasen fra context.
                    for (MemoryDb db : request.handlerContext.get( MemoryDb.class )) {

                        //Hent sekken for norge, hvis sekken ikke finnes, lag en ny
                        Get<Sekk> getSekk = new Get<Sekk>( Sekk.class,"norge" );

                        DbOp<Id<Sekk>> op = getSekk.bind( (kanskjeEnSekk)->{
                            if(kanskjeEnSekk.isSome())
                                return new NoOp<>( kanskjeEnSekk.some() );
                            else
                                return new Put<>(new Id<>("norge",new Sekk("norge", Stream.<Gave>nil())) );
                        });

                        //Lag en funksjon som henter ut alle gavetyper i en sekk;

                        //Lag en tester for sekk (sjekk test mappen).

                        //Legg gaver i sekken basert på parameterne type og vekt

                        //Lag en tabell med betgnelse på alle gavene nissen har i sekken.
                        //Logging
                    }
                    return new StringResponse( "No database" );
                } )
                .get("/sekk",(request)->{
                    //List ut alt innholdet av sekken som er tyngre enn parameteren vekt.

                    return new StringResponse( "No content" );
                });
    }
}
