package services;

import io.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import rMethods.RestCalls;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimplerTest extends RestCalls {

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
        assertThat(jsonPlaceHolderUserResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        jsonPlaceHolderUserResponse.prettyPrint();
    }

    @Test
    @DisplayName("Verify PostId's of the Given userId's")
    @Order(2)
    public void verifyPostIds() {
        ArrayList<Integer> userIds = jsonPlaceHolderUserResponse.path("id");
        jsonPlaceHolderUserPostsResponse = getRequestWithParameter(POSTS, "userId", userIds);
        assertThat(jsonPlaceHolderUserPostsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        jsonPlaceHolderUserPostsResponse.prettyPrint();
    }

    @Test
    @DisplayName("Verify EmailId's of the Given PostId's by fetching Comments")
    @Order(3)
    public void verifyEmailIds() {
        ArrayList<Integer> postIds = jsonPlaceHolderUserPostsResponse.path("id");
        jsonPlaceHolderUserPostCommentsResponse = getRequestWithParameter(COMMENTS, "postId", postIds);
        assertThat(jsonPlaceHolderUserPostCommentsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        jsonPlaceHolderUserPostCommentsResponse.prettyPrint();
        ArrayList<String> emails = jsonPlaceHolderUserPostCommentsResponse.path("email");
        emails.forEach(email -> {
            assertThat(EmailValidator.getInstance().isValid(email)).withFailMessage("Invalid Email " + email).isTrue();
        });
    }
}
