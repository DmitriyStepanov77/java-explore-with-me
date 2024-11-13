package ru.practicum.explore.service.compilation;

import com.querydsl.core.BooleanBuilder;
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
public class CompilationServiceImp implements CompilationService {

    private final CompilationsRepository compilationsRepository;
    private final CompilationDtoMapper compilationDtoMapper;
    private final EventsRepository eventsRepository;

    public CompilationServiceImp(CompilationsRepository compilationsRepository,
                                 CompilationDtoMapper compilationDtoMapper, EventsRepository eventsRepository) {
        this.compilationsRepository = compilationsRepository;
        this.compilationDtoMapper = compilationDtoMapper;
        this.eventsRepository = eventsRepository;
    }

    @Override
    public Compilation addCompilation(NewCompilationDto compilationDto) {
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
        return compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found"));
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (pinned != null)
            booleanBuilder.and(QCompilation.compilation.pinned.eq(pinned));
        return compilationsRepository.findAll(booleanBuilder, pageable).getContent();
    }

    @Override
    public Compilation updateCompilation(UpdateCompilationDto compilationDto, int compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (compilationDto.getTitle() != null)
            compilation.setTitle(compilationDto.getTitle());
        if (compilationDto.getPinned() != null)
            compilation.setPinned(compilationDto.getPinned());
        if (!compilationDto.getEvents().isEmpty())
            compilation.setEvents(eventsRepository.findAllByIdIn(compilationDto.getEvents()));

        return compilationsRepository.save(compilation);
    }
}
