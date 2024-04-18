package infrastructure.resources;

import api.CandidateApi;
import api.dto.in.CreateCandidate;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.instancio.Instancio;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
}