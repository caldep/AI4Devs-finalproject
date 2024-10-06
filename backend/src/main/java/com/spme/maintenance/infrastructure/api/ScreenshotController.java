package com.spme.maintenance.infrastructure.api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@RequestMapping("/resources")
public class ScreenshotController {

    @Value("${alert.screenshot.bucket}")
    private String screenshotBucket;

    @Value("${amazon.dynamodb.region}")
    private String region;

    @Value("${amazon.dynamodb.access-key}")
    private String accessKey;

    @Value("${amazon.dynamodb.secret-key}")
    private String secretKey;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(this.accessKey, this.secretKey)))
                .withRegion(this.region)
                .build();
    }

    @PostMapping("/save-screenshot")
    public ResponseEntity<String> saveScreenshot(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("fileName") String fileName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(screenshotBucket, fileName, file.getInputStream(), metadata);

            return ResponseEntity.ok("Captura de pantalla guardada exitosamente en S3");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la captura de pantalla: " + e.getMessage());
        }
    }
}
