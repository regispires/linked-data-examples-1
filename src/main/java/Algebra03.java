import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;

public class Algebra03 {
    public static void main(String[] args) {
        StringBuilder str = new StringBuilder();
        str.append("PREFIX :<http://example.org/> ")
           .append("SELECT ?s ?name ")
           .append("WHERE { ")
           .append("  ?s :age ?age . ")
           .append("  OPTIONAL { ")
           .append("    ?s :name ?name . ")
           .append("  } ")
           .append("  FILTER (?age<  20) . ")
           .append("} ");
        Query query = QueryFactory.create(str.toString());
        Op op = Algebra.compile(query);
        System.out.println(op);
        op = Algebra.optimize(op);
        System.out.println(op);
    }
}
