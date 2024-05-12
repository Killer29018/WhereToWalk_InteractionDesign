package WhereToWalk;

import com.opencsv.CSVReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVReaderTools {
    protected static List<String[]> read(String s) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(s).toURI());
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll();
            }
        }
    }
}
