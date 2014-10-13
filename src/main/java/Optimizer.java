import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Optimizer {
    public static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        }
        finally {
            stream.close();
        }
    }

    public static void main(String[] args) {
        try {
            Model model = ModelFactory.createDefaultModel();
            model.add(model.createResource("http://example/regis"), RDFS.label, "Regis");
            
            String str = readFile("/regis/Dropbox/workspace/linked_data1/src/q.sparql");
            System.out.println("Query:");
            System.out.println(str);
            Query query = QueryFactory.create(str.toString());
            Op op = Algebra.compile(query);
            System.out.println("Algebra:");
            System.out.println(op);
            op = Algebra.optimize(op);
            System.out.println("Optimized Algebra:");
            System.out.println(op);
            QueryIterator it = Algebra.exec(op, model);
            while (it.hasNext()) {
                Binding b = it.next();
                System.out.println(b);
            }
            it.close() ;        
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
