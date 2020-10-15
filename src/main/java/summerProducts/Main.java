package summerProducts;


import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CSVReader reader = new CSVReader(
                new FileReader("src/main/resources/test-task_dataset_summer_products.csv"), ',', '"', 1);

        SummerProductsAnalyzer analyzer = new SummerProductsAnalyzer(reader);
        analyzer.compute();
    }

}
