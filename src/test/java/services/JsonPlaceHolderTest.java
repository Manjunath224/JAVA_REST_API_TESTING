package services;

import io.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.*;
import rMethods.RestCalls;

import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceHolderTest extends RestCalls {

    public static Response jsonPlaceHolderUserResponse, jsonPlaceHolderUserPostsResponse, jsonPlaceHolderUserPostCommentsResponse;

    public static String[] getQueryNames() {
        return new String[]{"Delphine", "Antonette"};
    }

    @Tag("Smoke")
    @Test
    @DisplayName("Verify Status code of get User service")
    @Order(1)
    public void verifyUsersStatusCode() {
        jsonPlaceHolderUserResponse = getRequestWithParameter(USERS, "username", getQueryNames());
        jsonPlaceHolderUserResponse.then().log().ifError().assertThat().statusCode(HttpsURLConnection.HTTP_OK)
                .assertThat().body(matchesJsonSchemaInClasspath("schemas/users.json"));
    }

    @Test
    @DisplayName("Verify PostId's of the Given userId's")
    @Order(2)
    public void verifyPostIds() {
        ArrayList<Integer> userIds = jsonPlaceHolderUserResponse.path("id");
        jsonPlaceHolderUserPostsResponse = getRequestWithParameter(POSTS, "userId", userIds);
        jsonPlaceHolderUserPostsResponse.then().log().ifError().assertThat().statusCode(HttpsURLConnection.HTTP_OK)
                .assertThat().body(matchesJsonSchemaInClasspath("schemas/posts.json"));
    }

    @Test
    @DisplayName("Verify EmailId's of the Given PostId's by fetching Comments")
    @Order(3)
    public void verifyEmailIds() {
        ArrayList<Integer> postIds = jsonPlaceHolderUserPostsResponse.path("id");
        jsonPlaceHolderUserPostCommentsResponse = getRequestWithParameter(COMMENTS, "postId", postIds);
        jsonPlaceHolderUserPostCommentsResponse.then().log().ifError().assertThat().statusCode(HttpsURLConnection.HTTP_OK)
                .assertThat().body(matchesJsonSchemaInClasspath("schemas/comments.json"));
        ArrayList<String> emails = jsonPlaceHolderUserPostCommentsResponse.path("email");
        emails.forEach(email ->
                assertThat("Email not valid: " + email, EmailValidator.getInstance().isValid(email), is(equalTo(true))));
    }
}
