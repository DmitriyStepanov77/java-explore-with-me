package ru.practicum.explore.dto.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {

    @Size(min = 5, max = 50)
    private String title;

    @Size(min = 1, max = 255)
    private String text;
}
