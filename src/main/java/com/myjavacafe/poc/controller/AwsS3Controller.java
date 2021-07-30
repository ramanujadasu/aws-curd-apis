package com.myjavacafe.poc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.myjavacafe.poc.aws.s3.AWSS3Service;
import com.myjavacafe.poc.model.BucketModel;

@RestController
@RequestMapping("/aws/s3")
public class AwsS3Controller {
	private static final Logger logger = LoggerFactory.getLogger(AwsS3Controller.class);

	private static final AWSCredentials credentials;

	@Value("aws_api_key")	
	private String awsKey;
	@Value("aws_secret_key")
	private String awsSkey;
	static {
		// put your accesskey and secretkey here
	    String awsAccessKey = "";//Need provide proper access key
	    String awsSecretkey = "";//Need provide proper secret key
		credentials = new BasicAWSCredentials(awsAccessKey, awsSecretkey);
	}

	AWSS3Service awsService = null;

	public AWSS3Service getClient(AWSCredentials credentials) {
		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
		awsService = new AWSS3Service(s3client);
		return awsService;
	}

	@GetMapping("/buckets")
	public ResponseEntity<List<String>> getBucketDetails() {
		List<String> bucketObjects = null;
		try {
			awsService = getClient(credentials); // to fetch the credentials
			bucketObjects = new ArrayList<>();
			for (Bucket bucket : awsService.listBuckets()) {
				bucketObjects.add(bucket.getName());
			}

			logger.info("bucketObjects:: "+ bucketObjects);

			if (bucketObjects.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(bucketObjects, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception:: ",e);
			return new ResponseEntity<>(bucketObjects, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/buckets/{bucketName}")
	public ResponseEntity<List<Map<String, String>>> getBucketDetailsByBucketName(
			@PathVariable(required = true) String bucketName) {

		List<Map<String, String>> bucketObjects = null;
		try {
			awsService = getClient(credentials); // to fetch the credentials
			bucketObjects = new ArrayList<>();
			ObjectListing objectListing = awsService.listObjects(bucketName);
			for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
				Map<String, String> object = new HashMap<>();
				object.put(os.getKey(), os.getBucketName());
				bucketObjects.add(object);
			}
			logger.info("bucketObjects::"+ bucketObjects);

			if (bucketObjects.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(bucketObjects, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception:: ",e);
			return new ResponseEntity<>(bucketObjects, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/buckets")
	public ResponseEntity<String> createBucket(@RequestBody BucketModel bucket) {
		try {
			awsService = getClient(credentials); // to fetch the credentials
			if (awsService.doesBucketExist(bucket.getBucketName())) {
				logger.info("***********Given bucket found, Please try different Bucket name************");
				return new ResponseEntity<>("Given bucket found, Please try different Bucket name",
						HttpStatus.BAD_REQUEST);
			}
			logger.info("***********Started bucket creation************");
			awsService = getClient(credentials);
			Bucket newBucket = awsService.createBucket(bucket.getBucketName());
			logger.info("newBucket: "+ newBucket);
			return new ResponseEntity<>("Bucket Created Successfully", HttpStatus.CREATED);
		} catch (IllegalArgumentException iae) {
			logger.error("IllegalArgumentException:" + iae);
			return new ResponseEntity<>("Bucket name should not contain valid characters", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Exception:" + e);
			return new ResponseEntity<>("Unable able to create bucket", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @PutMapping("/tutorials/{id}")
	// public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id,
	// @RequestBody Tutorial tutorial) {
	// Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

	// if (tutorialData.isPresent()) {
	// Tutorial _tutorial = tutorialData.get();
	// _tutorial.setTitle(tutorial.getTitle());
	// _tutorial.setDescription(tutorial.getDescription());
	// _tutorial.setPublished(tutorial.isPublished());
	// return new ResponseEntity<>(tutorialRepository.save(_tutorial),
	// HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// }
	// }

	@DeleteMapping("/buckets/{bucketName}")
	public ResponseEntity<String> deleteTutorial(@PathVariable("bucketName") String bucketName) {
		try {
			awsService = getClient(credentials); // to fetch the credentials
			awsService.deleteBucket(bucketName);
			logger.info("Given bucket successfully deleted");
			return new ResponseEntity<>("Given bucket successfully deleted", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception:: ",e);
			return new ResponseEntity<>("Given bucket not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @DeleteMapping("/tutorials")
	// public ResponseEntity<HttpStatus> deleteAllTutorials() {
	// try {
	// tutorialRepository.deleteAll();
	// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	// } catch (Exception e) {
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	// }

	// @GetMapping("/tutorials/published")
	// public ResponseEntity<List<Tutorial>> findByPublished() {
	// try {
	// List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

	// if (tutorials.isEmpty()) {
	// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	// }
	// return new ResponseEntity<>(tutorials, HttpStatus.OK);
	// } catch (Exception e) {
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

}
