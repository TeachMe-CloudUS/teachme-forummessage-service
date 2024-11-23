package us.cloud.teachme.forummessage.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BadWordsService{

    @Value("${badwords.api.url}")
    private String apiUrl;

    @Value("${badwords.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public BadWordsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public boolean containsBadWords(String content) {
        String url = apiUrl + content;

        // Configura los headers con la API Key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", "community-purgomalum.p.rapidapi.com");

        // Crea la solicitud HTTP
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Realiza la solicitud GET a la API
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);

            // Extrae el valor de "result"
            String result = jsonResponse.get("result").asText();

            // Compara el valor de "result" con el contenido original
            if (result.equals(content)) {
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            // Manejo de errores si la solicitud falla
            e.printStackTrace();
            return false;
        }
    }
}

