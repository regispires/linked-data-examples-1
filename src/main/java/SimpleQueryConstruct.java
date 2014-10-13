import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class SimpleQueryConstruct {
    public static void main(String[] args) {
    	QueryExecution qe = null;
    	try {
            String serviceURI = "http://wifo5-03.informatik.uni-mannheim.de/drugbank/sparql";
            String queryString = "CONSTRUCT { ?s ?p ?o } where { ?s ?p ?o } limit 40 ";
            System.out.println("query : " + queryString);

            Query query = QueryFactory.create(queryString);
            qe = QueryExecutionFactory.sparqlService(serviceURI, query);
            Model model = qe.execConstruct();
			FileOutputStream out = new FileOutputStream(new File("/tmp/arquivo.nt"));
	        model.write(out, "N-TRIPLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (qe != null) {
		        qe.close();
			}
		}
    }
}
