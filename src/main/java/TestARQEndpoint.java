import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.query.QueryExecutionFactory;

public class TestARQEndpoint  {
      
	    //private DBPediaConfig dbPediaConfig = null;

	 private String serviceURI = "http://DBpedia.org/sparql";
//        public DBPediaOnlineAccessor(DBPediaConfig dbPediaConfig){
//                this.dbPediaConfig = dbPediaConfig;     
//        }
//        
        
        
        public List<Triple> findObjectPropertiesOfSubject(String dbpediaIndividualURI) {
                List<Triple> resultingTriples = new ArrayList<Triple>();
                String queryString = 
                        "PREFIX rdft: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                        "PREFIX dbprop: <http://dbpedia.org/property/>\n" +
                        "PREFIX geor: <http://www.georss.org/georss/>\n" +
                        "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"+
                        
                        "SELECT DISTINCT ?y ?z WHERE { <"+ dbpediaIndividualURI +">  ?y  ?z  . "+
                        "FILTER( ?y != rdft:type && ?y != rdfs:label && ?y != rdfs:comment " +
                                        "&& ?y != foaf:depiction && ?y != foaf:img && ?y != foaf:page " +
                                        "&& ?y != skos:subject && ?y != foaf:homepage && ?y !=geor:point " +
                                        "&& ?y != geo:lat && ?y != geo:long " +
                                        "&& !regex(?y, '^http://www.geonames.org/ontology') " +
                                        //"&& !regex(?y, '^http://dbpedia.org/property/')" +
                                        "&& !regex(?y, '^http://www.w3.org/2002/07/owl') )}";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                    ResultSet results = qexec.execSelect() ;
                    while (results.hasNext())
                    {
                      QuerySolution soln = results.nextSolution() ;
                      RDFNode predicate = soln.get("y") ;
                      RDFNode relObject  = soln.get("z");
                      Model model = ModelFactory.createDefaultModel();                
                      Resource subject = model.createResource(dbpediaIndividualURI);
                      Property predicateProp = model.createProperty(predicate.asNode().getURI());                     
                      Triple triple = model.createStatement(subject,predicateProp, relObject).asTriple();
                      resultingTriples.add(triple);
                      System.out.println("PROP:"+predicate.toString());
                      System.out.println("REL-OBJ:"+relObject.toString());
                    }
                  } finally { qexec.close() ; 
                  }
                return resultingTriples;
        }

        
        public List<String> findRelatedSubjects(String queryTerm) {
                List<String> relatedSubjects = new ArrayList<String>();
                String queryString = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                                                         "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                                         "SELECT ?x WHERE {?x foaf:name ?z FILTER regex(?z, '"+ queryTerm +"','i') .} ";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                        ResultSet results = qexec.execSelect() ;
                        while (results.hasNext())
                        {
                                QuerySolution soln = results.nextSolution() ;
                                Resource resource = soln.getResource("x");                                    
                                System.out.println("KEYWORD RELATED URI:"+resource.toString());
                                relatedSubjects.add(resource.getURI());
                        }
                } finally { qexec.close() ; 
                }
                
                queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                 "SELECT ?x WHERE {?x rdfs:label ?z . FILTER regex(?z, '"+ queryTerm +"','i')}";
                query = QueryFactory.create(queryString) ;
                qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                        ResultSet results = qexec.execSelect() ;
                        while (results.hasNext())
                        {
                                QuerySolution soln = results.nextSolution() ;
                                Resource resource = soln.getResource("x");
                                System.out.println("KEYWORD RELATED URI:"+resource.toString());
                                if(!relatedSubjects.contains(resource.getURI()))
                                        relatedSubjects.add(resource.getURI());
                        }
                } finally { qexec.close() ; 
                }
                
                return relatedSubjects;
        }

        
        public List<Resource> findSiblingIndividuals(String dbpediaIndividualURI) {
                List<Resource> siblings = new ArrayList<Resource>();
                /*List<String> classURIs =  listClassURIsOfIndividual(dbpediaIndividualURI);
                String queryString = "PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                                          "SELECT ?x WHERE { ";
                classURIs.remove("http://dbpedia.org/ontology/Resource");
                for(int i=0; i < classURIs.size() ; i++){
                        String classURI = classURIs.get(i);
                        queryString += "?x rdfs:type <"+ classURI +"> .\n";
                }
                queryString += "}";
                
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                        ResultSet results = qexec.execSelect() ;
                        while (results.hasNext())
                        {
                                QuerySolution soln = results.nextSolution() ;
                                Resource resource = soln.getResource("x");                                    
                                System.out.println(resource.toString());
                                siblings.add(resource);
                        }
                } finally { qexec.close() ; 
                }*/
                return siblings;
        }

        
        public List<Triple> findSubjectsWithObjectProperty(String dbpediaIndividualURI) {
                List<Triple> resultingTriples = new ArrayList<Triple>();
                String queryString = "SELECT ?x ?y WHERE { ?x  ?y   <"+ dbpediaIndividualURI +"> .}";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                    ResultSet results = qexec.execSelect() ;
                    while (results.hasNext())
                    {
                      QuerySolution soln = results.nextSolution() ;
                      Resource relSubject = soln.getResource("x") ;
                      RDFNode predicate = soln.get("y");
                      
                      Model model = ModelFactory.createDefaultModel();                
                      Property predicateProp = model.createProperty(predicate.asNode().getURI());
                      Resource subject = model.createResource(dbpediaIndividualURI);
                      Triple triple = model.createStatement(relSubject, predicateProp, subject).asTriple();
                      resultingTriples.add(triple);
                      //System.out.println(relSubject.toString());
                    }
                  } finally { qexec.close() ; 
                  }
                return resultingTriples;
        }

        
        public String getNameOfIndividual(String indvURI) {
                String nameOfIndividual = null;
                String queryString = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "SELECT ?z WHERE { <"+ indvURI +"> foaf:name ?z .}";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                        ResultSet results = qexec.execSelect() ;
                        while (results.hasNext())
                        {
                                QuerySolution soln = results.nextSolution() ;
                                Literal literal = soln.getLiteral("z");                               
                                //System.out.println(literal.toString());
                                nameOfIndividual = literal.toString();
                        }
                } finally { qexec.close() ; 
                }
                if(nameOfIndividual == null){
                        queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "SELECT ?z WHERE { <"+ indvURI +"> rdfs:label ?z .}";
                        query = QueryFactory.create(queryString) ;
                        qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                        try {
                                ResultSet results = qexec.execSelect() ;
                                while (results.hasNext())
                                {
                                        QuerySolution soln = results.nextSolution() ;
                                        Literal literal = soln.getLiteral("z");                                 
                                        //System.out.println(literal.toString());
                                        nameOfIndividual = literal.getValue().toString();
                                }
                        } finally { qexec.close() ; 
                        }
                }
                if(nameOfIndividual == null){
                        nameOfIndividual = indvURI.substring(indvURI.lastIndexOf('/')+1);
                        nameOfIndividual = nameOfIndividual.replace("_", " ");
                }
                try{
                nameOfIndividual = java.net.URLDecoder.decode(nameOfIndividual, "utf-8");
                }catch(Exception ex){
                        ex.printStackTrace();
                }
                return nameOfIndividual;
        }

        
        public void initializeDataSource() {
                //We do not need to initialize anything for online access

        }

        
        public List<String> listClassURIsOfIndividual(String indvURI) {
                List<String> classURIs = new ArrayList<String>();
                String queryString = "PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT ?z WHERE { <"+ indvURI +"> rdfs:type ?z . " +
                                "FILTER regex(?z, '^http://dbpedia.org/ontology/')}";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                        ResultSet results = qexec.execSelect() ;
                        while (results.hasNext())
                        {
                                QuerySolution soln = results.nextSolution() ;
                                Resource resource = soln.getResource("z");                                    
                                //System.out.println(resource.toString());
                                classURIs.add(resource.getURI());
                        }
                } finally { qexec.close() ; 
                }
                return classURIs;
        }
        
        public static void main(String []args){
                System.out.println("Inicio");
        	    String serviceURI = "http://DBpedia.org/sparql";
                String queryString = "SELECT ?z " +
                	             	"WHERE { ?x  ?y  <http://dbpedia.org/resource/Hamburg_Airport> . " +
                		                    "?x <http://xmlns.com/foaf/0.1/name> ?z}";
                Query query = QueryFactory.create(queryString) ;
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, query) ;
                try {
                    ResultSet results = qexec.execSelect() ;
                    for ( ; results.hasNext() ; )
                    {
                      QuerySolution soln = results.nextSolution() ;
                      //RDFNode r = soln.get("x") ; // Get a result variable - must be a resource
                      Literal res = soln.getLiteral("z");
                      System.out.println(res.toString());
                    }
                  } finally { qexec.close() ; 
                  }

              		  
        }
}

