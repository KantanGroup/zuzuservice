package com.zuzuapps.task.app.common;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author tuanta17
 */
public class BufferedImageHttpMessageConverter implements HttpMessageConverter<BufferedImage> {
    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(new MediaType("image", "png"));
    }

    @Override
    public BufferedImage read(Class<? extends BufferedImage> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return ImageIO.read(httpInputMessage.getBody());
    }

    @Override
    public void write(BufferedImage bufferedImage, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
