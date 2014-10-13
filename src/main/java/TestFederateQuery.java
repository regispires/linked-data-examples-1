import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;

public class TestFederateQuery {

	public static void main(String[] args) {
		System.out.println("Inicio");
		
		String queryString = 
			"PREFIX imdb: <http://data.linkedmdb.org/resource/movie/> " +
			"PREFIX dcterms: <http://purl.org/dc/terms/> " +
			"PREFIX dbpo: <http://dbpedia.org/ontology/> " +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"SELECT ?birthDate ?spouseName ?movieTitle ?movieDate { " +
			"  { SERVICE <http://dbpedia.org/sparql> " +
			"    { SELECT ?birthDate ?spouseName " +
			"      WHERE {" +
			"         ?actor rdfs:label \"Arnold Schwarzenegger\"@en ; " +
			"                dbpo:birthDate ?birthDate ; " +
			"                dbpo:spouse ?spouseURI . " +
			"         ?spouseURI rdfs:label ?spouseName . " +
			"          FILTER ( lang(?spouseName) = 'en' ) " +
			"      }" +
			"    }" +
			"  } " +
			"  { SERVICE <http://data.linkedmdb.org/sparql> " +
			"   { SELECT ?actor ?movieTitle ?movieDate " +
			"     WHERE {" +
			"       ?actor imdb:actor_name \"Arnold Schwarzenegger\". " +
			"       ?movie imdb:actor ?actor ; " +
			"              dcterms:title ?movieTitle ; " +
			"       dcterms:date ?movieDate . " +
			"     } " +
			"    } " +
			"  } " +
			"} ";
	
		System.out.println("query : " + queryString);
		long inicio = System.currentTimeMillis();
		QueryExecution qe = QueryExecutionFactory.create(queryString, Syntax.syntaxARQ, DatasetFactory.create());

		// Executa a consulta e obtém os resultados
		ResultSet results = qe.execSelect();
		System.out.println("Result : " + results.getRowNumber());

		// Exibição dos resultados
        Query query = QueryFactory.create(queryString);
 		ResultSetFormatter.out(System.out, results, query);

		// liberar o recurso alocado pela consulta
		qe.close();
		
		long fim = System.currentTimeMillis();

		System.out.println("Término em : " + (fim - inicio));
	}
}
