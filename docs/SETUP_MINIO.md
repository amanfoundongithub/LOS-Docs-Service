## Setup MinIO Locally

This project uses MinIO to store files, since AWS S3
is expensive to use. 

Since the interface of MinIO is similar
to AWS, it is just a URL change once we switch to AWS S3.

This guide will teach you how to setup this locally so 
that the `document_service` can easily work flawlessly.

### Step 1: Setup MinIO image 

- Create a local directory for minio's data:
```bash
mkdir -p ~/minio/data
```

A physical disk folder will be allocated, in case you decide to 
shut down minio's image.

- Spin up MinIO using Docker:
```bash 
docker run -d \
  -p 9000:9000 \
  -p 9001:9001 \
  --name minio-local \
  -e "MINIO_ROOT_USER=<Your username>" \
  -e "MINIO_ROOT_PASSWORD=<Your password>" \
  -v ~/minio/data:/data \
  --restart always \
  quay.io/minio/minio server /data --console-address ":9001"
```
This will run the docker in background.

(**Note:** Remember to keep at least 8 characters in `MINIO_USER_PASSWORD`
otherwise it will cause an error in the Docker setup.)

### Step 2: Setup MinIO Web Hook

We will setup web hooks on the MinIO container to ensure
that we get an event whenever the document is successfully
uploaded.

- Open bash terminal for MinIO:
```bash
docker exec -it minio-local bash
```

- Configure MinIO client class:
```bash 
mc alias set local http://localhost:9000 <Your MinIO username> <Your MinIO password>
```

- Create a bucket named `documents`:
```bash
mc mb local/documents
```

- Create a web hook for emitting event:
```bash
mc admin config set local notify_webhook:endpoint1 \
  endpoint="http://host.docker.internal:5800/api/v1/internal/minio-callback" \
  auth_token="<Your secret token here>" \
  queue_limit="1000"
```
(Note: Spin up the server, before running this command.)

- Restart the MinIO application:
```bash 
mc admin service restart local
```

- Bind your `documents` container to upload event 
on webhook:
```bash 
mc event add local/documents arn:minio:sqs::endpoint1:webhook --event put
```

After doing all this, you will be able to set the 
MinIO local instance for starting this application.

### Step 3: Configure Project
The final step requires you to set the following variable in `application-local.yaml`
as follows:
```bash 
MINIO_SECRET_TOKEN: <Your token that you passed as auth_token>
```

Add this token, otherwise the application will mark all
the transactions from the MinIO as `UNAUTHORIZED`!

### Step 4: Run The Project
If all steps worked success, then you should be able to start working
on the project flawlessly! 

### Any Issues?
Contact me if you find any discrepancy!! Visit my GitHub profile, if you 
need any assistance on setup.