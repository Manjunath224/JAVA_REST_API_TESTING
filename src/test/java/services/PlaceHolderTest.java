package services;

import io.restassured.response.Response;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import rMethods.RestCalls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("JsonPlaceHolderCheck")
public class PlaceHolderTest extends RestCalls {

    public static Response jsonPlaceHolderUserResponse, jsonPlaceHolderUserPostsResponse, jsonPlaceHolderUserPostCommentsResponse;
    public static ArrayList<Integer> userIds = new ArrayList<>();
    public static HashMap<Integer, ArrayList<Integer>> postIds = new HashMap<>();
    public static HashMap<ArrayList<Integer>, ArrayList<String>> commentIDAndEmail = new HashMap<>();


    public static String[] getQueryNames() {
        return new String[]{"Delphine", "Antonette"};
    }

    public static void addUserIds(int userIndex) {
        userIds.add((Integer) ((ArrayList<?>) jsonPlaceHolderUserResponse.path("id")).get(userIndex));
    }

    public static Integer[] getUserIds() {
        return userIds.toArray(new Integer[0]);
    }

    public static ArrayList<Integer> getAllPostIds() {
        ArrayList<Integer> allPostIds = new ArrayList<>();
        for (HashMap.Entry<Integer, ArrayList<Integer>> postId : postIds.entrySet()) {
            allPostIds.addAll(postId.getValue());
        }
        return allPostIds;
    }

    public static HashMap<ArrayList<Integer>, ArrayList<String>> getAllCommentIdAndEmailId() {
        HashMap<ArrayList<Integer>, ArrayList<String>> allCommentAndEmailId = new HashMap<>();
        ArrayList<Integer> allCommentIds = new ArrayList<>();
        ArrayList<String> allEmailIds = new ArrayList<>();
        for (HashMap.Entry<ArrayList<Integer>, ArrayList<String>> commentEmail : commentIDAndEmail.entrySet()) {
            allCommentIds.addAll(commentEmail.getKey());
            allEmailIds.addAll(commentEmail.getValue());
        }
        allCommentAndEmailId.put(allCommentIds, allEmailIds);
        System.out.println(commentIDAndEmail);
        System.out.println(allCommentAndEmailId.size());
        return allCommentAndEmailId;
    }

    public static String[] getAllEmailIds() {
        ArrayList<String> emails = new ArrayList<>();
        for (Map.Entry<ArrayList<Integer>, ArrayList<String>> arrayListArrayListEntry : getAllCommentIdAndEmailId().entrySet()) {
            emails.addAll((ArrayList<String>) ((HashMap.Entry) arrayListArrayListEntry).getValue());
        }
        return emails.toArray(new String[0]);
    }

    @BeforeAll
    public static void beforeAll() {
        jsonPlaceHolderUserResponse = getRequestWithParameter(USERS,"username","");
        jsonPlaceHolderUserResponse.prettyPrint();
    }

    @Test
    @Tag("Smoke")
    @DisplayName("Verify Status code of get User service")
    @Order(1)
    public void verifyStatusCode() {
        assertThat(jsonPlaceHolderUserResponse.statusCode()).isEqualTo(200);
    }

    @DisplayName("Verify if the expected UserName exist in the array of Response")
    @ParameterizedTest(name = "{index}] QueryUserName = ''{0}''")
    @MethodSource("getQueryNames")
    @Order(2)
    public void verifyUsername(String userName) {
        int userIndex = ((ArrayList<?>) jsonPlaceHolderUserResponse.path("username")).indexOf(userName);
        assertThat(userIndex).isGreaterThanOrEqualTo(0);
        addUserIds(userIndex);
    }

    @DisplayName("Verify the Posts Id's of the given User")
    @ParameterizedTest(name = "{index}] UserId = ''{0}''")
    @MethodSource("getUserIds")
    @Order(3)
    public void verifyPostIds(Integer userId) {
        jsonPlaceHolderUserPostsResponse = getRequestWithParameter(POSTS, "userId", userId.toString());
        ArrayList<Integer> postIdForUser = jsonPlaceHolderUserPostsResponse.path("id");
        assertThat(postIdForUser.size()).isPositive();
        postIds.put(userId, postIdForUser);
    }

    @DisplayName("Verify the comment Id and email Id for Posts Id's")
    @ParameterizedTest(name = "{index}] PostId = ''{0}''")
    @MethodSource("getAllPostIds")
    @Order(4)
    public void verifyCommentsId(Integer postId) {
        jsonPlaceHolderUserPostCommentsResponse = getRequestWithParameter(COMMENTS, "postId", postId.toString());
        ArrayList<String> emailIdForComments = jsonPlaceHolderUserPostCommentsResponse.path("email");
        ArrayList<Integer> commentIdForComments = jsonPlaceHolderUserPostCommentsResponse.path("id");
        assertThat(emailIdForComments.size()).isPositive();
        assertThat(commentIdForComments.size()).isPositive();
        commentIDAndEmail.put(commentIdForComments, emailIdForComments);
    }

    @DisplayName("Verify if the email Id is valid")
    @ParameterizedTest(name = "{index}] EmailId = ''{0}''")
    @MethodSource("getAllEmailIds")
    @Order(5)
    public void verifyEmailId(String email) {
        assertThat(EmailValidator.getInstance().isValid(email)).withFailMessage("Invalid Email " + email).isTrue();
    }


}

