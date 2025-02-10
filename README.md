# HeliosX
## Take Home exercise

### Design

1. Applications api contains two endpoints
    - get questions `/api/1/consultation/questions`, which accepts GET request and retrieves list of the questions required for consultation.
    - post answers  `/api/1/consultation/`, which accepts POST request with JSON document that contains answers to the questions, and retrieves result of consultation
2. `docs` folder contains postman collection   



### Shortcuts
1. To simplify process of handling response, I assumed answers to all questions would be boolean.
2. Questions are stored in JSON file.
3. Mandatory questions have expected value to be null.
4. For validation of entity, I used Spring hence I lost ability to have meaningful message 
5. Reduced amount of Unit tests as most functionality is covered by integration tests
