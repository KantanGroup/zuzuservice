package com.zuzuapps.task.app.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author tuanta17
 */
public class GZipUtil {

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

