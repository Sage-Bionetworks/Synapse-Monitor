package org.sagebionetworks.web.server.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.amazonaws.util.StringInputStream;

public class DataUtilsTest {
	
	@Test
	public void testReadStringFromStream() throws IOException{
		char[] chars = new char[2333];
		Arrays.fill(chars, 'o');
		chars[0] = 'g';
		chars[chars.length-1] = 'd';
		String bigString = new String(chars);
		System.out.println(bigString);
		ByteArrayInputStream in = new ByteArrayInputStream(bigString.getBytes("UTF-8"));
		// Make sure we can read from this stream
		String result = DataUtils.readStringFromStream(in);
		assertNotNull(result);
		assertEquals(bigString.length(), result.length());
		assertEquals(bigString, result);
	}

	@Test
	public void testReadStringSmall() throws IOException{
		String small = "a";
		ByteArrayInputStream in = new ByteArrayInputStream(small.getBytes("UTF-8"));
		// Make sure we can read from this stream
		String result = DataUtils.readStringFromStream(in);
		assertNotNull(result);
		assertEquals(small, result);
	}
}
