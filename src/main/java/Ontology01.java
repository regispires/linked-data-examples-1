import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

public class Ontology01 {
    public static void main(String[] args) {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        String base = "http://example.com/vocab/test";
        String ns = base + "#";

        m.createOntology(base);
        m.createClass(ns + "Foo");

        System.out.println("RDF/XML Without xmlbase ...");
        m.write(System.out, "RDF/XML");

        System.out.println("Without xmlbase ...");
        m.write(System.out, "RDF/XML-ABBREV");

        System.out.println("\nWith xmlbase ...");
        RDFWriter writer = m.getWriter("RDF/XML-ABBREV");
        writer.setProperty("xmlbase", base);
        writer.write(m, System.out, null);
    }
}