import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;             //to write formatted text to an output stream
import java.net.HttpURLConnection;          //used for making HTTP requests, such as GET or POST,
import java.net.ServerSocket;             //used to create a server that listens for client connections on a specific port.
import java.net.Socket;             //to create a connection between a client and a server.
import java.net.URL;                //It provides methods to access and manipulate URL information.
import java.util.HashMap;           //likely used to store mappings of city names to coordinates.
import java.util.Map;               

public class WeatherServer {

    // Map to hold city coordinates (latitude, longitude)
    private static final Map<String, double[]> cityCoordinates = new HashMap<>();

    static {
        // Add some cities and their coordinates (latitude, longitude)
        cityCoordinates.put("Lahore", new double[]{31.5497, 74.3436});
        cityCoordinates.put("Karachi", new double[]{24.8607, 67.0011});
        cityCoordinates.put("Islamabad", new double[]{33.6844, 73.0479});
        cityCoordinates.put("New York", new double[]{40.7128, -74.0060});
        cityCoordinates.put("London", new double[]{51.5074, -0.1278});
        cityCoordinates.put("Toronto", new double[]{43.6510, -79.347015});
        cityCoordinates.put("Sydney", new double[]{-33.8688, 151.2093});
        cityCoordinates.put("Tokyo", new double[]{35.682839, 139.759455});
        cityCoordinates.put("Mumbai", new double[]{19.0760, 72.8777});
        cityCoordinates.put("Dubai", new double[]{25.276987, 55.296249});
        cityCoordinates.put("Cairo", new double[]{30.0444, 31.2357});
        cityCoordinates.put("Paris", new double[]{48.8566, 2.3522});
        cityCoordinates.put("Moscow", new double[]{55.7558, 37.6173});
        cityCoordinates.put("Berlin", new double[]{52.5200, 13.4050});
        cityCoordinates.put("Rome", new double[]{41.9028, 12.4964});
        cityCoordinates.put("Bangkok", new double[]{13.7563, 100.5018});
        cityCoordinates.put("Buenos Aires", new double[]{-34.6037, -58.3816});
        cityCoordinates.put("Rio de Janeiro", new double[]{-22.9068, -43.1729});
        cityCoordinates.put("Los Angeles", new double[]{34.0522, -118.2437});
        cityCoordinates.put("San Francisco", new double[]{37.7749, -122.4194});
        cityCoordinates.put("Seoul", new double[]{37.5665, 126.978});
        cityCoordinates.put("Singapore", new double[]{1.3521, 103.8198});
        cityCoordinates.put("Hong Kong", new double[]{22.3964, 114.1099});
        // Add more cities as needed
    }

    // Method to fetch weather data based on latitude and longitude
    public String fetchWeatherData(double latitude, double longitude) {
        //This method makes an HTTP GET request to the Open Meteo API using the specified latitude and longitude to fetch current weather data.
        StringBuilder result = new StringBuilder();
        try {
            // Construct the URL for the Open Meteo API using latitude and longitude
            String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude 
                + "&longitude=" + longitude + "&current_weather=true"; 
            
            System.out.println("Fetching URL: " + urlString);  // Log the URL for debugging
            
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get the response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);  // Log the response code
            
            // If the response code is 200 (HTTP OK), read the response
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } else {
                return "Error fetching weather data: API responded with code " + responseCode;
            }
        } catch (IOException e) {
            return "Error fetching weather data: " + e.getMessage();
        }
        return result.toString();
    }

//"sockets," which are endpoints for sending and receiving data.

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12347)) { // Server socket on port 12345
            //to lsiten client connections on this port
            System.out.println("Weather Server is running...");

            while (true) {                  //infinite loop to keep server running
                // Accept client connections
                try (Socket clientSocket = serverSocket.accept(); //When a connection is made, a Socket object is 
                                //created to represent the client connection. This object is called clientSocket.

                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     //is created to send data to the client through the socket's output stream.

                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        //is created to read data sent by the client from the socket's input stream.
                    
                    System.out.println("Client connected.");
                    
                    // Read the city name from the client
                    String city = in.readLine();
                    System.out.println("Received city: " + city);
                    
                    // Get coordinates based on the city name
                    double[] coordinates = cityCoordinates.get(city);
                    
                    if (coordinates != null) {
                        // Fetch weather data using the coordinates

                        String weatherData = new WeatherServer().fetchWeatherData(coordinates[0], coordinates[1]);
                        
                        // Send the weather data back to the client
                        out.println(weatherData);
                    } else {
                        out.println("City not found in the database.");
                    }
                } catch (IOException e) {
                    System.err.println("Client connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
}
