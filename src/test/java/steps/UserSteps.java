package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class UserSteps {
    private Response response;

    // @Given ანოტაცია: აღნიშნავს საწყის მდგომარეობას, სადაც ჩვენ ვადგენთ API-ს ბაზურ URI-ს.
    @Given("the base URI is {string}")
    public void theBaseURIIs(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    // @When ანოტაცია: აღნიშნავს მოქმედებას, სადაც ვაგზავნით GET მოთხოვნას კონკრეტულ endpoint-ზე.
    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        response = RestAssured.given().log().all().get(endpoint);
    }

    // @Then ანოტაცია: აღნიშნავს შემოწმებას, სადაც ვამოწმებთ, რომ პასუხის სტატუს კოდი სწორია.
    @Then("the response status code is {int}")
    public void theResponseStatusCodeIs(int statusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, statusCode,
                "Unexpected status code. Expected: " + statusCode + ", Actual: " + actualStatusCode);
    }

    // @And ანოტაცია: ვამოწმებთ რომ დაბრუნებული პასუხი შეიცავს იუზერების მონაცემებს
    @And("the response contains list of users")
    public void theResponseContainsListOfUsers() {
        List<String> users = response.jsonPath().getList("name");
        Assert.assertFalse(users.isEmpty());
    }

    // @Then ანოტაცია: ვამოწმებთ რომ დაბრუნებულ პასუხზე მოდის სწორი მონაცემები
    @Then("the response should contain data with {string} and {string}")
    public void theResponseShouldContainDataWithAnd(String userName, String email) {
        Assert.assertTrue(response.asString().contains(userName));
        Assert.assertTrue(response.asString().contains(email));
    }

    // @When ანოტაცია: აღნიშნავს მოქმედებას, სადაც ვაგზავნით POST მოთხოვნას კონკრეტულ endpoint-ზე
    @When("I send a POST request to {string} with {string}")
    public void iSendAPOSTRequestToWith(String endpoint, String jsonBody) {
        jsonBody = "{ \"name\":\"" + "Lizi" + "\", \"email\":\"liziberiashvili98@gmail.com\" }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .log().body()
                .when()
                .post(endpoint);
    }

    // @And ანოტაცია: ვამოწმებთ,რომ რესპონსი აბრუნებს სწორ body-ს
    @And("verify body after creation")
    public void verifyBodyAfterCreation() {
        response.then()
                .log().body()
                .body("name", equalTo("Lizi"))
                .body("email", equalTo("liziberiashvili98@gmail.com"));
    }

    // @When ანოტაცია: აღნიშნავს მოქმედებას, სადაც ვაგზავნით PUT მოთხოვნას კონკრეტულ endpoint-ზე
    @When("I send a PUT request to {string} with {string}")
    public void iSendAPUTRequestToWith(String endpoint, String jsonBody) {
        jsonBody = "{ \"name\":\"" + "Lizi" + "\", \"age\":26 }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .log().body()
                .when()
                .put(endpoint);
    }
    // @And ანოტაცია: ვამოწმენთ, რომ რესპონსი აბრუნებს შეცვლილ body-ს
    @And("the response contains updated values")
    public void theResponseContainsUpdatedValues() {
        response.then()
                .log().body()
                .body("name", equalTo("Lizi"))
                .body("age", equalTo(26));
    }
    // @When ანოტაცია: აღნიშნავს მოქმედებას, სადაც ვაგზავნით DELETE მოთხოვნას კონკრეტულ endpoint-ზე.
    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        response = RestAssured.given()
                .when()
                .delete(endpoint)
                .then()
                .assertThat()
                .statusCode(anyOf(is(200), is(204)))
                .body(emptyOrNullString())
                .extract().response();

    }
    // @Then ანოტაცია: ვამოწმებთ, რომ დაბრუნებულ პასუხზე მოდის დომეინის სწორი მონაცემები
    @Then("all user websites end with an allowed top domain")
    public void allUserWebsitesEndWithAnAllowedTopDomain() {
        String[] allowedDomains = {".org", ".info", ".biz", ".io", ".com", ".net"};
        List<String> userWebsites = response.jsonPath().getList("website");
        System.out.println(userWebsites);
        for(String userWebsite : userWebsites){
            boolean isValid = false;
            for(String domain : allowedDomains){
                if (userWebsite.endsWith(domain)){
                    isValid = true;
                    break;
                }
            } Assert.assertTrue(isValid,
                    "Website does not end with an allowed domain: " + userWebsite);
        }

    }
    // @Then ანოტაცია: ვამოწმებთ, რომ დაბრუნებული პასუხი შეიცავს კონკრეტული რაოდენობის იუზერებს
    @Then("Response should contain exactly {int} users data")
    public void responseShouldContainExactlyUsersData(int expectedCount) {
        List<String> users = response.jsonPath().getList("id");
        Assert.assertEquals(users.size(), expectedCount, "Users count mismatch!");
    }

    // @Then ანოტაცია: ვამოწმებთ, რომ დაბრუნებული პასუხზე მოდის სწორი მონაცემები
    @Then("Response should contain user address data with {string}: {string},{string}: {string}")
    public void responseShouldContainUserAddressDataWith(String key1, String value1, String key2, String value2) {
        Map<String, String> addressData = response.jsonPath().getMap("address");
        System.out.println(addressData);
        Assert.assertTrue(addressData.containsKey(key1));
        Assert.assertTrue(addressData.containsValue(value1));
        Assert.assertTrue(addressData.containsKey(key2));
        Assert.assertTrue(addressData.containsValue(value2));
    }

    // @When ანოტაცია: აღნიშნავს მოქმედებას, სადაც ვაგზავნით POST მოთხოვნას კონკრეტულ endpoint-ზე ცარიელი body-თ
    @When("I send a POST request to {string} with no body")
    public void iSendAPOSTRequestToWithNoBody(String endpoint) {
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("")
                .log().body()
                .when()
                .post(endpoint);
    }
    //@Then ანოტაცია: ვამოწმებთ, რომ დაბრუნებული პასუხი არ შეიცავს ე.წ. validation error-ს
    @Then("Response should contain no validation error")
    public void responseShouldContainNoValidationError() {
       String respBody = response.then().log().body().extract().response().asString();
        Assert.assertEquals(respBody, "{\n" + "  \"id\": 11\n" + "}",
                "Response body mismatch!");
    }

    //@Then ანოტაცია: ვამოწმებთ თითოეულ იუზერზე აიდის,სახელის,იუზერნეიმისა და მეილის მონაცემებს
    @Then("Response should contain {string}, {string}, {string}, and {string}")
    public void responseShouldContainAnd(String expectedId, String expectedName, String expectedUsername, String expectedEmail) {

        String actualId = response.jsonPath().getString("id");
        String actualName = response.jsonPath().getString("name");
        String actualUsername = response.jsonPath().getString("username");
        String actualEmail = response.jsonPath().getString("email");
        Assert.assertEquals(actualId, expectedId, "Id mismatch!");
        Assert.assertEquals(actualName, expectedName, "Name mismatch!");
        Assert.assertEquals(actualUsername, expectedUsername, "Username mismatch!");
        Assert.assertEquals(actualEmail, expectedEmail, "Email mismatch!");
    }
}