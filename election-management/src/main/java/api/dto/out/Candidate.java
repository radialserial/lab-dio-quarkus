package api.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Candidate(
        String id,
        Optional<String> photo,
        String fullName,
        String email,
        Optional<String> phone,
        Optional<String> jobTitle
) {
    public static Candidate fromDomain(domain.Candidate domainCandidate) {
        return new Candidate(
                domainCandidate.id(),
                domainCandidate.photo(),
                domainCandidate.givenName() + " " + domainCandidate.familyName(),
                domainCandidate.email(),
                domainCandidate.phone(),
                domainCandidate.jobTitle()
        );
    }
}
