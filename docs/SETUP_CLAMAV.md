## Setup ClamAV locally

This project uses ClamAV, an open source tool
to detect viruses and malware.

Using this is highly recommended, as this is a working
prototype.

### Step 1: Setup ClamAV image
- To setup image using docker, do the following command:
```bash 
docker run -d \
  --name clamav-server \
  -p 3310:3310 \
  -e CLAMAV_NO_FRESHCLAM_ON_STARTUP=true \
  mailserver/clamav:latest
```

This starts the ClamAV server live on port 3310

### Step 2: Verification

After you run this, and no issues come up, run this command
to see ClamAV terminal output:
```bash 
docker logs -f clamav-server  
```

If no problematic or error logs show up, it means that ClamAV is running successfully behind the scenes!
