import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherApp {

    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = reader.readLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter the date (yyyy-MM-dd HH:mm:ss): ");
                    String targetDate = reader.readLine();
                    double temperature = fetchData(targetDate, "\"main\":{\"temp\":");
                    if (temperature != Double.MIN_VALUE) {
                        System.out.println("Temperature on " + targetDate + ": " + temperature + "°C");
                    } else {
                        System.out.println("Data not found for the specified date.");
                    }
                    break;

                case "2":
                    System.out.print("Enter the date (yyyy-MM-dd HH:mm:ss): ");
                    targetDate = reader.readLine();
                    double windSpeed = fetchData(targetDate, "\"wind\":{\"speed\":");
                    if (windSpeed != Double.MIN_VALUE) {
                        System.out.println("Wind Speed on " + targetDate + ": " + windSpeed + " m/s");
                    } else {
                        System.out.println("Data not found for the specified date.");
                    }
                    break;

                case "3":
                    System.out.print("Enter the date (yyyy-MM-dd HH:mm:ss): ");
                    targetDate = reader.readLine();
                    double pressure = fetchData(targetDate, "\"main\":{\"pressure\":");
                    if (pressure != Double.MIN_VALUE) {
                        System.out.println("Pressure on " + targetDate + ": " + pressure + " hPa");
                    } else {
                        System.out.println("Data not found for the specified date.");
                    }
                    break;

                case "0":
                    System.out.println("Exiting the program.");
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static double fetchData(String targetDate, String field) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        String responseJson = response.toString();

        Pattern pattern = Pattern.compile("\"dt_txt\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(responseJson);

        while (matcher.find()) {
            String responseDate = matcher.group(1);
            LocalDateTime responseDateTime = LocalDateTime.parse(responseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime targetDateTime = LocalDateTime.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            if (responseDateTime.isEqual(targetDateTime)) {
                int startIndex = responseJson.indexOf(field, matcher.end());
                if (startIndex != -1) {
                    int endIndex = responseJson.indexOf(",", startIndex);
                    if (endIndex != -1) {
                        String valueStr = responseJson.substring(startIndex + field.length(), endIndex).trim();
                        return Double.parseDouble(valueStr);
                    }
                }
            }
        }

        return Double.MIN_VALUE;
    }
}
