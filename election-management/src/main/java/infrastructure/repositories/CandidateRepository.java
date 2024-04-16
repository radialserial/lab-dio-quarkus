package infrastructure.repositories;

import domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository {
    void save(List<Candidate> candidateList);

    default void save(Candidate candidate) {
        save(List.of(candidate));
    }

    List<Candidate> findAll();

    Optional<Candidate> findById(String id);
}
