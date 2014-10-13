import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.ResourceUtils;

public class RenameTest {
    static Model _renameTestModel = null;
    private static Dataset _dataset;

    public static void main(String[] args) throws Exception {
        // Warm up all classes/methods we will use
        String datasetName = "renameTestDS";
        _dataset = TDBFactory.createDataset(datasetName);
        _renameTestModel = _dataset.getNamedModel("http://www.boeing.com/sem/renametest");

        try {
            String res1 = "http://www.boeing.com/sem/renametest#User-user7";
            String res2 = "http://www.boeing.com/sem/renametest#User-user8";

            Resource res1Resource = _renameTestModel.createResource(res1);
            Property typeProp = ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            Resource thing = _renameTestModel.createResource("http://www.w3.org/2002/07/owl#Thing");

            Statement stmt = ResourceFactory.createStatement(res1Resource, typeProp, thing);

            _renameTestModel.add(stmt);
            ResourceUtils.renameResource(res1Resource, res2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}