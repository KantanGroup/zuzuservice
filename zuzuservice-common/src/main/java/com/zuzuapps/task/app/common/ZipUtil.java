package com.zuzuapps.task.app.common;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author tuanta17
 */
public class ZipUtil {

    private int compressionMethod = Zip4jConstants.COMP_DEFLATE;
    private int compressionLevel = Zip4jConstants.DEFLATE_LEVEL_ULTRA;
    private int encryptionMethod = Zip4jConstants.ENC_METHOD_STANDARD;
    private int aesKeyStrength = Zip4jConstants.AES_STRENGTH_256;

    public ZipUtil() {
    }

    public ZipUtil(int encryptionMethod, int aesKeyStrength) {
        this.encryptionMethod = encryptionMethod;
        this.aesKeyStrength = aesKeyStrength;
    }

    public void zip(String input, String output, String password)
            throws ZipException, IOException {
        zip(input, output, password, "");
    }

    public void zip(String input, String output, String password, String fileNameCharset)
            throws ZipException, IOException {

        ZipFile zipFile = new ZipFile(output);
        // If the fileNameCharset is empty then charset is detected automatically
        // Try with Cp850 and UTF8 or OS default
        if (!fileNameCharset.isEmpty()) {
            zipFile.setFileNameCharset(fileNameCharset);
        }

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(compressionMethod);
        parameters.setCompressionLevel(compressionLevel);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(encryptionMethod);
        parameters.setAesKeyStrength(aesKeyStrength);
        parameters.setPassword(password);

        File inputFile = new File(input);
        if (inputFile.isDirectory()) {
            zipFile.createZipFileFromFolder(inputFile, parameters, false, 0);
        } else {
            zipFile.addFile(inputFile, parameters);
        }
    }

    public void unzip(String input, String output, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(input);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(output);
    }

    /**
     * GZip it
     *
     * @param input  GZip file location
     * @param output GZip file location
     */
    public void gzip(String input, String output) throws IOException {
        byte[] buffer = new byte[1024];
        GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(output));
        FileInputStream in = new FileInputStream(input);
        int len;
        while ((len = in.read(buffer)) > 0) {
            gzos.write(buffer, 0, len);
        }
        in.close();
        gzos.finish();
        gzos.close();
    }

    /**
     * GunZip it
     */
    public void gunzip(String input, String output) throws IOException {
        byte[] buffer = new byte[1024];
        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(input));
        FileOutputStream out = new FileOutputStream(output);
        int len;
        while ((len = gzis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        gzis.close();
        out.close();
    }
}

