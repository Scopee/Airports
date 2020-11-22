import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@org.springframework.stereotype.Controller
public class Controller {
    private final PatriciaTrie<ArrayList<Integer>> trie;
    private final Map<Integer, String> airports;
    private final String path;
    private final int columnIndex;

    /**
     * Initialization of the controller.
     *
     * @param args Command line arguments
     */
    public Controller(String[] args) {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("application.yml"));
        Properties props = yamlFactory.getObject();
        assert props != null;
        path = props.getProperty("pathToCSV");
        if (args.length == 1 && Integer.parseInt(args[0]) >= 1 && Integer.parseInt(args[0]) <= 14) {
            this.columnIndex = Integer.parseInt(args[0]) - 1;
        } else {
            this.columnIndex = Integer.parseInt(props.getProperty("indexColumn")) - 1;
        }
        airports = parseCSV();
        trie = fillTree();
    }

    /**
     * This method returns array of string representations matching with the prefix.
     *
     * @param prefix prefix to find matching airports
     * @return array of string with matching airports
     */
    public String[] findAirports(String prefix) {
        SortedMap<String, ArrayList<Integer>> indexes = trie.prefixMap(prefix.toLowerCase());
        Set<Integer> idx = new HashSet<>();
        for (ArrayList<Integer> value : indexes.values()) {
            idx.addAll(value);
        }
        ArrayList<String> foundAirports = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (idx.contains(Integer.parseInt(line[0]))) {
                    foundAirports.add(new Airport(line).convertToString(columnIndex));
                }
            }
        } catch (IOException | CsvValidationException | NullPointerException e) {
            e.printStackTrace();
        }

        String[] toSort = foundAirports.toArray(String[]::new);
        Arrays.parallelSort(toSort, Comparator.comparing(a -> a));
        return toSort;
    }

    /**
     * This method read CSV file and parse information for index.
     * Index should be defined in application.yml or in command line argument.
     * Path to CSV file should be defined in application.yml.
     *
     * @return {@link Map} containing the key - number in CSV and value - field to index
     */
    private Map<Integer, String> parseCSV() {
        Map<Integer, String> airports = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] line;
            while ((line = reader.readNext()) != null) {
                airports.put(Integer.parseInt(line[0]), line[columnIndex].toLowerCase());
            }
        } catch (IOException | CsvValidationException | NullPointerException e) {
            e.printStackTrace();
        }
        return airports;
    }

    /**
     * This method initializes {@link PatriciaTrie}.
     *
     * @return Trie with value which contains numbers corresponding to indexed fields.
     */
    private PatriciaTrie<ArrayList<Integer>> fillTree() {
        PatriciaTrie<ArrayList<Integer>> tree = new PatriciaTrie<>();
        for (Map.Entry<Integer, String> entry : airports.entrySet()) {
            if (!tree.containsKey(entry.getValue()))
                tree.put(entry.getValue(), new ArrayList<>());
            tree.get(entry.getValue()).add(entry.getKey());
        }
        return tree;
    }
}
