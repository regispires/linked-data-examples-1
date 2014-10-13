import java.io.ByteArrayInputStream;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.pfunction.PropertyFunctionRegistry;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SimpleQuery03 {

    public static void main(String[] args) {
        Dataset dataset = TDBFactory.createDataset();
        Model model = dataset.getDefaultModel();
        PropertyFunctionRegistry.get().remove(RDFS.member.getURI());
        String xml = "<?xml version=\"1.0\"?>"
                + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "
                + "         xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">"
                + "     <rdf:Description rdf:about=\"http://example.com/query\">"
                + "        <rdfs:member rdf:resource=\"http://example.com/bugs/5\" />"
                + "        <rdfs:member rdf:resource=\"http://example.com/bugs/4\" />"
                + "        <rdfs:member rdf:resource=\"http://example.com/bugs/3\" />"
                + "        <rdfs:member rdf:resource=\"http://example.com/bugs/2\" />"
                + "        <rdfs:member rdf:resource=\"http://example.com/bugs/1\" />"
                + "     </rdf:Description>" + "</rdf:RDF>";
        model.read(new ByteArrayInputStream(xml.getBytes()), null);
        String queryString = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "SELECT ?resource ?member WHERE { "
                + "   ?resource rdfs:member ?member " + "   } ";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
        ResultSet result = qexec.execSelect();
        ResultSetFormatter.out(System.out, result, query);
        qexec.close();
    }
}
