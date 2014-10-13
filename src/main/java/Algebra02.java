import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.vocabulary.DC;

public class Algebra02 {
    public static Model createModel() {
        Model m = ModelFactory.createDefaultModel();
        Resource r1 = m.createResource("http://example.org/book#1");
        Resource r2 = m.createResource("http://example.org/book#2");
        r1.addProperty(DC.title, "SPARQL - the book").addProperty(
                DC.description, "A book about SPARQL");
        r2.addProperty(DC.title, "Advanced techniques for SPARQL");
        return m;
    }
    
    public static void main(String[] args) {
        String s = "SELECT DISTINCT ?s { ?s ?p ?o }";
        Query query = QueryFactory.create(s);
        System.out.println(query);
        Op op = Algebra.compile(query);
        System.out.println(op);
        op = Algebra.optimize(op);
        System.out.println(op);
        QueryIterator qIter = Algebra.exec(op, createModel());
        while (qIter.hasNext()) {
            Binding b = qIter.nextBinding();
            System.out.println(b);
        }
        qIter.close();
    }
}
