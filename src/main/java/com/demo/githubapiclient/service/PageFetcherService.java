package com.demo.githubapiclient.service;

import reactor.core.publisher.Flux;

public interface PageFetcherService {

    Flux<?> fetchAllPages(String url, Class<?> targetClass);
}
