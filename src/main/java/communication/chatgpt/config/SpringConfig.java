package communication.chatgpt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import communication.chatgpt.controller.openAiResponse.OpenAiResponse;
import communication.chatgpt.controller.openAiResponse.ResponseEntityByWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class SpringConfig {

    @Value("${open-ai.key}")
    private String key;

    @Value("${server.connection-timeout.second}")
    private int connectionTimeout;

    @Value("${server.read-timeout.second}")
    private int readTimeout;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeaders(
                        headers -> {
                            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            headers.setBearerAuth(key);
                        }
                )
                .build();
    }

    @Bean
    public WebClient formWebClient() {
        return WebClient.builder()
                .defaultHeaders(
                        headers -> {
                            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
                            headers.setBearerAuth(key);
                        }
                )
                .build();
    }

    @Bean
    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(key);
        return headers;
    }

    @Bean
    public HttpHeaders formHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(key);
        return headers;
    }

    @Bean
    public OpenAiResponse openAiResponse() {
//        return new ResponseEntityByRestTemplate(objectMapper(), restTemplate());
        return new ResponseEntityByWebClient(objectMapper(), webClient(), formWebClient());
    }
}
