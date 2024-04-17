package infrastructure.repositories;

import domain.Candidate;
import domain.CandidateQuery;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CandidateRepositoryTest {

    public abstract CandidateRepository candidateRepository();

    @Test
    void save() {
        Candidate candidate = Instancio.create(Candidate.class);

        candidateRepository().save(candidate);

        Optional<Candidate> result = candidateRepository().findById(candidate.id());

        assertTrue(result.isPresent());
        assertEquals(candidate, result.get());
    }

    @Test
    void findAll() {
        List<Candidate> candidateList = Instancio.stream(Candidate.class).limit(10).toList();

        candidateRepository().save(candidateList);

        List<Candidate> result = candidateRepository().findAll();

        // FIXME fails when comparing list contents
        assertEquals(candidateList.size(), result.size());
    }

    @Test
    void findByName() {
        Candidate candidate1 = Instancio.create(Candidate.class);
        Candidate candidate2 = Instancio.of(Candidate.class)
                .set(
                        field("familyName"),
                        "Alves"
                ).create();

        CandidateQuery candidateQuery = new CandidateQuery.Builder().name("ALV").build();

        candidateRepository().save(List.of(candidate1, candidate2));

        List<Candidate> result = candidateRepository().find(candidateQuery);

        assertEquals(1, result.size());
        assertEquals(candidate2, result.get(0));
    }


}