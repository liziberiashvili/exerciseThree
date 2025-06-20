Feature: JSON Placeholder Users Data

  Background:
    Given the base URI is "https://jsonplaceholder.typicode.com"


  Scenario: Validate user list
    When I send a GET request to "/users"
    Then the response status code is 200
    And the response contains list of users

  Scenario: Validate user data contains specific username and email
    When I send a GET request to "/users/2"
    Then the response should contain data with "Antonette" and "Shanna@melissa.tv"

  Scenario: Validate user creation
    When I send a POST request to "/users" with "body"
    Then the response status code is 201
    And verify body after creation

  Scenario: Validate user update
    When I send a PUT request to "users/2" with "body"
    Then the response status code is 200
    And the response contains updated values

  Scenario: Validate user delete
    When I send a DELETE request to "users/1"
    Then the response status code is 200
    
  Scenario: Validate user websites ends with allowed top domain
    When I send a GET request to "/users"
    Then all user websites end with an allowed top domain

  Scenario: Validate that /users endpoint returns 10 users data
    When I send a GET request to "/users"
    Then Response should contain exactly 10 users data

  Scenario: Validate user address object inside the response
    When I send a GET request to "/users/1"
    Then Response should contain user address data with "street": "Kulas Light","city": "Gwenborough"

  Scenario: Validate POST request with empty name value
    When I send a POST request to "/users" with no body
    Then Response should contain no validation error

  Scenario Outline: Validate all user id, name, username and email
    When I send a GET request to "/users/<id>"
    Then Response should contain "<id>", "<name>", "<username>", and "<email>"
  Examples:
    | id | name | username | email |
    | 1  | Leanne Graham | Bret | Sincere@april.biz |
    | 2  | Ervin Howell  | Antonette | Shanna@melissa.tv |
    | 3  | Clementine Bauch | Samantha | Nathan@yesenia.net |
    | 4   | Patricia Lebsack | Karianne | Julianne.OConner@kory.org |
    | 5   | Chelsey Dietrich | Kamren   | Lucio_Hettinger@annie.ca  |
    | 6   | Mrs. Dennis Schulist | Leopoldo_Corkery | Karley_Dach@jasper.info |
    | 7   | Kurtis Weissnat      | Elwyn.Skiles     | Telly.Hoeger@billy.biz  |
    | 8   | Nicholas Runolfsdottir V | Maxime_Nienow | Sherwood@rosamond.me   |
    | 9   | Glenna Reichert          | Delphine      | Chaim_McDermott@dana.io |
    | 10  | Clementina DuBuque       | Moriah.Stanton | Rey.Padberg@karina.biz |
