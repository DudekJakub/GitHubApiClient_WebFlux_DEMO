package com.demo.githubapiclient.service;

import com.demo.githubapiclient.exception.GitHubServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GitHubPageFetcherService implements PageFetcherService {

    private final WebClient webClient;

    @Autowired
    public GitHubPageFetcherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<?> fetchAllPages(final String url, final Class<?> targetClass) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(response -> {
                    int currentPageValue = Integer.parseInt(url.trim().substring(url.lastIndexOf("=") + 1));
                    Optional<String> nextPageUrl = getNextPageUrl(response.headers().asHttpHeaders(), currentPageValue);

                    if (nextPageUrl.isPresent()) {
                        try {
                            URI decodedUri = new URI(nextPageUrl.get());
                            String path = decodedUri.getRawPath();
                            String query = decodedUri.getRawQuery();
                            String decodedUrl = String.format("%s://%s%s?%s", decodedUri.getScheme(), decodedUri.getHost(), path, query);

                            return Flux.concat(response.bodyToFlux(targetClass), fetchAllPages(decodedUrl, targetClass));
                        } catch (URISyntaxException e) {
                            log.debug("Problem with URI syntax occurred: [{}]", e.getMessage());
                            return Flux.error(new GitHubServiceException("There was a problem fetching " + targetClass.getSimpleName()));
                        }
                    } else {
                        return response.bodyToFlux(targetClass);
                    }
                });
    }

    private Optional<String> getNextPageUrl(HttpHeaders headers, int currentPageValue) {
        int nextPageValue = currentPageValue + 1;
        List<String> linkHeaders = headers.getOrEmpty(HttpHeaders.LINK);

        for (String linkHeader : linkHeaders) {
            String[] links = linkHeader.split(",");
            for (String link : links) {
                if (link.contains("rel=\"next\"")) {
                    String[] parts = link.split(";");
                    for (String part : parts) {
                        if (part.contains("page=" + nextPageValue)) {
                            String href = part.trim().replaceAll("<", "").replaceAll(">", "");
                            return Optional.of(href);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }
}
