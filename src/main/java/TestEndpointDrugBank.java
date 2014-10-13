import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class TestEndpointDrugBank  {
      
        public static void main(String []args){
                System.out.println("Inicio");

           	    String serviceURI = "http://cheminfov.informatics.indiana.edu:8890/sparql";
           	 
                String queryString = "PREFIX sider: <http://chem2bio2rdf.org/sider/resource/> " +
                		"PREFIX kegg: <http://chem2bio2rdf.org/kegg/resource/> " +
                		"PREFIX drugbank: <http://chem2bio2rdf.org/drugbank/resource/> " +
                		"SELECT ?pathway_id (count(?pathway_id) as ?count) " +
                		"FROM <http://chem2bio2rdf.org/sider> " +
                		"FROM <http://chem2bio2rdf.org/kegg> " +
                		"FROM <http://chem2bio2rdf.org/drugbank> " +
                		"WHERE { " +
                		  "?sider sider:side_effect ?side_effect .  " +
                		   "FILTER regex(?side_effect, 'hepatomegaly', 'i') . " +
           				  " ?sider sider:cid ?compound . " +
           				  " ?drug drugbank:CID ?compound . " +
           				  "?drug_target drugbank:DBID ?drug . " +
           				  "?drug_target drugbank:SwissProt_ID ?uniprot . " +
             		      "?kegg_pathway kegg:protein ?uniprot . " +
           				  "?kegg_pathway kegg:pathwayid ?pathway_id . " +
           				"} " +
           				"GROUP BY ?pathway_id ORDER BY ?count";

              		  
                //System.out.println("query : " + queryString);
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURI, query) ;
    			
    			// Executa a consulta e obtém os resultados
    			ResultSet results = qe.execSelect();

    			// Exibição dos resultados
    			ResultSetFormatter.out(System.out, results, query);
    			System.out.println("Result : " + results.getRowNumber());
    			
    			// liberar o recurso alocado pela consulta
    			qe.close();

                System.out.println("Fim");
        }
}

/*
SELECT  ?name  ?workplace
		  WHERE  {
		  SERVICE  <http://dblp.uni-trier.de> {

		    ?author   foaf:name �Paul Erdos� .
		    ?article   dc:creator  ?author .
		    ?article   dc:creator  ?coauthor .
		    ?article   rdf:type   foaf:Document . 
		    ?coauthor   foaf:name ?name .
		  }


		  SERVICE  <http://dbpedia.org> {
		    ?coauthor   dbpprop:nationality dbpedia:German .
		     OPTIONAL  { ?coauthor dbpprop:workplaces ?workplace . }
		   }
		 }
		 ORDER BY  ?name
		 LIMIT  10

*/