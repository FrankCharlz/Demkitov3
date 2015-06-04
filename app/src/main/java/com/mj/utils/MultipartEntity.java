package com.mj.utils;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MultipartEntity implements HttpEntity {

    private String boundary = null;
    ProgressListener progressListener;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean isSetLast = false;
    boolean isSetFirst = false;

    public MultipartEntity() {
        this.boundary = System.currentTimeMillis() + "";
        Log.e("UPLOADING","Initialized entity");
    }
    
    public MultipartEntity(ProgressListener pl) {
    	this();
        progressListener = pl;      
    }

    public void writeFirstBoundaryIfNeeds(){
        Log.e("UPLOADING","Writing first boundary...");
        if(!isSetFirst){
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                
            }
        }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
    	Log.e("UPLOADING","Writing last boundary...");
        if(isSetLast){
            return ;
        }
        try {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        } catch (final IOException e) {

        }
        isSetLast = true;
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" +key+"\"\r\n").getBytes());
            out.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
            out.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {

        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin){
        addPart(key, fileName, fin, "application/octet-stream");
    }

    public static interface ProgressListener {
        void transferred(long num);
    }
    
    public void addPart(final String key, final String fileName, final InputStream fin, String type){
        writeFirstBoundaryIfNeeds();
        
        try {
            type = "Content-Type: "+type+"\r\n";
            out.write(("Content-Disposition: form-data; name=\""+ key+"\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            
        	Log.e("UPLOADING", "Started to write the output stream");
        	
            final byte[] buff = new byte[4096];
            int d = 0; 
            int count = 0;
            boolean checkinProgress = (progressListener != null);
            while ((d = fin.read(buff)) != -1) {
                out.write(buff, 0, d);
                if (checkinProgress)
                	progressListener.transferred(count++);
            }
            out.flush();
        } catch (final IOException e) {
        	Log.e("UPLOADING", "IO Exception"+e.getLocalizedMessage());
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
            	Log.e("UPLOADING", "Final IO Exception"+e.getLocalizedMessage());
            }
        }
    }

    public void addPart(final String key, final File value) {
        try {
            addPart(key, value.getName(), new FileInputStream(value));
        } catch (final FileNotFoundException e) {

        }
    }

    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
    	outstream.write(out.toByteArray());
    	
    }
    
    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public void consumeContent() throws IOException,
    UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
            "Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException,
    UnsupportedOperationException {
        return new ByteArrayInputStream(out.toByteArray());
    }

}
