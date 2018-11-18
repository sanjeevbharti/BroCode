package com.epam.hackathon.file;

import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Service
class LocalFilesystemFileService implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFilesystemFileService.class);
    private static final List<String> imageFileExtension = Collections.unmodifiableList(asList("jpeg", "gif"));

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private final Path foundDir;
    private final Path lostDir;

    LocalFilesystemFileService(@Value("${found.dir}") final String foundDir,
                               @Value("${lost.dir}") final String lostDir) {
        this.foundDir = Paths.get(foundDir);
        this.lostDir = Paths.get(lostDir);
    }

    @Override
    public String saveFound(final String filename, final byte[] data) throws IOException {
        return save(filename, data, foundDir);
    }
    @Override
    public String saveLost(final String filename, final byte[] data) throws IOException {
        return save(filename, data, lostDir);
    }

    private static String save(final String originalFilename, final byte[] data, final Path dir) throws IOException {
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String timeString = LocalDateTime.now().format(formatter);
        String filename = timeString + "." + fileExtension;

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        Files.write(dir.resolve(filename), data);

        String mp4FilePath = dir.toAbsolutePath() + "/" + filename;

        if (fileExtension.equals("mp4")) {
            try {
                convertVideotoJPG(mp4FilePath, dir.toString(), "jpg",11);
                Files.delete(Paths.get(mp4FilePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filename;
    }


    private static void convertVideotoJPG(String mp4Path, String imagePath, String imgType, int secJump) throws Exception, IOException
    {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(mp4Path);
        frameGrabber.start();
        Frame frame;
        double frameRate=frameGrabber.getFrameRate();
        int imgNum=0;
        int frameLength = frameGrabber.getLengthInFrames();
        System.out.println("Video has "+frameLength+" frames and has frame rate of "+frameRate);

        int noOfFramePerSec = frameGrabber.getLengthInFrames() / (int)frameRate;

        System.out.println("noOfFramePerSec : "+noOfFramePerSec);

        int frameJump = (int)frameRate * secJump;

        System.out.println("secJump : " + secJump);

        try {
            for(int ii=1;ii<=frameLength;ii++){
                imgNum++;
                frameGrabber.setFrameNumber(ii);
                frame = frameGrabber.grab();
                BufferedImage bi = converter.convert(frame);
                String timeString = LocalDateTime.now().format(formatter);
                String filename = timeString + "." + imgType;
                String path = imagePath+ File.separator+filename;
                System.out.println(ii + " " +frameLength);
                if(ii > 1) {
                    ImageIO.write(bi, imgType, new File(path));
                }
                ii+=frameJump;
            }
            frameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
