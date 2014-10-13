import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class DumpModel {
	private static Logger log = LoggerFactory.getLogger(DumpModel.class);
	
    private static Model makeModel() {
        String BASE = "http://example/";
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("", BASE);
        Resource r1 = model.createResource(BASE + "r1");
        Resource r2 = model.createResource(BASE + "São Paulo");
        Property p1 = model.createProperty(BASE + "p");
        Property p2 = model.createProperty(BASE + "p2");
        RDFNode v1 = model.createTypedLiteral("1", XSDDatatype.XSDinteger);
        RDFNode v2 = model.createTypedLiteral("2", XSDDatatype.XSDinteger);

        r1.addProperty(p1, v1).addProperty(p1, v2);
        r1.addProperty(p2, v1).addProperty(p2, v2);
        r2.addProperty(p1, v1).addProperty(p1, v2);

        return model;
    }

	public static void main(String[] args) {
		try {
			FileOutputStream out = new FileOutputStream(new File("/tmp/arquivo.nt"));
			Model results = makeModel();
			log.info("Salvando arquivo...");
			results.write(out, "N-TRIPLE");
			//results.write(out, "TTL");
			//results.write(out, "RDF/XML");
			//results.write(out, "RDF/XML-ABBREV");
			out.close();
			log.info("Arquivo salvo.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
