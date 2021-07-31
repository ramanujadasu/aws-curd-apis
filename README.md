# aws-curd-apis

To run this project follow bellow steps:

Step1:

Install Java8,Eclipse,Postman

Step2:

Please provide aws credetials

aws access key and aws secret key in the bellow file(line 46 and 47)

/aws-curd-apis/src/main/java/com/myjavacafe/poc/controller/AwsS3Controller.java

Step3:

Run maven clean

Run maven install

Step4:

Run main program /aws-curd-apis/src/main/java/com/myjavacafe/poc/AWSCurdOperationsApplication.java

Step5:

Once started, console will show "Started AWSCurdOperationsApplication in XXX seconds"

Step6:

Import the /aws-curd-apis/postman/AWS.postman_collection.json collecton in postman

Step7:

Verify apis with images provided in postman folder for reference


# AWS S3 Operations:
===
API: API Health

REQUEST METHOD: GET

REQUEST URL: http://localhost:8080/actuator/health

===
API: Get all buckets

REQUEST METHOD: GET

REQUEST URL: http://localhost:8080/aws/s3/buckets

=== 
API: Get bucket by bucketName

REQUEST METHOD: GET

REQUEST URL: http://localhost:8080/aws/s3/buckets/{bucketName}

===
API: Create bucket

REQUEST METHOD: POST

REQUEST URL: http://localhost:8080/aws/s3/buckets

REQUEST BODY:
{
    "bucketName":"testnew-23"
}

===
API: Delete bucket

REQUEST METHOD: DELETE

REQUEST URL: http://localhost:8080/aws/s3/buckets/{bucketName}

===
