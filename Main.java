import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

class CurrencyConverter {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static double convertCurrency(String baseCurrency, String targetCurrency, double amount) throws IOException {
        String urlStr = API_URL + baseCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        double exchangeRate = jsonResponse.getJSONObject("rates").getDouble(targetCurrency);
        return amount * exchangeRate;
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter base currency (e.g., USD, EUR): ");
            String baseCurrency = reader.readLine().toUpperCase();

            System.out.print("Enter target currency (e.g., USD, EUR): ");
            String targetCurrency = reader.readLine().toUpperCase();

            System.out.print("Enter amount to convert: ");
            double amount = Double.parseDouble(reader.readLine());

            double convertedAmount = CurrencyConverter.convertCurrency(baseCurrency, targetCurrency, amount);

            System.out.println("Converted amount: " + convertedAmount + " " + targetCurrency);
        } catch (IOException e) {
            System.out.println("Error reading input or fetching data: " + e.getMessage());
        }
    }
}
