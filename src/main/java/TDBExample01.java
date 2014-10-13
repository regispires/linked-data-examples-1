import java.io.File;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBExample01 {
    public static void main(String[] args) {
        String directory = "tdb";
        File f = new File(directory);
        if (!f.exists() && !f.mkdirs()) {
            return;
        }
        Dataset dataset = TDBFactory.createDataset(directory);
        String modelname = "db1_model1";
        Model model = TDBFactory.createNamedModel(modelname, directory);
        Resource subj = model.createResource("http://myr.rdf.org#me");
        model.add(subj, FOAF.nick, "myr");
        model.commit();
        TDB.sync(model);
        TDB.sync(dataset);
        String sparqlQueryString = "SELECT * {GRAPH ?g {?s ?p ?o } }";
        // CHECK
        submitQuery(dataset, directory, sparqlQueryString);
        // RE-CHECK
        submitQuery(dataset, directory, sparqlQueryString);
    }

    private static void submitQuery(Dataset dataset, String directory,
            String sparqlQueryString) {
        dataset = TDBFactory.createDataset(directory);
        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
        ResultSet results = qexec.execSelect();
        ResultSetFormatter.out(results);
        qexec.close();
        dataset.close();
    }
}
