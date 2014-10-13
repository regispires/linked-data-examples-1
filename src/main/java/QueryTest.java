//import javax.swing.JOptionPane;
import javax.xml.ws.http.HTTPException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;

/** 
 * Verifica se um Endpoint SPARQL está ativo ou não.
 */
public class QueryTest {

    public static void main(String[] args) {
        //String service= "http://www4.wiwiss.fu-berlin.de/drugbank/sparql";
        //String service= "http://www4.wiwiss.fu-berlin.de/sider/sparql";
        //String service= "http://www4.wiwiss.fu-berlin.de/diseasome/sparql";
        String service= "http://dbpedia.org/sparql";
        //String service= JOptionPane.showInputDialog("Service");
        String query= "ASK WHERE { ?s ?p ?o }";
        QueryExecution qe= null;
        try {
            qe= QueryExecutionFactory.sparqlService(service, query);
            //qe.setTimeout(1000);
                if (qe.execAsk()) {
                    System.out.println(service+" is UP");
                    //JOptionPane.showMessageDialog(null, service+" is UP");
                }
        }
        catch (HTTPException e) { 
            System.out.println(service+" is DOWN"); 
            //JOptionPane.showMessageDialog(null, service+" is DOWN"); 
        } finally {
            qe.close();
        }
    }
}