import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ConversorApp {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/c99dac6d528ce8c52792560d/latest/USD";

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido/a al Conversor de Monedas!");

        // Mostrar el menú y obtener la moneda de origen
        String monedaOrigen = seleccionarMoneda(scanner, "Seleccione la moneda de origen");

        // Obtener la tasa de cambio
        double tasaCambio = obtenerTasa(monedaOrigen);

        // Mostrar el menú y obtener la moneda de destino
        String monedaDestino = seleccionarMoneda(scanner, "Seleccione la moneda de destino");

        // Realizar la conversión
        while (true) {
            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            double resultado = convertirMoneda(cantidad, tasaCambio, monedaOrigen, monedaDestino);
            System.out.println("Resultado: " + cantidad + " " + monedaOrigen + " = " + resultado + " " + monedaDestino);

            System.out.println("¿Quieres hacer otra conversión? (S/N)");
            String respuesta = scanner.next();
            if (!respuesta.equalsIgnoreCase("S")) {
                System.out.println("¡Gracias por usar el conversor de monedas!");
                break;
            }
        }

        scanner.close();
    }

    //  Método para obtener la tasa de cambio desde la API
    public static double obtenerTasa(String monedaOrigen) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Parsear la respuesta JSON con Gson
        JsonElement elemento = JsonParser.parseString(responseBody);
        JsonObject rootObject = elemento.getAsJsonObject();
        JsonObject conversionRates = rootObject.getAsJsonObject("conversion_rates");

        // Obtener la tasa de conversión para la moneda seleccionada
        // Utilizamos la moneda que el usuario seleccionó (monedaOrigen)
        return conversionRates.get(monedaOrigen).getAsDouble();
    }

    // Método para seleccionar la moneda (Origen o Destino)
    public static String seleccionarMoneda(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        System.out.println("1. ARS - Peso argentino");
        System.out.println("2. BOB - Boliviano");
        System.out.println("3. BRL - Real brasileño");
        System.out.println("4. CLP - Peso chileno");
        System.out.println("5. COP - Peso colombiano");
        System.out.println("6. USD - Dólar estadounidense");

        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1: return "ARS";
            case 2: return "BOB";
            case 3: return "BRL";
            case 4: return "CLP";
            case 5: return "COP";
            case 6: return "USD";
            default:
                System.out.println("Opción no válida. Se selecciona USD por defecto.");
                return "USD";
        }
    }

    // Método para realizar la conversión
    public static double convertirMoneda(double cantidad, double tasaOrigen, String monedaOrigen, String monedaDestino) throws IOException, InterruptedException {
        // Aquí obtendrás la tasa de la moneda destino en relación a la moneda origen
        double tasaDestino = obtenerTasa(monedaDestino);
        double resultado = cantidad * tasaDestino / tasaOrigen; // Conversión utilizando tasas

        // Redondear el resultado a 2 decimales usando String.format()
        String resultadoFormateado = String.format("%.2f", resultado);

        // Reemplazar coma por punto en el caso de que el sistema use coma como separador decimal
        resultadoFormateado = resultadoFormateado.replace(',', '.');

        // Convertir el resultado formateado a un double y devolverlo
        return Double.parseDouble(resultadoFormateado);
    }



}
