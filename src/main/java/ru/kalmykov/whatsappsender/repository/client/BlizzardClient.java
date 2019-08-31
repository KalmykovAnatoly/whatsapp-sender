package ru.kalmykov.whatsappsender.repository.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.kalmykov.whatsappsender.dto.PostTokenResponse;

import javax.annotation.ParametersAreNonnullByDefault;

@Service
@ParametersAreNonnullByDefault
public class BlizzardClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlizzardClient.class);

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String secret;
    private final String endpoint;
    private PostTokenResponse token;

    public BlizzardClient(
            RestTemplate restTemplate,
            @Value("${blizzard-client.client-id}") String clientId,
            @Value("${blizzard-client.secret}") String secret,
            @Value("${endpoint.blizzard}") String endpoint
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.secret = secret;
        this.endpoint = endpoint;
    }

    private PostTokenResponse obtainToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, secret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(map, headers);

        return restTemplate.postForEntity(endpoint, request, PostTokenResponse.class).getBody();
    }

    public PostTokenResponse getToken() {
        if (this.token != null) {
            return token;
        } else {
            this.token = obtainToken();
            return this.token;
        }
    }
}
