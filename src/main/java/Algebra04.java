import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.algebra.*;
import com.hp.hpl.jena.sparql.algebra.op.*;
import com.hp.hpl.jena.sparql.core.*;
import com.hp.hpl.jena.sparql.engine.*;
import com.hp.hpl.jena.sparql.engine.binding.*;

public class Algebra04 {
    public static void main(String[] args) {
        // Creates a SPARQL algebra expression
        BasicPattern bp = new BasicPattern();
        Triple t = new Triple(Var.alloc("x"), 
                Var.alloc("y"), Var.alloc("z"));
        bp.add(t);
        Op op = new OpBGP(bp);
        op = new OpSlice(op, Long.MIN_VALUE, 10);
        
        // Uses SERVICE operation from SPARQL algebra
        Node node = Node.createURI("http://DBpedia.org/sparql");
        Op ops = new OpService(node, op, false);
        System.out.println(ops);

        // Creates an empty just to execute the algebra expr. 
        Model model = ModelFactory.createDefaultModel();
        QueryIterator it = Algebra.exec(ops, model);
        while (it.hasNext()) {
            Binding b = it.next();
            System.out.println(b);
        }
        it.close() ;        
        
        // Converts from algebra to SPARQL
        Query q = OpAsQuery.asQuery(op);
        System.out.println(q);
    }
}
