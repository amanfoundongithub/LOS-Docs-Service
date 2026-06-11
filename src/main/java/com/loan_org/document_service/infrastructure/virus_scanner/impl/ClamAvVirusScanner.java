package com.loan_org.document_service.infrastructure.virus_scanner.impl;


import com.loan_org.document_service.infrastructure.virus_scanner.VirusScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ClamAvVirusScanner implements VirusScanner {

    @Value("${clamav.host}")
    private String host;

    @Value("${clamav.port}")
    private int port;

    @Value("${clamav.timeout}")
    private int timeOutInMilliSeconds;

    @Value("${clamav.chunk_size}")
    private int chunkSize;


    @Override
    public boolean scan(InputStream fileStream) {
        log.info("[CLAMAV_VIRUS_SCANNER] Opening TCP connection to ClamAV daemon...");

        try (Socket socket = new Socket()) {

            socket.connect(new InetSocketAddress(host, port), timeOutInMilliSeconds);
            socket.setSoTimeout(timeOutInMilliSeconds);

            try (OutputStream out = socket.getOutputStream();
                 DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(out));
                 InputStream in = socket.getInputStream()) {

                dos.write("zINSTREAM\0".getBytes(StandardCharsets.US_ASCII));
                dos.flush();

                byte[] buffer = new byte[chunkSize];
                int read;
                while ((read = fileStream.read(buffer)) != -1) {
                    dos.writeInt(read);
                    dos.write(buffer, 0, read);
                }

                dos.writeInt(0);
                dos.flush();

                String response = new String(in.readAllBytes(), StandardCharsets.US_ASCII).trim();
                log.info("[CLAMAV_VIRUS_SCANNER] Raw response parsed from daemon: '{}'", response);

                if (response.contains("OK")) {
                    log.info("[CLAMAV_VIRUS_SCANNER] Security clearance verified. Document is clean!");
                    return true;
                } else if (response.contains("FOUND")) {
                    log.warn("[SECURITY_ALERT] Virus malicious signature detected by engine: {}", response);
                    return false;
                }

                log.error("[CLAMAV_VIRUS_SCANNER] Unexpected response message structure from scanner.");
                return false;
            }
        } catch (Exception e) {
            log.error("[CLAMAV_VIRUS_SCANNER] Communication or streaming error with ClamAV server engine", e);
            return false;
        }
    }
}