package ru.practicum.explore.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.enums.RequestState;

import java.util.List;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateDto {
    private List<Integer> requestIds;
    private RequestState status;
}
