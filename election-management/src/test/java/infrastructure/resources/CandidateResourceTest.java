package infrastructure.resources;

import api.CandidateApi;
import api.dto.in.CreateCandidate;
import api.dto.in.UpdateCandidate;
import api.dto.out.Candidate;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.instancio.Instancio;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(CandidateResource.class)
class CandidateResourceTest {
    @InjectMock
    CandidateApi candidateApi;

    @Test
    void create() {
        var inputCandidate = Instancio.create(CreateCandidate.class);

        given().contentType(MediaType.APPLICATION_JSON).body(inputCandidate)
                .when().post()
                .then().statusCode(RestResponse.StatusCode.CREATED);

        verify(candidateApi).create(inputCandidate);
        verifyNoMoreInteractions(candidateApi);
    }

    @Test
    void list() {
        var outputCandidateList
                = Instancio.stream(Candidate.class).limit(4).toList();

        when(candidateApi.list()).thenReturn(outputCandidateList);

        var response = given()
                .when().get()
                .then().statusCode(RestResponse.StatusCode.OK).extract().as(Candidate[].class);

        verify(candidateApi).list();
        verifyNoMoreInteractions(candidateApi);

        assertEquals(
                Arrays.stream(response).toList(),
                outputCandidateList
        );
    }

    @Test
    void update() {
        var id = UUID.randomUUID().toString();
        var input = Instancio.create(UpdateCandidate.class);
        var update = Instancio.of(UpdateCandidate.class)
                .set(field("photo"), input.photo())
                .set(field("givenName"), "Natanael")
                .set(field("familyName"), "Alves")
                .set(field("email"), input.email())
                .set(field("phone"), input.phone())
                .set(field("jobTitle"), input.jobTitle())
                .create();

        when(candidateApi.update(id, input)).thenReturn(Candidate.fromDomain(input.toDomain(id)));
        when(candidateApi.update(id, update)).thenReturn(Candidate.fromDomain(update.toDomain(id)));

        var response1 = given().contentType(MediaType.APPLICATION_JSON).body(input)
                .when().put("/" + id)
                .then().statusCode(RestResponse.StatusCode.OK).extract().as(Candidate.class);

        verify(candidateApi).update(id, input);

        var response2 = given().contentType(MediaType.APPLICATION_JSON).body(update)
                .when().put("/" + id)
                .then().statusCode(RestResponse.StatusCode.OK).extract().as(Candidate.class);

        verify(candidateApi).update(id, update);

        assertEquals(response1.id(), id);
        assertEquals(response2.id(), id);

        assertNotEquals(response1.fullName(), response2.fullName());
        assertEquals(response1.photo(), response2.photo());
        assertEquals(response1.email(), response2.email());
        assertEquals(response1.phone(), response2.phone());
        assertEquals(response1.jobTitle(), response2.jobTitle());

        verifyNoMoreInteractions(candidateApi);
    }

}