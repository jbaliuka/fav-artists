package com.example.favartists.api;

import com.example.favartists.model.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FavArtistsControllerTest {
    @LocalServerPort
    private int port;
    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().port(12365));
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void saveArtist() throws IOException, InterruptedException {
        var artist = new Artist();
        artist.setArtistName("ABBA");
        artist.setArtistId(3492);

        var putRequestBody = new ObjectMapper().writeValueAsString(artist);
        var putRequest =
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/artists"))
                        .PUT(HttpRequest.BodyPublishers.ofString(putRequestBody))
                        .header("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE)
                        .header("userId", "testUser")
                        .build();
        var getRequest =
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/artists"))
                        .GET()
                        .header("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE)
                        .header("userId", "testUser")
                        .build();

        var putResponse = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());
        assertThat(new ObjectMapper().readValue(putResponse.body(), Artist.class))
                .isEqualTo(artist);
        var getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertThat(new ObjectMapper().readValue(getResponse.body(), Artist[].class))
                .containsAll(List.of(artist));

    }
}
