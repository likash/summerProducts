package summerProducts;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SummerProductsAnalyzer {

    private final CSVReader reader;
    private final Map<String, ComputedValues> computedValuesPerCountry;

    public SummerProductsAnalyzer(CSVReader reader) {
        this.reader = reader;
        computedValuesPerCountry = new HashMap<>();
    }

    public void compute() throws IOException {
        int countLinesWithoutCountry = 0;

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            String country = nextLine[29];
            double price = Double.parseDouble(nextLine[2]);
            double ratingFiveCount = nextLine[9].isEmpty() ? 0 : Double.parseDouble(nextLine[9]);
            double ratingCount = nextLine[8].isEmpty() ? 0 : Double.parseDouble(nextLine[8]);

            if (country.isEmpty()) {
                countLinesWithoutCountry++;
            } else {
                computedValuesPerCountry.merge(country, new ComputedValues(price, ratingFiveCount, ratingCount, 1),
                        (old, n) -> {
                            old.addValues(price, ratingCount, ratingFiveCount);

                            return old;
                        });
            }

        }

        System.out.println(countLinesWithoutCountry + " lines has not country");

        computedValuesPerCountry.forEach((k, v) -> System.out.printf(
                "Country: %s Average price: %.2f Share of five-star products: %.3f %n",
                k, v.computeAveragePrice(), v.computeFiveStar()));
    }

    public static class ComputedValues {
        private double sum;
        private double ratingFiveCount;
        private double ratingCount;
        private int count;

        public ComputedValues(double sum, double ratingFiveCount, double ratingCount, int count) {
            this.sum = sum;
            this.ratingFiveCount = ratingFiveCount;
            this.ratingCount = ratingCount;
            this.count = count;
        }

        public void addValues(double sum, double ratingCount, double ratingFiveCount) {
            this.sum += sum;
            this.ratingCount += ratingCount;
            this.ratingFiveCount += ratingFiveCount;
            this.count++;
        }

        public double computeAveragePrice() {
            return sum / count;
        }

        public double computeFiveStar() {
            return ratingCount > 0 ? ratingFiveCount / ratingCount * 100 : -1;
        }
    }
}
