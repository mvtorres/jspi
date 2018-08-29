package de.lohndirekt.print.attribute;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class AttributeParserTest extends TestCase{
	@Test
	public void testParseResponseInfiniteLoop() throws InterruptedException{
		final boolean threadFinished[] = new boolean[]{false};
		final Object wait = new Object();
		
		Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] badData = new byte[]{0,0,0};
					try {
						//this test wont finish if 
						AttributeParser.parseAttribute(new ByteArrayInputStream(badData), null);
					} catch (Throwable e) {
					}
					synchronized (wait) {
						threadFinished[0] = true;
						wait.notify();
					}
				}
			}
		);
		thread.start();
		synchronized (wait) {
			if(!threadFinished[0]){
				wait.wait(500);
			}
		}
		
		Assert.assertTrue(threadFinished[0]);
	}
}
