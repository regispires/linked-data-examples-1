import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class SimpleQuery {
    public static void main(String[] args) {
        String serviceURI = "http://wifo5-03.informatik.uni-mannheim.de/drugbank/sparql";
        String queryString = "select * where { ?x ?y ?z } limit 40 ";
        System.out.println("query : " + queryString);

        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results, query);
        qe.close();
    }
}
