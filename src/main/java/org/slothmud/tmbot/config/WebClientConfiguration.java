package org.slothmud.tmbot.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500000)
                .responseTimeout(Duration.ofMillis(500000))
                .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(500000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(500000, TimeUnit.MILLISECONDS)));
    }


    @Bean
    public WebClient slothAucListWebClient(@Value("${org.slothmud.tmbot.auclist.url}") String baseUrl,
                                          HttpClient httpClient) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient slothEqListWebClient(@Value("${org.slothmud.tmbot.eqlist.url}") String baseUrl,
                                          HttpClient httpClient) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient slothPartiesInfoWebClient(@Value("${org.slothmud.tmbot.parties.url}") String baseUrl,
                                          HttpClient httpClient) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient telegramWebClient(@Value("${org.slothmud.tmbot.url}") String baseUrl,
                                       @Value("${org.slothmud.tmbot.token}") String botToken,
                                          HttpClient httpClient) {

        return WebClient.builder()
                .baseUrl(baseUrl + botToken)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
