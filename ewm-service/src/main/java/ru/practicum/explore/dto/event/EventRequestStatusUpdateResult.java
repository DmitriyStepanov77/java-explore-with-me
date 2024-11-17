package ru.practicum.explore.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.request.RequestDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
