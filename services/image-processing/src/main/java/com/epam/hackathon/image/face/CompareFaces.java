package com.epam.hackathon.image.face;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.epam.hackathon.image.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

/***
 * This class have methods that will take face from source image and will check if similar face is present in target image or not.
 * There is a similarity threshold value that will be used to decide whether the face matches or not.
 *
 */
@Component
public class CompareFaces {

	private static final Logger logger = Logger.getLogger(CompareFaces.class.getName());

	@Value("${mock.aws.api.call}")
	private String mockAwsApiCall;

	private final AmazonRekognition awsClient;

	@Autowired
	public CompareFaces(final AmazonRekognition awsClient) {
		this.awsClient = awsClient;
	}

	/***
	 * This method call the AWS api to compare face from source image in target image with the given similrity confidence value.
	 * @return
	 */
	public Float run(String sourceImage, String targetImage) {
		ByteBuffer source = FileUtil.loadImage(sourceImage);
		ByteBuffer target = FileUtil.loadImage(targetImage);

		if (source == null || target == null) {
			logger.warning("None of the image (source/target) should be null. Source Image : " + sourceImage + " Target Image : " + targetImage);
			return null;
		}

		if(mockAwsApiCall!=null && mockAwsApiCall.equalsIgnoreCase("true")) {
			logger.info("======= MOCKING AWS API to call for comparing Source Image : "+sourceImage+" with target image : "+targetImage);
			return 50f;
		}

		logger.info("======= AWS API to call for comparing Source Image : "+sourceImage+" with target image : "+targetImage);

		CompareFacesRequest request = new CompareFacesRequest().withSourceImage(new Image().withBytes(source)).withTargetImage(new Image().withBytes(target))
				.withSimilarityThreshold(0f);

		CompareFacesResult result = awsClient.compareFaces(request);
		List<CompareFacesMatch> matchedFaces = result.getFaceMatches();


		Float similarityConfidence=0f;
		if(matchedFaces!=null&&matchedFaces.size()>0) {  

			//Matching found
			logger.info("============>"+matchedFaces.size()+" MATCHING FACE FOUND IN "+targetImage+"<=============");

			//Show matching faces with their similarity confidence value
			
			for(CompareFacesMatch faceMatch:matchedFaces) {

				logger.info("====>Similarity confidence : "+faceMatch.getSimilarity()+"<=====");

				//if more than 1 face matching with input is found then we will keep the similarityConfidence of highest value.
				if(faceMatch.getSimilarity()>similarityConfidence) {
					similarityConfidence=faceMatch.getSimilarity();
				}
			}

			logger.info("----------------------------------------------------");
		}else { 

			//Matching not found
			logger.info("xxxxxxxxxxxxxxxxxxxxx MATCH NOT FOUND in '"+targetImage+"' xxxxxxxxxxxxxxxxxxxxxxx");
			logger.info("----------------------------------------------------");
		}



		return similarityConfidence;
	}
}
