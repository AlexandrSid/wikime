package org.aleksid.wikime.restclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class RestClientMain {


    public static void main(String[] args) {
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
//
//        WebClientConfiguration configuration = new WebClientConfiguration();
//        client = configuration.webClientWithTimeout();

        Mono<String> mono = client.get()
                .uri("/article/" + "1")
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        clientResponse -> Mono.empty())*/
                .bodyToMono(String.class);

        System.out.println("---------");
        String block = mono.block();
        System.out.println(block);
        System.out.println("---------");
    }

}
