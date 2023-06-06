package com.demo.githubapiclient.mapper;

import com.demo.githubapiclient.model.Repository;
import com.demo.githubapiclient.model.dto.RepositoryResponseDto;

import java.util.List;

public interface RepositoryMapper {

    RepositoryResponseDto mapRepositoryToResponseDto(Repository repository);
    List<RepositoryResponseDto> mapRepositoryListToResponseDtoList(List<Repository> repositoryList);
}
