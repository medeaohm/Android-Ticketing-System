### REST Server (Web Api ASP) Routes

Action | Method | Description | Body/Query | Response | Notes 
--- | --- | --- | --- | --- | ---  
**/token** | `POST` | Gets Autotization token | ```{ pasword: "", username: "", grant_type: "password"  }``` | ```{ "access_token": "LongStringOfCharacters", "token_type": "bearer", "expires_in": 1209599, "userName": "admin", ".issued": "Sun, 25 Dec 2016 09:13:57 GMT", ".expires": "Sun, 08 Jan 2017 09:13:57 GMT", Roles: "Administrator, Inspector"}```  | Content-Type Header Should be **application/x-www-form-urlencoded**
**api/account/register** | `POST`| User Registration | ```{ firstName: "", lastName: "", username: "", email: "", password: "", confirmPassword: ""  }``` | Password should have at least 6 symbols 
**api/tickets/buy** | `POST` | Buy new ticket | ```{ hours: 2 }``` | ```{ QRCode: "LongStringOfCharacters", Cost: "1.60" }```| Requires authorization. Cost is in leva
**api/tickets/** | `GET` | Shows all tickets for current logged user | - | ```[{ Id: "GUID", BoughtAt: "Date", Cost: "Decimal", Expired: "Bool", "Activated": "Bool", "DateActivated": "Date/null", "ExpiresOn": "Date/null", Duration: "NumberHours", QRCode: "LongText", Owner: { "Owner": { Id: "GUID"  UserName: "Text", FullName: "Text"} }  ]``` | Requires authorization 
**api/tickets/all?count=2** | `GET` | Same as above but for all users | count is optional positive number | `Same as above` | for Administrators\*\*
**api/tickets/byId?id=GUID** | `GET` | Returns info of single ticket | id is **required** | `Same as above but for single item` | 
**api/tickets/isValid?id=GUID** | `GET`| Checks whether a ticked is valid (Activated and not Expired) | id is **required** | `true/false` | for Inspectors\*\*
**api/tickets/allByUsername?username=text** | `GET`| Gets tickets for a given user by username |  username is **required**, count is optional | See *api/tickets/* 
**api/tickets/allByUserid?id=GUID** | `GET` | Gets tickets for a given user by its id |  id is **required**, count is optional | See *api/tickets/* 
**api/tickets/activate** | `PUT` | Activates a ticked |  ```{ id: "GUID"}``` | ```{ Message = "Successfully activated/Ticked already activated.", ExpiresOn = "Date" }``` | Requires authorization
**api/users/** | `GET` | Gets all registered users | - | ```[ { FirstName: "Text", LastName: "Text", Email: "Text", Tickets: [], Roles: [], Id: "GUID", UserName: "Text", FullName: "", Avatar: "string", FileExtension: "jpg|png", Balance: "number" }]``` | for Administrators\*\*
**api/users/info/** | `GET` | Returns info about current logged user | - | `Simliar to above but for single user - not array` | 
**api/users/byid?id=GUID** | `GET` | Returns info about user by id | id is **required** | `Same as above` | 
**api/users/** | `POST` | Creates a new user (same as *api/account/register*) | `check api/account/register/` | `Same as above` | 
**api/users/** | `PUT` | Updates a user | ```{ Id: "GUID", "FirstName": "Text", LastName: "Text", Email: "Text", UserName: "Text" }``` | No response - only status code 200 - OK.
**api/users/** | `DELETE` | Removes a user | ```{ Id: "GUID" } ``` |  No response - only status code 200 - OK. 
**api/users/charge** | `PUT` | Adds money to current user account | ```{ CardNumer: "string", SecurityCode: "string", CardType: "string",  ExpireMonth: "number (1-12)", ExpireYear: "number", CardHolderNames: "string", Amount: "number"} ``` |  No response - only status code 200 - OK.
**api/users/avatar** | `PUT` | Updates the profile picture of current user. | - | No response - only status code 200 - OK.
**api/news** | `GET` | Get all the news | - | [{ Id: "Integer", Title: "Text", Content: "Text", CreatedOn: "Date", "Comments": "{ ["Comment": { Id: "Integer"  Content: "Text",  Author: "Text",  CreatedOn: "Date",  NewsItemId: "Integer"}]}]```
**api/news/{id}** | `GET` | Get the news with the provided id | - | Same as above but a single item is returned
**api/news/post** | `POST` | Creates new news | - | No response - only status code 200 - OK. | for Administrators\*\*
**api/news/delete/{id}** | `POST` | Deletes the news with the provided id | - | No response - only status code 200 - OK. | for Administrators\*\*
**api/comments** | `GET` | Get all the comments | - | { Id: "Integer",  Content: "Text", Author: "Text", CreatedOn: "Date", NewsItemId: "Integer"}```
**api/comments/byNews/{id}** | `GET` | Get all the comments related to the news with the provided id | - | Same as above but an array of items is returned
**api/comments/post** | `POST` | Creates new comment| - | No response - only status code 200 - OK. | Requires authorization and the current user being the owner of the comment \*\*
**api/comments/delete/{id}** | `POST` | Deletes the comment with the provided id | - | No response - only status code 200 - OK. | Requires authorization and the current user being the owner of the comment\*\*