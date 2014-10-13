import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.hp.hpl.jena.tdb.store.bulkloader.BulkLoader;
import com.hp.hpl.jena.vocabulary.RDF;

public class BulkLoaderExample {
    public static void main(String[] args) throws IOException {
        DatasetGraphTDB datasetGraph = (DatasetGraphTDB)TDBFactory.createDatasetGraph("tdbDir");

        /*
         * I saw the BulkLoader had two ways of loading data based on whether
         * the dataset existed already. I did two runs one with the following
         * two lines commented out to test both ways the BulkLoader runs.
         * Hopefully this had the desired effect.
         */
        datasetGraph.getDefaultGraph().add(
                new Triple(NodeFactory.createURI("urn:hello"), RDF.type.asNode(), NodeFactory
                        .createURI("urn:house")));
        datasetGraph.sync();
        InputStream inputStream = new FileInputStream("dbpediaData");
        BulkLoader.loadDataset(datasetGraph, inputStream, true);
    }
}
