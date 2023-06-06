package com.demo.githubapiclient.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @NotBlank
    private String name;

    @NotNull
    private Commit commit;
}
