package ru.practicum.explore.service;

import ru.practicum.explore.HitDto;
import ru.practicum.explore.StatDto;

import java.util.List;

public interface StatService {

    public HitDto saveHit(HitDto hitDto);

    public List<StatDto> getHits(String startTime, String endTime, List<String> uris, boolean uniqueIp);
}
