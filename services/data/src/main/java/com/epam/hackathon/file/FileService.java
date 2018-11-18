package com.epam.hackathon.file;

import java.io.IOException;

public interface FileService {
    /**
     * Return filename of saved file.
     */
    String saveFound(String filename, byte[] data) throws IOException;

    /**
     * Return filename of saved file.
     */
    String saveLost(String filename, byte[] data) throws IOException;

}
