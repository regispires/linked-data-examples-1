import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class SimpleQuery2 {
    public static void main(String[] args) {
        String serviceURI = "http://www4.wiwiss.fu-berlin.de/drugbank/sparql";
        String queryString = "select * where { ?x ?y ?z } limit 40 ";
        System.out.println("query : " + queryString);

        QueryEngineHTTP queryExecution = new QueryEngineHTTP(serviceURI, queryString);
        ResultSet results = queryExecution.execSelect();
        ResultSetFormatter.out(System.out, results);
        queryExecution.close();
    }
}
