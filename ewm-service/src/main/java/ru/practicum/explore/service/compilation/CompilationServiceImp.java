package ru.practicum.explore.service.compilation;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.compilation.UpdateCompilationDto;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.CompilationDtoMapper;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.QCompilation;
import ru.practicum.explore.repository.CompilationsRepository;
import ru.practicum.explore.repository.EventsRepository;

import java.util.List;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class CompilationServiceImp implements CompilationService {

    private final CompilationsRepository compilationsRepository;
    private final CompilationDtoMapper compilationDtoMapper;
    private final EventsRepository eventsRepository;

    @Override
    public Compilation addCompilation(NewCompilationDto compilationDto) {
        log.info("Add compilation with title = {}", compilationDto.getTitle());

        Compilation compilation = compilationDtoMapper.toEntity(compilationDto);

        List<Integer> eventsId = compilationDto.getEvents();

        if (!eventsId.isEmpty())
            compilation.setEvents(eventsRepository.findAllByIdIn(eventsId));

        return compilationsRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationsRepository.deleteById(compId);
    }

    @Override
    public Compilation getCompilation(int compId) {
        log.info("Get compilation with id = {}", compId);

        return compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found"));
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        log.info("Get compilations.");

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (pinned != null) {
            booleanBuilder.and(QCompilation.compilation.pinned.eq(pinned));
            log.info("Parameter 'pinned' = {}.", pinned);
        }
        return compilationsRepository.findAll(booleanBuilder, pageable).getContent();
    }

    @Override
    public Compilation updateCompilation(UpdateCompilationDto compilationDto, int compId) {
        log.info("Update compilation with id = {}", compId);

        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
            log.info("Parameter 'title' = {}.", compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
            log.info("Parameter 'pinned' = {}.", compilationDto.getPinned());
        }
        if (!compilationDto.getEvents().isEmpty()) {
            compilation.setEvents(eventsRepository.findAllByIdIn(compilationDto.getEvents()));
            log.info("Parameters 'events ids' = {}.", compilationDto.getEvents());
        }
        return compilationsRepository.save(compilation);
    }
}
