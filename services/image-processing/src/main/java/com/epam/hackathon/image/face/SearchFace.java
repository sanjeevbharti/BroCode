package com.epam.hackathon.image.face;

import com.epam.hackathon.data.ImageRepositoryImpl;
import com.epam.hackathon.domain.FoundImage;
import com.epam.hackathon.domain.LostImage;
import com.epam.hackathon.image.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/***
 * This class is to search for a given face in the images already present in a particular repository.
 *
 */
@Component
public class SearchFace {
	private static final Logger logger = Logger.getLogger(SearchFace.class.getName());

	private static int totalAwsCalls;
	
	@Autowired
	private ImageRepositoryImpl imageRepository;

	@Autowired
	private CompareFaces compareFaces;
	private String folderLocationToSearch;

	@Autowired
	public SearchFace(@Value("${found.dir}") String folderLocationToSearch) {

		this.folderLocationToSearch=folderLocationToSearch;
	}

	/***
	 * This method run the search of face from source image in all the target images in a given repository.
	 * @param lostImage image path of the lost person image.
	 * @return A map of image with corresponding similarity confidence value.
	 */
	public LostImage run(String lostImage) {

		logger.info("=============>Going to search for lost image '"+lostImage+"' in all the images present at '"+folderLocationToSearch+"'<==============");

		List<String> targetImagesInFileSystem=FileUtil.getAllImagesFromFolder(folderLocationToSearch);

		logger.info("======Total of "+targetImagesInFileSystem.size()+" images found in our target repository : '"+folderLocationToSearch+"'");

		String lostImageFileNameWithoutExtension=FileUtil.getFileNameWithoutExtensionFromFilePath(lostImage);
		Long lostImageId=Long.parseLong(lostImageFileNameWithoutExtension);
		
		logger.info("======Checking number of target images already checked before for lost image id : "+lostImageId);
		
		LostImage theLostImageDBObject=imageRepository.getLostImageById(lostImageId);
		
		logger.info("======== theLostImageDBObject : "+theLostImageDBObject);
		
		List<FoundImage> foundImagesFromDB=new ArrayList<>();

		//If lost image is in DB then we need to check and see which found images are already processed.
		if(theLostImageDBObject!=null) {
			foundImagesFromDB=theLostImageDBObject.getFoundImages();
			
			logger.info("------------------ "+foundImagesFromDB.size()+" found images found in DB for lost image id : "+lostImageId);
			
			for(FoundImage foundImage:foundImagesFromDB) {
				if(targetImagesInFileSystem.contains("../ui/"+foundImage.getFoundImagePath())) {
					targetImagesInFileSystem.remove("../ui/"+foundImage.getFoundImagePath());
				}
			}

			logger.info("===== Total of "+targetImagesInFileSystem.size()+" images need to be checked and processed for matching in our target repository : "+folderLocationToSearch+"'");

		}else {
			theLostImageDBObject=new LostImage();
			theLostImageDBObject.setLostImageId(lostImageId);
			theLostImageDBObject.setLostImagePath(lostImage.substring(lostImage.indexOf("localStorage")));
		}

		theLostImageDBObject=populateLostImageObject(lostImage, targetImagesInFileSystem, lostImageId, theLostImageDBObject,
				foundImagesFromDB);

		return theLostImageDBObject;
	}

	/**
	 * @param lostImage
	 * @param targetImagesInFileSystem
	 * @param lostImageId
	 * @param theLostImageDBObject
	 * @param foundImagesFromDB
	 */
	private LostImage populateLostImageObject(String lostImage, List<String> targetImagesInFileSystem, Long lostImageId,
			LostImage theLostImageDBObject, List<FoundImage> foundImagesFromDB) {
		if(targetImagesInFileSystem!=null&&targetImagesInFileSystem.size()>0) {
			
			//Delete existing record from DB if present
			logger.info("======== Lost Image record for lost image id '"+lostImageId+"' will be deleted if it exists.");
			imageRepository.delete(lostImageId);
			
			
			for(String targetImage: targetImagesInFileSystem) {

				Float similarityConfidence=compareFaces.run(lostImage, targetImage);
				if(similarityConfidence!=null) {
					totalAwsCalls++;
				}

				//Create FoundImage object
				FoundImage foundImage=new FoundImage();
				String foundImageFileNameWithoutExtension=FileUtil.getFileNameWithoutExtensionFromFilePath(targetImage);
				Long foundImageId=Long.parseLong(foundImageFileNameWithoutExtension);
				foundImage.setFoundImageId(foundImageId);
				foundImage.setFoundImagePath(targetImage.substring(targetImage.indexOf("localStorage")));
				foundImage.setMatchPercentage(similarityConfidence.doubleValue());

				foundImagesFromDB.add(foundImage);
			}

			theLostImageDBObject.setFoundImages(foundImagesFromDB);
		}else {
			return null;
		}
		
		return theLostImageDBObject;
	}

	public static int getTotalAwsCalls() {
		return totalAwsCalls;
	}
}
