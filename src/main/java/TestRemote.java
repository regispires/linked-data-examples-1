import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;

public class TestRemote {
    public static void main(String[] args) {

        String queryString = 
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
            "PREFIX dbpedia: <http://dbpedia.org/ontology/> \n"+
            
            // 'AS' keyword not SPARQL1.0 but valid on dbpedia: 
            "SELECT DISTINCT (str(?label_lang) AS ?label) ?mov WHERE { \n"+ 
            "    ?mov rdf:type dbpedia:Film. \n"+
            "    ?mov dbpedia:director ?dir. \n"+
            "    ?dir rdfs:label ?label_lang. \n"+
            
            // BIND is SPARQL1.1 only!
            "    BIND(str(?label_lang) AS ?labsel). \n"+  
            "} LIMIT 10 \n";
        
        Query myQuery = QueryFactory.create(queryString);
        
        QueryExecution qexec = QueryExecutionFactory.createServiceRequest(
                "http://dbpedia.org/sparql" , myQuery);
        
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()){
                System.out.println(rs.next());
            }
        } catch (QueryExceptionHTTP e) {
            System.err.println("<response>: ");
            System.err.println(e.getResponseMessage());
            System.err.println("</response> ");
            System.err.flush();
            e.printStackTrace();
            
        }
    }
}