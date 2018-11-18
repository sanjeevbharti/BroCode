package com.epam.hackathon.image.face;

import com.epam.hackathon.data.ImageRepositoryImpl;
import com.epam.hackathon.domain.LostImage;
import com.epam.hackathon.image.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@PropertySource("classpath:application.properties")
@PropertySource("classpath:data.properties")
@Component
public class MatchingFaces {
	
	private static Logger logger=Logger.getLogger(MatchingFaces.class.getName());
	
	@Autowired
	private ImageRepositoryImpl imageRepository;
	
	@Autowired
	private SearchFace searchFace;
	
	@Value("${found.dir}")
	private String foundFolderLocation;
	
	@Value("${lost.dir}")
	private String lostFolderLocation;
	
	public void start() {
		long startTime=System.currentTimeMillis();
		logger.info("==================================== IMAGE MATCHING BATCH JOB STARTING ==================================");
		
		logger.info(">>>>> Lost Image Folder : "+lostFolderLocation);
		logger.info(">>>>> Found Image Folder : "+foundFolderLocation);
		
		
		List<LostImage> allLostImageObjects=new ArrayList<>();
		List<String> allLostImages=FileUtil.getAllImagesFromFolder(lostFolderLocation);
		for(String lostImage:allLostImages) {
			
			LostImage lostImageObject=searchFace.run(lostImage);
			
			if(lostImageObject!=null) {
				allLostImageObjects.add(lostImageObject);
			}
		}
		
		saveInDB(allLostImageObjects);
		
		int totalAwsApiCall=searchFace.getTotalAwsCalls();
		
		logger.info("----------> TOTAL AWS API CALLS : "+totalAwsApiCall+" <-------------");
		long endTime=System.currentTimeMillis();
		logger.info("BATCH JOB TOOK "+(endTime-startTime)/1000+" SECS TO FINISH");
		logger.info("=============================================== BATCH JOB FINISHED =====================================");
	}

	/**
	 * @param allLostImageObjects
	 */
	private void saveInDB(List<LostImage> allLostImageObjects) {
		//DB Insert
		if(allLostImageObjects.size()>0) {
			logger.info("===== Total of >>"+allLostImageObjects.size()+"<< LostImage records inserted/updated in database");
			imageRepository.insert(allLostImageObjects);
		}
	}
}
