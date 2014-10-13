import java.util.List;
import com.hp.hpl.jena.graph.Factory;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Reasoner01 {
    public static void main(String[] args) {
        Node p = Node.createURI("p");
        Node a = Node.createURI("a");
        Node b = Node.createURI("b");
        Node c = Node.createURI("c");
        List<Rule> ruleList = Rule
                .parseRules("[r1: (?a p ?b), (?b p ?c) -> (?a p ?c),print(?a,?c)]");
        Graph test = Factory.createGraphMem();
        test.add(new Triple(a, p, b));
        test.add(new Triple(b, p, c));
        GenericRuleReasoner reasoner = (GenericRuleReasoner) GenericRuleReasonerFactory
                .theInstance().create(null);
        reasoner.setRules(ruleList);
        reasoner.setMode(GenericRuleReasoner.FORWARD);
        InfGraph infgraph = reasoner.bind(test);
        ExtendedIterator<Triple> triples = infgraph.find(null, p, null);
        while (triples.hasNext()) {
            System.out.println(" tri: " + triples.next());
        }
    }
}
