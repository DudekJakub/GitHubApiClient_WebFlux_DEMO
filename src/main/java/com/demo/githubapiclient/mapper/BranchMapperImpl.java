package com.demo.githubapiclient.mapper;

import com.demo.githubapiclient.model.Branch;
import com.demo.githubapiclient.model.dto.BranchResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchMapperImpl implements BranchMapper {

    @Override
    public BranchResponseDto mapBranchToResponseDto(final Branch branch) {
        return BranchResponseDto.builder()
                .branchName(branch.getName())
                .lastCommitSha(branch.getCommit().getSha())
                .build();
    }

    @Override
    public List<BranchResponseDto> mapBranchListToResponseDtoList(List<Branch> branchList) {
        return branchList.stream()
                .map(this::mapBranchToResponseDto)
                .toList();
    }
}
