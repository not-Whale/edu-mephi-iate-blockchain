package edu.mephi.iate.blockchain.lab2.http_client;

import edu.mephi.iate.blockchain.lab2.enums.BlockchairInfinitableEndpointEnum;
import edu.mephi.iate.blockchain.lab2.enums.BlockchairInfinitableEndpointParameterEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class BitcoinStatsBlockchairAPIHttpClient implements AutoCloseable {

    private final String baseUrl;

    private final HttpClient httpClient;

    public BitcoinStatsBlockchairAPIHttpClient() {
        this("https://api.blockchair.com/");
    }

    public BitcoinStatsBlockchairAPIHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NEVER)
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    public Optional<String> get(BlockchairInfinitableEndpointEnum endpoint, Map<BlockchairInfinitableEndpointParameterEnum, List<String>> params) {
        URI requestURI = URI.create(baseUrl);

        StringBuilder paramsString = new StringBuilder();
        var entrySet = params.entrySet().stream().toList();
        for (int k = 0; k < entrySet.size(); k++) {
            var entry = entrySet.get(k);

            if (k == 0) {
                paramsString.append("?");
            } else {
                paramsString.append("&");
            }
            String key = entry.getKey().getParameterName();
            paramsString.append(key).append("=");

            List<String> values = entry.getValue();
            for (int v = 0; v < values.size(); v++) {
                paramsString.append(values.get(v));
                if (v < values.size() - 1) {
                    paramsString.append(",");
                }
            }
        }
        requestURI = requestURI.resolve(endpoint.getEndpointName() + paramsString);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .uri(requestURI)
                .build();

        try {
            HttpResponse<String> response = this.httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            } else {
                log.error("Error during requesting {}. Status code: {}", requestURI, response.statusCode());
                return Optional.empty();
            }

        } catch (IOException e) {
            log.error("IOException during requesting {}. Message: {}", requestURI, e.getMessage());
            return Optional.empty();
        } catch (InterruptedException e) {
            log.error("InterruptedException during requesting {}. Message: {}", requestURI, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void close() {
        this.httpClient.close();
    }
}
