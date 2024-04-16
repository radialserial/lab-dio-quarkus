package infrastructure.repositories;

import domain.Candidate;
import domain.CandidateQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SQLCandidateRepository implements CandidateRepository {
    @Override
    public void save(List<Candidate> candidateList) {

    }

    @Override
    public List<Candidate> find(CandidateQuery candidateQuery) {
        return List.of();
    }

}
