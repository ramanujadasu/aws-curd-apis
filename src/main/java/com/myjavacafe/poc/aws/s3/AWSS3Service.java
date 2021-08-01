package com.myjavacafe.poc.aws.s3;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.myjavacafe.poc.exceptions.AccessDeniedException;

public class AWSS3Service {
    private final AmazonS3 s3client;
    private static Logger logger = LoggerFactory.getLogger(AWSS3Service.class);

    public AWSS3Service() {
        this(new AmazonS3Client() {
        });
    }

    public AWSS3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    // is bucket exist?
    public boolean doesBucketExist(String bucketName) {
        return s3client.doesBucketExistV2(bucketName);
    }

    // create a bucket
    public Bucket createBucket(String bucketName) {
        return s3client.createBucket(bucketName);
    }

    // list all buckets
    public List<Bucket> listBuckets() {
    	try {
    		return s3client.listBuckets();
    	}catch(Exception ade) {
    		logger.error("Exception", ade);
    		throw new AccessDeniedException(ade.getMessage());
    	}
    }

    // delete a bucket
    public void deleteBucket(String bucketName) {
        s3client.deleteBucket(bucketName);
    }

    // uploading object
    public PutObjectResult putObject(String bucketName, String key, File file) {
        return s3client.putObject(bucketName, key, file);
    }

    // listing objects
    public ObjectListing listObjects(String bucketName) {
        return s3client.listObjects(bucketName);
    }

    // get an object
    public S3Object getObject(String bucketName, String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    }

    // copying an object
    public CopyObjectResult copyObject(String sourceBucketName, String sourceKey, String destinationBucketName,
            String destinationKey) {
        return s3client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
    }

    // deleting an object
    public void deleteObject(String bucketName, String objectKey) {
        s3client.deleteObject(bucketName, objectKey);
    }

    // deleting multiple Objects
    public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
        return s3client.deleteObjects(delObjReq);
    }
}
