package com.spme.maintenance.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class PredictionModelBucketS3 {

    @Value("${prediction.model.bucket}")
    private String bucket;

    @Value("${prediction.model.key}")
    private String key;

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
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(this.region)
            .build();
    }

    public InputStream getModelInputStream() {
        S3Object s3Object = s3Client.getObject(bucket, key);
        return s3Object.getObjectContent();
    }
}
