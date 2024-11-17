package ru.practicum.explore.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.enums.RequestState;

import java.time.LocalDateTime;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private int id;
    private LocalDateTime created;
    private int event;
    private int requester;
    private RequestState status;
}
