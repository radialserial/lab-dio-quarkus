package infrastructure.resources;

import api.CandidateApi;
import api.dto.in.CreateCandidate;
import api.dto.in.UpdateCandidate;
import api.dto.out.Candidate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/api/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidateResource {
    private final CandidateApi candidateApi;

    public CandidateResource(CandidateApi candidateApi) {
        this.candidateApi = candidateApi;
    }

    @GET
    @ResponseStatus(RestResponse.StatusCode.OK)
    public List<Candidate> list() {
        return candidateApi.list();
    }

    @POST
    @ResponseStatus(RestResponse.StatusCode.CREATED)
    @Transactional
    public void create(CreateCandidate createDto) {
        candidateApi.create(createDto);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Candidate update(@PathParam("id") String id, UpdateCandidate updateCandidateDto) {
        return candidateApi.update(id, updateCandidateDto);
    }

}
