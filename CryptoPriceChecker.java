import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;
import java.util.Iterator;

public class CryptoPriceChecker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Formatar a URL da API CoinGecko para obter os preços de todas as criptomoedas
            String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum,ripple,litecoin,cardano,stellar,dogecoin,monero,chainlink,polkadot&vs_currencies=usd";

            // Criar um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criar uma solicitação HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // Enviar a solicitação e obter a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar se a resposta foi bem-sucedida
            if (response.statusCode() == 200) {
                // Parsear o corpo da resposta JSON
                JSONObject jsonResponse = new JSONObject(response.body());

                // Iterar sobre todas as criptomoedas disponíveis na resposta
                Iterator<String> keys = jsonResponse.keys();
                System.out.println("Criptomoedas Disponíveis:");
                while (keys.hasNext()) {
                    String cryptoSymbol = keys.next();
                    double price = jsonResponse.getJSONObject(cryptoSymbol).getDouble("usd");
                    System.out.println(cryptoSymbol.toUpperCase() + ": $" + price);
                }

                // Solicitar ao usuário que escolha uma criptomoeda para verificar em detalhes
                System.out.println("\nDigite o símbolo da criptomoeda que deseja verificar em detalhes:");
                String chosenCryptoSymbol = scanner.nextLine().toLowerCase();

                // Verificar se o símbolo escolhido está na lista de criptomoedas disponíveis
                if (jsonResponse.has(chosenCryptoSymbol)) {
                    double chosenCryptoPrice = jsonResponse.getJSONObject(chosenCryptoSymbol).getDouble("usd");
                    System.out.println("O preço atual de " + chosenCryptoSymbol.toUpperCase() + " é: $" + chosenCryptoPrice);
                } else {
                    System.out.println("Criptomoeda não encontrada na lista.");
                }
            } else {
                System.out.println("Erro na consulta à API: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ocorreu um erro ao tentar obter a lista de criptomoedas e seus preços.");
        }

        scanner.close();
    }
}
