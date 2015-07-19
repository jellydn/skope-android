/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

package com.speakgeo.skopebeta.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpFileUploadUtil {
	private URL mConnectURL;
	private final ArrayList<Data> mData;

	public HttpFileUploadUtil(String url) {
		try {
			mConnectURL = new URL(url);
		} catch (Exception ex) {

		}
		this.mData = new ArrayList<Data>();
	}

	public void addData(Data item) {
		mData.add(item);
	}

	public void addData(String name, Object value) {
		Data temp = new Data(name, value, null, null);
		mData.add(temp);
	}

	public void addData(String name, Object value, String filename, String fileType) {
		Data temp = new Data(name, value, filename, fileType);
		mData.add(temp);
	}

	public String doUpload(Context context) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) mConnectURL.openConnection();
			conn.setReadTimeout(UserProfileSingleton.TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			//
			dos = new DataOutputStream(conn.getOutputStream());

			for (Data item : mData) {
				if (item.getValue() instanceof Uri) {
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ item.getName() + "\";filename=\""
							+ item.getFilename() + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: "+item.getFileType() + lineEnd);
					dos.writeBytes(lineEnd);

                    InputStream is;
                    Uri uri = (Uri)item.getValue();
                    if(uri.toString().contains("content")) {
                        ContentResolver cr = context.getContentResolver();
                        is = cr.openInputStream(uri);
                    }
                    else {
                        is = new FileInputStream(uri.getPath());
                    }

                    byte[] buf = new byte[1024*1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        dos.write(buf, 0, len);
                    }
                    is.close();

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				} else {
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ item.getName() + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(item.getValue()));
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}
			}

			mData.clear();
			// close streams
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			return ex.getMessage();
		}

		catch (IOException ioe) {
			return ioe.getMessage();
		}
		try {
			inStream = new DataInputStream(conn.getInputStream());
			StringBuffer b = new StringBuffer();
			String line = null;
			while ((line = inStream.readLine()) != null) {
				b.append(line);
			}
			inStream.close();
			return b.toString();
		} catch (IOException ioex) {
			return ioex.getMessage();
		}
	}

	// -----------------
	public class Data {
		private String mName;
		private Object mValue;
		private String mFilename;
        private String mFileType;

		public Data(String name, Object value, String filename, String fileType) {
			mName = name;
			mValue = value;
			mFilename = filename;
            mFileType = fileType;
		}

		public String getName() {
			return mName;
		}

		public void setName(String mName) {
			this.mName = mName;
		}

		public Object getValue() {
			return mValue;
		}

		public void setValue(Object mValue) {
			this.mValue = mValue;
		}

		public String getFilename() {
			return mFilename;
		}

		public void setFilename(String mFilename) {
			this.mFilename = mFilename;
		}

        public String getFileType() {
            return mFileType;
        }

        public void setFileType(String mFileType) {
            this.mFileType = mFileType;
        }
    }
}