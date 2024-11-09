import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WeatherClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12347); // Connect to the server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Use Scanner for user input
            Scanner scanner = new Scanner(System.in);
            
            // Prompt user for city name
            System.out.print("Enter city name: ");
            String city = scanner.nextLine();
            
            // Send the city name to the server
            out.println(city);
            
            // Receive and print the weather data from the server
            String weatherData = in.readLine();
            System.out.println("Weather Data: " + weatherData);
            
            // Close the scanner
            scanner.close();
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
