import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;

public class Optimizer2 {
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
            String str = readFile("q.sparql");
            System.out.println("Query:");
            System.out.println(str);
            Query query = QueryFactory.create(str.toString());
            Op op = Algebra.compile(query);
            System.out.println("Algebra:");
            System.out.println(op);
            op = Algebra.optimize(op);
            System.out.println("Optimized Algebra:");
            System.out.println(op);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
