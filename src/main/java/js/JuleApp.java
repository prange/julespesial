package js;


import fj.Func;
import fj.data.Option;
import fj.data.Stream;
import fj.function.Doubles;
import js.db.*;
import js.model.AlwaysTruePredicate;
import js.model.CastToSekk;
import js.model.OverWeightPredicate;
import js.model.Predicate;
import peak.RouteBuilder;
import peak.WebApplication;
import peak.lifecycle.Register;
import peak.request.Request;
import peak.response.StringResponse;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static js.model.Predicates.sekkPredicateFor;

public class JuleApp extends WebApplication {

    protected Func<ServletContext, ServletContext> initContext() {
        return Register.inContext( new MemoryDb() )
                .andThen( Register.inContext( new HashMapStore() ) );
    }

    protected RouteBuilder init(RouteBuilder builder) {
        return builder
                .get( "/", (request) ->
                        new StringResponse( "Julespesial" )
                )
                /*
                *
                * 1a
                * =================================
                * Første versjon av /sekk bruker idiomatisk java til å hente ut sekken fra en HashMap.
                */
                .get( "/sekk1a", (request) -> {
                    HashMap<String, Object> store =
                            request.handlerContext.get( HashMapStore.class ).some().store;

                    if (store == null)
                        return new StringResponse( "No store" );

                    Sekk1 sekk =
                            (Sekk1) store.get( "norge" );

                    //Henter ut requestparameter vekt fra den rå servlet requesten
                    String grense =
                            request.underlying.getParameter( "vekt" );

                    //Hvis grensen er null så returerer vi en liste med alle gavene
                    if (grense == null) {
                        Set<String> setOfTypes = new HashSet<String>();

                        for (Gave1 gave : sekk.getGaver()) {
                            setOfTypes.add( gave.getType() );
                        }
                        return new StringResponse( setOfTypes.toString() );
                    }
                    else {
                        double d = Double.parseDouble( grense );
                        Set<String> setOfTypes = new HashSet<String>();
                        for (Gave1 gave : sekk.getGaver()) {
                            if (gave.getWeight().getValue() > d)
                                setOfTypes.add( gave.getType() );
                        }
                        return new StringResponse( setOfTypes.toString() );
                    }
                } )
                /*
                *
                * 1b
                * =================================
                * Første versjon av /sekk bruker idiomatisk java til å hente ut sekken fra en HashMap.
                * Vi pynter litt med clean code
                */
                .get( "/sekk1b", (request) -> {
                    HashMap<String, Object> store =
                            getMap( request );

                    if (store == null)
                        return new StringResponse( "No store" );

                    Sekk1 sekk =
                            (Sekk1) store.get( "norge" );

                    //Henter ut requestparameter vekt fra den rå servlet requesten
                    String grense =
                            request.underlying.getParameter( "vekt" );

                    //Hvis grensen er null så returerer vi en liste med alle gavene
                    if (grense == null) {
                        Set<String> setOfTypes = new HashSet<String>();

                        for (Gave1 gave : sekk.getGaver()) {
                            setOfTypes.add( gave.getType() );
                        }
                        return new StringResponse( setOfTypes.toString() );
                    }
                    else {
                        double d = Double.parseDouble( grense );
                        Set<String> setOfTypes = new HashSet<String>();
                        for (Gave1 gave : sekk.getGaver()) {
                            if (gave.getWeight().getValue() > d)
                                setOfTypes.add( gave.getType() );
                        }
                        return new StringResponse( setOfTypes.toString() );
                    }
                } )
                /*
                *
                * 2
                * =================================
                * Andre versjon av sekk. Vi sikrer at vi ikke får null ved å bruke fold
                */
                .get( "/sekk2", (request) -> {
                    //Vi henter ut store
                    for (HashMapStore store : request.handlerContext.get( HashMapStore.class )) {
                        //Så henter vi ut en sekk fra store, og caster den
                        for (Sekk1 sekk : Option.fromNull( store.store.get( "norge" ) ).map( (obj) -> (Sekk1) obj )) {
                            //Så henter vi en requestparameter fra requesten, og mapper den til en double
                            for (Double d : request.getParameter( "vekt" ).bind( Doubles.fromString() )) {
                                Set<String> setOfTypes = new HashSet<String>();
                                for (Gave1 gave : sekk.getGaver()) {
                                    if (gave.getWeight().getValue() > d)
                                        setOfTypes.add( gave.getType() );
                                }
                                return new StringResponse( setOfTypes.toString() );

                            }
                            //Kommer vi hit var det ingen gyldig double i requesten
                            Set<String> setOfTypes = new HashSet<String>();

                            for (Gave1 gave : sekk.getGaver()) {
                                setOfTypes.add( gave.getType() );
                            }
                            return new StringResponse( setOfTypes.toString() );

                        }
                    }
                    //Kommer vi hit var det ingen gyldig store
                    return new StringResponse( "Store not found" );
                } )
                /*
                *
                *
                * 3
                * =================================
                * Tredje versjon av sekk.
                * Vi sikrer at vi ikke får null ved å bruke fold
                * Vi bruker filter og map på innholdet i sekken
                */
                .get( "/sekk3", (request) -> {
                    //Vi henter ut store
                    for (HashMapStore store : request.handlerContext.get( HashMapStore.class )) {
                        //Så henter vi ut en sekk fra store, og caster den
                        for (Sekk2 sekk : Option.fromNull( store.store.get( "norge2" ) ).map( (obj) -> (Sekk2) obj )) {
                            //Så henter vi en requestparameter fra requesten, og mapper den til en double
                            Func<Gave2, Boolean> filter =
                                    request
                                            .getParameter( "vekt" ) //Vi henter ut vekt
                                            .bind( Doubles.fromString() ) //Gjr det om til en double hvis mulig
                                            .fold( () -> (gave) -> true, (d) -> (gave) -> gave.vekt.value > d );

                            return new StringResponse( sekk.gaver.filter( filter ).map( (gave) -> gave.betegnelse ).toString() );

                        }
                    }
                    //Kommer vi hit var det ingen gyldig store
                    return new StringResponse( "Store not found" );
                } )
                /*
                *
                *
                * 4
                * =================================
                * Fjerde versjon av sekk.
                * Vi sikrer at vi ikke får null ved å bruke fold
                * Vi bruker filter og map på innholdet i sekken
                * Vi lager domeneobjekter for alle funksjonene
                */
                .get( "/sekk4", (request) -> {
                    //Vi henter ut store
                    for (HashMapStore store : request.handlerContext.get( HashMapStore.class )) {
                        //Så henter vi ut en sekk fra store, og caster den
                        for (Sekk2 sekk : Option.fromNull( store.store.get( "norge2" ) ).bind( new CastToSekk() )) {
                            //Så henter vi en requestparameter fra requesten, og mapper den til en double
                            Predicate<Gave2> filter =
                                    request
                                            .getParameter( "vekt" ) //Vi henter ut vekt
                                            .bind( Doubles.fromString() ) //Gjr det om til en double hvis mulig
                                            .fold( () -> new AlwaysTruePredicate(), (d) -> new OverWeightPredicate( d ) );

                            return new StringResponse( sekk.gaver.filter( filter ).map( (gave) -> gave.betegnelse ).toString() );

                        }
                    }
                    //Kommer vi hit var det ingen gyldig store
                    return new StringResponse( "Store not found" );
                } )
                 /*
                *
                *
                * 5
                * =================================
                * Femte versjon av sekk.
                * Vi sikrer at vi ikke får null ved å bruke fold
                * Vi bruker filter og map på innholdet i sekken
                * Vi lager domeneobjekter for alle funksjonene
                * Vi bruker datastrukturer for å hente data fra databasen
                */
                .get( "/sekk5", (request) -> {
                    for (MemoryDb db : request.handlerContext.get( MemoryDb.class )) {

                        //Hent sekken for norge
                        Get<Sekk2> getSekk =
                                new Get<Sekk2>( Sekk2.class, "norge" );

                        Option<Double> threshold =
                                request.getParameter( "vekt" ).bind( Doubles.fromString() );

                        DbOp<StringResponse> op =
                                getSekk.map( (kanskjeIdSekk) ->
                                        kanskjeIdSekk.fold(
                                                () -> new StringResponse( "Ingen sekk funnet" ),
                                                (idSekk) -> {
                                                    Stream<Gave2> filtrerteGaver = idSekk.gaver.filter( sekkPredicateFor( threshold ) );
                                                    return new StringResponse( filtrerteGaver.toString() );
                                                } )

                                );

                        return db.run( op );

                    }
                    return new StringResponse( "No database" );
                } )

                /*
                *
                *
                * 1
                * Idiomatisk post
                *
                *
                 */
                .post( "/sekk1", (request) -> {

                    HashMap<String, Object> store =
                            getMap( request );

                    if (store == null)
                        return new StringResponse( "No store" );

                    String type = request.underlying.getParameter( "type" );
                    String vekt = request.underlying.getParameter( "vekt" );

                    if (vekt == null)
                        return new StringResponse( "Ugyldig vekt" );

                    Weight1 weight = new Weight1();
                    weight.setValue( Double.parseDouble( vekt ) );

                    Sekk1 sekk = (Sekk1) store.get( "norge" );
                    if (sekk == null) {
                        Sekk1 nySekk = new Sekk1();
                        nySekk.setName( "norge" );
                        store.put( "norge", nySekk );
                        sekk = nySekk;
                    }

                    Gave1 gave = new Gave1();
                    gave.setType( type );
                    gave.setWeight( weight );

                    sekk.getGaver().add( gave );


                    return new StringResponse( "Lagt til " + gave + " til " + sekk );
                } )
                /*
                *2
                * Funksjonell post
                *
                 */
                .post( "/sekk2", (request) -> {

                    //Hent databasen fra context.
                    for (MemoryDb db : request.handlerContext.get( MemoryDb.class )) {

                        //Hent parametere
                        Option<String> kanskjeType = request.getParameter( "type" );
                        Option<Double> kanskjeVekt = request.getParameter( "vekt" ).bind( Doubles.fromString() );
                        //Sett samme parametere til en gave
                        Option<Gave2> kanskjeGave =
                                kanskjeType.bind( kanskjeVekt, (t) -> (v) -> new Gave2( t, new Weight2( v ) ) );

                        //Hent sekken for norge
                        Get<Sekk2> getSekk =
                                new Get<>( Sekk2.class, "norge" );

                        //.... hvis sekken ikke finnes, lag en ny
                        DbOp<Sekk2> op =
                                getSekk.map( (kanskjeSekk) -> kanskjeSekk.orSome( () ->  Sekk2.nyTomSekk( "norge" ) ) );

                        //Legg til gaven hvis kanskjeGave er "some"
                        DbOp<Sekk2> update =
                               op.map( (sekk) -> kanskjeGave.isSome() ? sekk.leggTilGave( kanskjeGave.some() ) : sekk );

                        //Så lagrer vi
                        DbOp<Sekk2> store =
                                update.bind( Put.put("norge") );

                        //Så oppdaterer vi databasen
                        db.run( store );

                        return kanskjeGave.fold( () -> new StringResponse( "Ingen parametere funnet" ), (g) -> new StringResponse( g.toString() + "lagt til" ) );

                    }
                    return new StringResponse( "No database" );
                } );


    }

    private HashMap<String, Object> getMap(Request request) {
        return request.handlerContext.get( HashMapStore.class ).some().store;
    }
}
