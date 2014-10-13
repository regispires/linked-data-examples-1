import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDFS;


public class DanaiExample {
    public static void main( String[] args ) {
        // set up a test model
        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        String ns = "http://example.com/test#";
        OntClass a = m.createClass( ns + "A" );
        OntClass b = m.createClass( ns + "B" );
        OntClass c = m.createClass( ns + "C" );

        OntProperty p = m.createOntProperty( ns + "p" );
        OntProperty q = m.createOntProperty( ns + "q" );

        a.addSubClass( b );
        b.addSubClass( c );

        p.addRange( b );
        q.addRange( a );
        q.addRange( c );

        // the query we want to test
        String qStr = "prefix rdfs: <" + RDFS.getURI() + ">"  +
                "select distinct ?p where {\n" +
                " {?p rdfs:range ?cls}\n" +
                " union\n" +
                " {?p rdfs:range ?d.\n" +
                "  ?cls rdfs:subClassOf ?d}\n" +
                "}";

        // bind ?cls to example:B
        QuerySolutionMap preBindings = new QuerySolutionMap();
        preBindings.add( "cls", b );

        // run the query
        Query query = QueryFactory.create( qStr );
        QueryExecution exec = QueryExecutionFactory.create(query, m, preBindings );
        ResultSet results = exec.execSelect();

        while (results.hasNext()) {
            System.out.println( "Solution: " + results.next() );
        }
    }
}

