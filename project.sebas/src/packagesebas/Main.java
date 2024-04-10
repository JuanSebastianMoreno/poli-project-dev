package packagesebas;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException {
        // Generate both reports
        generateSalespersonsReport();
        generateProductQuantityReport();
    }

    private static void generateSalespersonsReport() throws IOException {
        Map<String, Double> salesBySalesperson = calculateSalesBySalesperson();

        // Sort the map by value in descending order
        Map<String, Double> sortedSalesBySalesperson = new TreeMap<>((a, b) -> {
            int result = Double.compare(salesBySalesperson.get(b), salesBySalesperson.get(a));
            return result != 0 ? result : 1; // Handle cases where there are equal values
        });
        sortedSalesBySalesperson.putAll(salesBySalesperson);

        // Write the sorted report to a file
        FileWriter writer = new FileWriter("salespersons_report.csv");
        for (Map.Entry<String, Double> entry : sortedSalesBySalesperson.entrySet()) {
            String salespersonName = entry.getKey();
            double totalSales = entry.getValue();
            String line = String.format("%s,%.2f%n", salespersonName, totalSales);
            writer.write(line);
        }
        writer.close();
    }

    private static void generateProductQuantityReport() throws IOException {
        Map<Integer, Integer> totalSalesByProduct = new HashMap<>();

        // Iterate over salesperson files
        File folder = new File("C:\\Users\\Cbastian\\eclipse-workspace\\project.sebas");
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt") && file.getName().startsWith("CC")) {
                Map<Integer, Integer> salesByProductSalesperson = calculateSalesByProductSalesperson(file);
                // Add sales from salesperson to total
                for (Map.Entry<Integer, Integer> entry : salesByProductSalesperson.entrySet()) {
                    int productId = entry.getKey();
                    int soldQuantity = entry.getValue();
                    totalSalesByProduct.put(productId, totalSalesByProduct.getOrDefault(productId, 0) + soldQuantity);
                }
            }
        }

        // Sort the map by value (quantity sold) in descending order.
        Map<Integer, Integer> sortedTotalSalesByProduct = new TreeMap<>((a, b) -> {
            int result = totalSalesByProduct.get(b) - totalSalesByProduct.get(a);
            return result != 0 ? result : 1; // Handle cases where values are equal
        });
        sortedTotalSalesByProduct.putAll(totalSalesByProduct);


     // Write the sorted report to a file.
        FileWriter writer = new FileWriter("product_quantity_report.csv");
        for (Map.Entry<Integer, Integer> entry : sortedTotalSalesByProduct.entrySet()) {
            int productId = entry.getKey();
            int totalSoldQuantity = entry.getValue();
            double productPrice = getProductPrice(productId);
            String line = String.format("%d;%d;%s;%.2f%n", productId, totalSoldQuantity, getProductName(productId), productPrice);
            writer.write(line);
        }
        writer.close();
    }

    private static Map<String, Double> calculateSalesBySalesperson() throws IOException {
        Map<String, Double> salesBySalesperson = new HashMap<>();
        File folder = new File("C:\\Users\\Cbastian\\eclipse-workspace\\project.sebas");
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt") && file.getName().startsWith("CC")) {
                Scanner scanner = new Scanner(file);
                String salespersonName = file.getName().replace(".txt", ""); // Get file name
                double totalSales = 0.0;
                while (scanner.hasNextLine()) {
                    String[] saleData = scanner.nextLine().split(";");
                    if (saleData.length == 2) { // Check if there are two parts in the data (productId and quantity)
                        int productId = Integer.parseInt(saleData[0]);
                        int quantity = Integer.parseInt(saleData[1].replace("quantity of products sold:", ""));
                        double productPrice = getProductPrice(productId);
                        totalSales += productPrice * quantity;
                    }
                }
                scanner.close();
                salesBySalesperson.put(salespersonName, totalSales);
            }
        }
        return salesBySalesperson;
    }

    private static Map<Integer, Integer> calculateSalesByProductSalesperson(File file) throws IOException {
        Map<Integer, Integer> salesByProductSalesperson = new HashMap<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] saleData = scanner.nextLine().split(";");
            if (saleData.length == 2) { // Check if there are two parts in the data (productId and quantity)
                int productId = Integer.parseInt(saleData[0]);
                int quantity = Integer.parseInt(saleData[1].replace("quantity of products sold:", ""));
                salesByProductSalesperson.put(productId, salesByProductSalesperson.getOrDefault(productId, 0) + quantity);
            }
        }
        scanner.close();
        return salesByProductSalesperson;
    }

    private static double getProductPrice(int productId) throws IOException {
        Scanner scanner = new Scanner(new File("products.txt"));
        while (scanner.hasNextLine()) {
            String[] productData = scanner.nextLine().split(";");
            if (Integer.parseInt(productData[0]) == productId) {
                scanner.close();
                return Double.parseDouble(productData[2]); // Return product price
            }
        }
        scanner.close();
        return 0.0; // In case the product is not found
    }

    private static String getProductName(int productId) throws IOException {
        Scanner scanner = new Scanner(new File("products.txt"));
        while (scanner.hasNextLine()) {
            String[] productData = scanner.nextLine().split(";");
            if (Integer.parseInt(productData[0]) == productId) {
                scanner.close();
                return productData[1]; // Return product name
            }
        }
        scanner.close();
        return "Unknown Product"; // In case the product is not found
    }
}
