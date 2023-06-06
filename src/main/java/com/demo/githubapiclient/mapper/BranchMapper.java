package com.demo.githubapiclient.mapper;

import com.demo.githubapiclient.model.Branch;
import com.demo.githubapiclient.model.dto.BranchResponseDto;

import java.util.List;

public interface BranchMapper {

    BranchResponseDto mapBranchToResponseDto(Branch branch);
    List<BranchResponseDto> mapBranchListToResponseDtoList(List<Branch> branchList);
}
