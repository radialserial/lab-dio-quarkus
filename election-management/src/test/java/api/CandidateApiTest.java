package api;

import api.dto.in.CreateCandidate;
import api.dto.in.UpdateCandidate;
import domain.Candidate;
import domain.CandidateService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class CandidateApiTest {

    @Inject
    CandidateApi candidateApi;

    @InjectMock
    CandidateService candidateService;

    @Test
    void create() {
        CreateCandidate createDto = Instancio.create(CreateCandidate.class);
        ArgumentCaptor<Candidate> captor = ArgumentCaptor.forClass(Candidate.class);

        candidateApi.create(createDto);

        verify(candidateService).save(captor.capture());
        verifyNoMoreInteractions(candidateService);

        Candidate resultCandidate = captor.getValue();
        assertEquals(createDto.photo(), resultCandidate.photo());
        assertEquals(createDto.givenName(), resultCandidate.givenName());
        assertEquals(createDto.familyName(), resultCandidate.familyName());
        assertEquals(createDto.email(), resultCandidate.email());
        assertEquals(createDto.phone(), resultCandidate.phone());
        assertEquals(createDto.jobTitle(), resultCandidate.jobTitle());
    }

    @Test
    void update() {
        String id = UUID.randomUUID().toString();
        UpdateCandidate updateDto = Instancio.create(UpdateCandidate.class);
        Candidate candidate = updateDto.toDomain(id);

        ArgumentCaptor<Candidate> captor = ArgumentCaptor.forClass(Candidate.class);

        when(candidateService.findById(id)).thenReturn(candidate);

        api.dto.out.Candidate response = candidateApi.update(id, updateDto);

        verify(candidateService).save(captor.capture());
        verify(candidateService).findById(id);
        verifyNoMoreInteractions(candidateService);

        assertEquals(api.dto.out.Candidate.fromDomain(candidate), response);
    }

    @Test
    void list() {
        List<Candidate> candidateList = Instancio.stream(Candidate.class).limit(10).toList();

        when(candidateService.findAll()).thenReturn(candidateList);

        var response = candidateApi.list();

        verify(candidateService).findAll();
        verifyNoMoreInteractions(candidateService);

        assertEquals(
                candidateList.stream().map(api.dto.out.Candidate::fromDomain).toList(),
                response
        );
    }

}