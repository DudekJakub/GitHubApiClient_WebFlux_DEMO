package com.demo.githubapiclient.mapper;

import com.demo.githubapiclient.model.Repository;
import com.demo.githubapiclient.model.dto.RepositoryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryMapperImpl implements RepositoryMapper {

    private final BranchMapper branchMapper;

    @Autowired
    public RepositoryMapperImpl(final BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    @Override
    public RepositoryResponseDto mapRepositoryToResponseDto(Repository repository) {
        return RepositoryResponseDto.builder()
                .repositoryName(repository.getName())
                .ownerLogin(repository.getOwner().getLogin())
                .branches(branchMapper.mapBranchListToResponseDtoList(repository.getBranches()))
                .build();
    }

    @Override
    public List<RepositoryResponseDto> mapRepositoryListToResponseDtoList(List<Repository> repositoryList) {
        return repositoryList.stream()
                .map(this::mapRepositoryToResponseDto)
                .toList();
    }
}
