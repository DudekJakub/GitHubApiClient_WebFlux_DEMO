package com.demo.githubapiclient.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repository {

    @NotBlank
    private String name;

    @NotNull
    private Owner owner;
    private boolean fork;
    private List<Branch> branches;
}
