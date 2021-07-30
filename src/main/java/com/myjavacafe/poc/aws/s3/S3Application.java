package com.myjavacafe.poc.aws.s3;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Application {

  private static final AWSCredentials credentials;
  private static String bucketName;

  static {
    // put your accesskey and secretkey here
    String awsAccessKey = "";//Need provide proper access key
    String awsSecretkey = "";//Need provide proper secret key
    credentials = new BasicAWSCredentials(awsAccessKey, awsSecretkey);
  }

  public static void main(String[] args) throws IOException {

    String existingBucketName = "mtestsm-bucket";
    // set-up the client
    AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.US_EAST_2).build();

    System.out.println("***********S3 Client created successfully************");
    AWSS3Service awsService = new AWSS3Service(s3client);

    System.out.println("***********awsService created successfully************");
    bucketName = "mtestsm-bucket3";

    // creating a bucket
    if (awsService.doesBucketExist(bucketName)) {
      System.out.println("Bucket name is not available." + " Try again with a different Bucket name.");
      System.out.println("***********Given bucket found, Please try othername************");
      return;
    }

    System.out.println("***********Strated bucket creation************");
    awsService.createBucket(bucketName);
    System.out.println("***********Bucket created successfully************");
    // list all the buckets
    System.out.println("***********List of buckets in AWS************");
    for (Bucket s : awsService.listBuckets()) {
      System.out.println(s.getName());
    }
    System.out.println("***********Delete bucket AWS************");
    // deleting bucket
    // awsService.deleteBucket("mtestsm-bucket-test2");

    String localFilePath = "/Users/sitaram/go/src/github.kyndryl.net/workings/spring-boot-h2-database-crud/src/main/java/com/kyn/sn/testfiles";
    // uploading object
    System.out.println("***********Update bucket with file************");
    awsService.putObject(bucketName, "Document/hello.txt", new File(localFilePath + "/hello.txt"));

    System.out.println("***********Update bucket with image************");
    awsService.putObject(bucketName, "Images/test.ping", new File(localFilePath + "/test.png"));

    System.out.println("***********Get buckets list************");
    // listing objects
    ObjectListing objectListing = awsService.listObjects(bucketName);
    for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
      System.out.println("key:: " + os.getKey());
    }

    System.out.println("***********Get specific bucket************");
    // downloading an object
    S3Object s3object = awsService.getObject(bucketName, "Document/hello.txt");
    S3ObjectInputStream inputStream = s3object.getObjectContent();
    FileUtils.copyInputStreamToFile(inputStream, new File(localFilePath + "/hello.txt"));

    System.out.println("***********Copy object to specific bucket************");
    // copying an object
    awsService.copyObject(bucketName, "Images/test.ping", existingBucketName, "Images/picture.png");

    System.out.println("***********Delete bucket************");
    // deleting an object
    awsService.deleteObject(bucketName, "Document/hello.txt");

    System.out.println("***********Get existing buckets list************");
    // listing objects
    objectListing = awsService.listObjects(existingBucketName);
    for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
      System.out.println("key:: " + os.getKey());
    }

    System.out.println("***********Delete multiple buckets************");
    // deleting multiple objects
    String objkeyArr[] = { "Document/hello.txt", "Images/picture.png" };

    DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(existingBucketName).withKeys(objkeyArr);
    awsService.deleteObjects(delObjReq);

    System.out.println("***********Successfully done all the operations************");
  }
}
