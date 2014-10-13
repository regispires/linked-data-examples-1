import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Algebra01 {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        model.add(model.createResource("http://example/regis"), RDFS.label, "Regis");

        String str = new StringBuilder()
            .append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ")
            .append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ")
            .append("PREFIX dailymed: <http://www4.wiwiss.fu-berlin.de/dailymed/resource/dailymed/> ")
            .append("PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/> ")
            .append("PREFIX sider: <http://www4.wiwiss.fu-berlin.de/sider/resource/sider/> ")
            .append("PREFIX dbpprop: <http://dbpedia.org/property/> ")
            .append("SELECT ?dgai ?sen ?dgin ?dgls ")
            .append("WHERE { ")
            .append("SERVICE <http://www4.wiwiss.fu-berlin.de/dailymed/sparql> { ")
            .append("<http://www4.wiwiss.fu-berlin.de/dailymed/resource/drugs/3056> dailymed:activeIngredient ?dgai . ")
            .append(" ?dgai rdfs:label ?dgain . ")
            .append(" <http://www4.wiwiss.fu-berlin.de/dailymed/resource/drugs/3056> dailymed:genericDrug ?gdg . ")
            .append(" <http://www4.wiwiss.fu-berlin.de/dailymed/resource/drugs/3056> owl:sameAs ?sa . ")
            .append("} ")
            .append("SERVICE <http://www4.wiwiss.fu-berlin.de/drugbank/sparql> { ")
            .append("  ?gdg drugbank:pharmacology  ?dgin ; ")
            .append("} ")
            .append("OPTIONAL { ")
            .append("  SERVICE <http://www4.wiwiss.fu-berlin.de/sider/sparql> { ")
            .append("    ?sa sider:sideEffect ?se . ")
            .append("    ?se sider:sideEffectName ?sen . ")
            .append("  } ")
            .append("} ")
            .append("OPTIONAL { ") 
            .append("  SERVICE <http://dbpedia.org/sparql> { ")
            .append("    ?sa dbpprop:legalStatus ?dgls . ") 
            .append("} ")
            .append("} ")
            .append("} ").toString();
        
        //String str = "SELECT * { ?x ?y ?z } limit 10";
        Query query = QueryFactory.create(str);
        Op op = Algebra.compile(query);
        System.out.println(op);
        op = Algebra.optimize(op);
        System.out.println(op);
        QueryIterator it = Algebra.exec(op, model);
        while (it.hasNext()) {
            Binding b = it.next();
            System.out.println(b);
        }
        it.close() ;        
    }
}
