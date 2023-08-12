import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Scanner;

public class WeatherApp {
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        try {
            URI endpointUri = new URI(BASE_URL);
            URL endpoint = endpointUri.toURL();
            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner sc = new Scanner(conn.getInputStream());
                String line = "";
                while (sc.hasNext()) {
                    line += sc.nextLine();
                }
                sc.close();

                WeatherApp weatherApp = new WeatherApp();
                weatherApp.processWeatherForecast(line);

            } else {
                System.out.println("Something Went Wrong!");
            }

        } catch (Exception exc) {
            System.out.print(exc);
        }
    }

    private void processWeatherForecast(String json) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option for getting weather forecast:\n"
                    + "1. Temperature\n"
                    + "2. Wind Speed\n"
                    + "3. Pressure\n"
                    + "0. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the date and time (yyyy-MM-dd HH:mm:ss) ");
                    String targetDateTime = sc.nextLine();
                    getTemperature(json, targetDateTime);
                    break;
                case 2:
                    System.out.print("Enter the date and time (yyyy-MM-dd HH:mm:ss)  ");
                    targetDateTime = sc.nextLine();
                    getWindSpeed(json, targetDateTime);
                    break;
                case 3:
                    System.out.print("Enter the date and time (yyyy-MM-dd HH:mm:ss)  ");
                    targetDateTime = sc.nextLine();
                    getPressure(json, targetDateTime);
                    break;
                case 0:
                    System.out.println("Thanks for using");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }

    private void getTemperature(String json, String targetDateTime) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date inputDate = inputDateFormat.parse(targetDateTime);

            JSONObject object = new JSONObject(json);
            JSONArray list = object.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject inList = list.getJSONObject(i);
                String forecastDateTime = inList.getString("dt_txt");
                Date forecastDate = inputDateFormat.parse(forecastDateTime);

                if (inputDate.compareTo(forecastDate) == 0) {
                    JSONObject main = inList.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    System.out.println("Temperature on " + forecastDateTime + " is " + temperature);
                    return;
                }
            }

            System.out.println("Data not found");
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    private void getPressure(String json, String targetDateTime) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date inputDate = inputDateFormat.parse(targetDateTime);

            JSONObject object = new JSONObject(json);
            JSONArray list = object.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject inList = list.getJSONObject(i);
                String forecastDateTime = inList.getString("dt_txt");
                Date forecastDate = inputDateFormat.parse(forecastDateTime);

                if (inputDate.compareTo(forecastDate) == 0) {
                    JSONObject main = inList.getJSONObject("main");
                    double pressure = main.getDouble("pressure");
                    System.out.println("Pressure on " + forecastDateTime + " is " + pressure);
                    return;
                }
            }

            System.out.println("Data not found");
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }

    private void getWindSpeed(String json, String targetDateTime) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date inputDate = inputDateFormat.parse(targetDateTime);

            JSONObject object = new JSONObject(json);
            JSONArray list = object.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject inList = list.getJSONObject(i);
                String forecastDateTime = inList.getString("dt_txt");
                Date forecastDate = inputDateFormat.parse(forecastDateTime);

                if (inputDate.compareTo(forecastDate) == 0) {
                    JSONObject wind = inList.getJSONObject("wind");
                    double windSpeed = wind.getDouble("speed");
                    System.out.println("Wind Speed on " + forecastDateTime + " is " + windSpeed);
                    return;
                }
            }

            System.out.println("Data not found ");
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
}
