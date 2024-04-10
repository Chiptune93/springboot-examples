# REST API SAMPLE

<b>Design Guide</b>

1. Make REST URI Simple and Intuitive
- Sub Resource Expression
    - /users/{userId}/devices : basic way
    - /users/{userId}/<b>likes</b>/devices : Relation of user and devices

2. Error Handling
- return error code and error detail
    - 200 OK
    - 400 Bad Request
    - 401 Unauthorized
    - 404 Not found
    - 500 Internal Server Error
- Do not return error stack trace

3. API Version Management
- Support Lower compatibility & Management
    - /account/<b>v2.0</b>/groups : version marking

4. (Optional) Paging
- Huge Data -> require paging
- use get parameter
    - /record?offset=100&limit=25 : get 100th record to 125th record

5. Partial Response
- request with fields what you want then server return that fields
    - /get?fields=id,name

6. Use Single Endpoint
- request matching same routing server
    - api.apiserver.com/users -> users.apiserver.com
    - api.apiserver.com/car -> car.apiserver.com

- others recommend things
    - use http protocol
    - use standard json library
    - make short response time as you can
    - authorize policy (keym token ...)
    - documentation and test guide
