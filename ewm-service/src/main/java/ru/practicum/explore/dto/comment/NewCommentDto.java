package ru.practicum.explore.dto.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotEmpty
    @NotBlank
    @Size(min = 5, max = 50)
    private String title;
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 255)
    private String text;
}
