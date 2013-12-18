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
                .get( "/", (request) ->
                        new StringResponse( "Julespesial" )
                )
                .get( "/hello", (request) ->
                        request
                                .getParameter( "navn" )
                                .<StringResponse>option( //Fold, dvs vi lager en ny verdi for Option avhengig av den interne tilstanden
                                        () -> new StringResponse( "Hei ukjente" ), //Lazy verdi. Hvis option er None()
                                        (navn) -> new StringResponse( "Hei " + navn ) ) //Funksjon. Hvis option er Some(navn)
                )
                .get( "/sekk", (request) -> {
                    //List ut alt innholdet av sekken som er tyngre enn parameteren vekt.
                    //Hent databasen fra context.
                    for (MemoryDb db : request.handlerContext.get( MemoryDb.class )) {

                    }
                    return new StringResponse( "No content" );
                } )
                .post( "/sekk", (request) -> {
                    //Skriv ut en requestparameter til skjerm.

                    //Hent sekken for norge

                    //Hent databasen fra context.
                    for (MemoryDb db : request.handlerContext.get( MemoryDb.class )) {

                        //Hent sekken for norge
                        Get<Sekk> getSekk = new Get<Sekk>( Sekk.class, "norge" );

                        //.... hvis sekken ikke finnes, lag en ny
                        DbOp<Id<Sekk>> op = getSekk.bind( (kanskjeEnSekk) -> {
                            if (kanskjeEnSekk.isSome())
                                return new NoOp<>( kanskjeEnSekk.some() );
                            else
                                return new Put<>( new Id<>( "norge", new Sekk( "norge", Stream.<Gave>nil() ) ) );
                        } );

                        //Legg gaver i sekken basert på parameterne type og vekt

                    }
                    return new StringResponse( "No database" );
                } );

    }
}
