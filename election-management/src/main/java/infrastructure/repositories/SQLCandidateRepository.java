package infrastructure.repositories;

import domain.Candidate;
import domain.CandidateQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class SQLCandidateRepository implements CandidateRepository {

    private final EntityManager entityManager;

    public SQLCandidateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(List<Candidate> candidateList) {
        candidateList.stream()
                .map(infrastructure.repositories.entities.Candidate::fromDomain)
                .forEach(entityManager::merge);
    }

    @Override
    public List<Candidate> find(CandidateQuery candidateQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<infrastructure.repositories.entities.Candidate>
                criteriaQuery = criteriaBuilder.createQuery(infrastructure.repositories.entities.Candidate.class);

        Root<infrastructure.repositories.entities.Candidate>
                root = criteriaQuery.from(infrastructure.repositories.entities.Candidate.class);

        criteriaQuery.select(root).where(conditions(candidateQuery, criteriaBuilder, root));

        return entityManager.createQuery(criteriaQuery)
                .getResultStream()
                .map(infrastructure.repositories.entities.Candidate::toDomain)
                .toList();

    }

    private Predicate[] conditions(
            CandidateQuery criteriaQuery,
            CriteriaBuilder criteriaBuilder,
            Root<infrastructure.repositories.entities.Candidate> root) {
        return Stream.of(
                criteriaQuery.ids().map(
                        id -> criteriaBuilder.in(root.get("id")).value(id)
                ),
                criteriaQuery.name().map(
                        name -> criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("familyName")),
                                        name.toLowerCase() + "%"
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("givenName")),
                                        name.toLowerCase() + "%"
                                )
                        )
                )
        ).flatMap(Optional::stream).toArray(Predicate[]::new);
    }

}
