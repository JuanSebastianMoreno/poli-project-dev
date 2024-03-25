package packagesebas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner; // Adding this line to import Scanner

public class GenerateInfoFiles {

    public static void main(String[] args) throws IOException {

        // Define variables for the amount of data
        int salespersonsNumber = generateRandomInt(5, 10); // Random number between 5 and 10
        int productsNumber = generateRandomInt(5, 10); // Random number between 5 and 10

        // Generate salespersons file
        generateSalespersons(salespersonsNumber);

        // Generate products file
        generateProducts(productsNumber);

        // Generate individual files per salesperson
        generateSalespersonsFiles();
    }

    private static void generateSalespersons(int salespersonsNumber) throws IOException {
        FileWriter salespersonsWriter = new FileWriter("salespersons.txt");
        for (int i = 0; i < salespersonsNumber; i++) {
            String documentType = "CC";
            int documentNumber = generateRandomInt(10000000, 99999999);
            String salespersonName = generateRandomString(10);
            String salespersonLastName = generateRandomString(15);

            salespersonsWriter.write(documentType + ";" + documentNumber + ";" + salespersonName + ";" + salespersonLastName + "\n");
        }
        salespersonsWriter.close();
    }

    private static void generateProducts(int productsNumber) throws IOException {
        FileWriter productsWriter = new FileWriter("products.txt");
        for (int i = 0; i < productsNumber; i++) {
            int productId = generateRandomInt(1, 1000);
            String productName = generateRandomString(15);
            double unitPriceProduct = generateRandomDouble(1000.0, 10000.0);

            productsWriter.write(productId + ";" + productName + ";" + unitPriceProduct + "\n");
        }
        productsWriter.close();
    }

    private static void generateSalespersonsFiles() throws IOException {
        Scanner scanner = new Scanner(new File("salespersons.txt"));
        while (scanner.hasNextLine()) {
            String[] salespersonInfo = scanner.nextLine().split(";");
            String documentType = salespersonInfo[0];
            String documentNumber = salespersonInfo[1];

            FileWriter salespersonWriter = new FileWriter(documentType + ";" + documentNumber + ".txt");
            String productsData = readFileToString("products.txt");
            String[] products = productsData.split("\n");

            for (String product : products) {
                String[] productInfo = product.split(";");
                int productId = Integer.parseInt(productInfo[0]);
                int soldQuantity = generateRandomInt(10, 20);

                salespersonWriter.write(productId + ";quantity of products sold:" + soldQuantity + "\n");
            }
            salespersonWriter.close();
        }
        scanner.close();
    }

    // Function to generate a random integer
    private static int generateRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    // Function to generate a random string
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    // Function to generate a random double
    private static double generateRandomDouble(double min, double max) {
        return Math.random() * (max - min + 1) + min;
    }

    // Function to read the entire content of a file as a string
    private static String readFileToString(String filePath) throws IOException {
        Scanner scanner = new Scanner(new File(filePath));
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString();
    }
}