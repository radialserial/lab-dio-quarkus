package infrastructure.repositories;

import domain.Candidate;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SQLCandidateRepository implements CandidateRepository {
    @Override
    public void save(List<Candidate> candidateList) {

    }

    @Override
    public List<Candidate> findAll() {
        return List.of();
    }

    @Override
    public Optional<Candidate> findById(String id) {
        return Optional.empty();
    }
}
