# Document Microservice

This is a microservice to do the document management for the Loan Origination
System. 

## API Documentation

The API description for the microservice is as follows:

| API endpoint                                         | Description                                                                                            |
|------------------------------------------------------|--------------------------------------------------------------------------------------------------------| 
| `POST /api/v1/documents/upload`                      | Helps the user to upload a document, generating a temporary `MinIO` upload URL, along with metadata    | 
| `POST /api/v1/documents/download`                    | Helps the user to download the document, generating a secure `MinIO` download URL, along with metadata |
| `GET /api/v1/documents/applications/{applicationId}` | Helps the user to fetch all the documents from the database for a given `applicationId`                |
| `DELETE /api/v1/documents/delete?storageKey=<key>`   | Helps the user to delete the document at the given `storageKey`.                                       |


## How to run locally?

To run the project locally on your device, do the following:

- **Step 1:** Create an `application-local.yaml` in the directory `src/main/java/resources`.

For this, we have provided a sample `application-local.txt`. Copy this and fill in some value(s) that are left
blank.

- **Step 2:** Setup two important services for pre-requisites: 
    - [MinIO document storage](https://github.com/amanfoundongithub/LOS-Document-Service/blob/develop/docs/SETUP_MINIO.md)
    - [ClamAV virus scanner](https://github.com/amanfoundongithub/LOS-Document-Service/blob/develop/docs/SETUP_CLAMAV.md)


- **Step 3:** Run the service, after the two steps are done. 

Run the `DocumentServiceApplication.main()` to start the server. 

The server will start on the port `5800` on `localhost`.

