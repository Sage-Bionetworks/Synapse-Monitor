package org.sagebionetworks.web.server.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data utils
 * @author John
 *
 */
public class DataUtils {
	
	/**
	 * Extract a string from the given input stream
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	public static String readStringFromStream(InputStream in) throws IOException{
		if(in == null) throw new IllegalArgumentException("Inputstream cannot be null");
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = in.read(buffer)) > 0){
				baos.write(buffer, 0, length);
			}
			return new String(baos.toByteArray(), "UTF-8");
		}finally{
			in.close();
		}
	}

}
