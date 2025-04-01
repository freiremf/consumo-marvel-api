import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Main {
    public static void main(String[] args) {
        final String URL_MARVEL = "https://gateway.marvel.com/v1/public/characters?ts=";
        final String LIMIT = "100";
        final String CHAVE_PUBLICA = "YOUR_PUBLIC_KEY"; // Substitua pela sua chave pública
        final String CHAVE_PRIVADA = "YOUR_PRIVATE_KEY"; // Substitua pela sua chave privada

        long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String hash = Md5Generator.toMD5(timeStamp + CHAVE_PRIVADA + CHAVE_PUBLICA);
        String url = URL_MARVEL + timeStamp + "&apikey=" + CHAVE_PUBLICA + "&hash=" + hash + "&limit=" + LIMIT;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200)
                throw new RuntimeException(response.body());
            System.out.println("Response: " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter os dados do API Marvel: " + e.getMessage());
        }
    }
}
