package ru.practicum.explore.service;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.HitDto;
import ru.practicum.explore.StatDto;
import ru.practicum.explore.mapper.HitMapper;
import ru.practicum.explore.repository.HitsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Log4j2
@Transactional
public class StatServiceImp implements StatService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HitsRepository hitsRepository;
    private final HitMapper hitMapper;

    @Autowired
    public StatServiceImp(HitsRepository hitsRepository, HitMapper hitMapper) {
        this.hitsRepository = hitsRepository;
        this.hitMapper = hitMapper;
    }

    @Override
    public HitDto saveHit(HitDto hitDto) {
        log.info("Adding hit with app = {} and uri = {}.", hitDto.getApp(), hitDto.getUri());
        HitDto saveHit = hitMapper.toModel(hitsRepository.save(hitMapper.toEntity(hitDto)));
        log.info("Added hit with app = {} and uri = {}.", saveHit.getApp(), saveHit.getUri());
        return saveHit;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDto> getHits(String startTime, String endTime, List<String> uris, boolean uniqueIp) {
        LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(startTime, StandardCharsets.UTF_8), formatter);
        LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(endTime, StandardCharsets.UTF_8), formatter);

        if (start.isAfter(end)) {
            throw new ValidationException("Period is incorrect.");
        }

        if (uris == null || uris.isEmpty()) {
            if (uniqueIp) {
                log.info("Get statistic by period and unique ip.");
                return hitsRepository.findAllHitByPeriodAndUniqueIp(start, end);
            } else {
                log.info("Get statistic by period.");
                return hitsRepository.findAllHitByPeriod(start, end);
            }
        } else {
            if (uniqueIp) {
                log.info("Get statistic by uris, period and unique ip.");
                return hitsRepository.findAllHitByPeriodAndUrisAndUniqueIp(start, end, uris);
            } else {
                log.info("Get statistic by uris, period");
                return hitsRepository.findAllHitByPeriodAndUris(start, end, uris);
            }
        }
    }
}
