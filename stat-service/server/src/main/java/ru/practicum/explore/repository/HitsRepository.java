package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.StatDto;
import ru.practicum.explore.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitsRepository extends JpaRepository<Hit, Integer> {
    @Query("select new ru.practicum.explore.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startTime and :endTime " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllHitByPeriodAndUniqueIp(LocalDateTime startTime, LocalDateTime endTime);


    @Query("select new ru.practicum.explore.StatDto(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startTime and :endTime " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllHitByPeriod(LocalDateTime startTime, LocalDateTime endTime);

    @Query("select new ru.practicum.explore.StatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h  " +
            "where h.timestamp between :startTime and :endTime and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllHitByPeriodAndUrisAndUniqueIp(LocalDateTime startTime,
                                                       LocalDateTime endTime,
                                                       List<String> uris);

    @Query("select new ru.practicum.explore.StatDto(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startTime and :endTime and h.uri in :uris " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatDto> findAllHitByPeriodAndUris(LocalDateTime startTime,
                                            LocalDateTime endTime,
                                            List<String> uris);

}
