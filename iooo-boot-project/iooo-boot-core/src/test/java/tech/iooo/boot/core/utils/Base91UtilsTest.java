package tech.iooo.boot.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created on 2018/8/2 下午5:43
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class Base91UtilsTest {

	@Test
	public void encode() {
		assertEquals(Base91Utils.encode("test"), "fPNKd");
		assertEquals(Base91Utils.encode("Hello World!"), ">OwJh>Io0Tv!8PE");
	}

	@Test
	public void decode() {
		assertEquals(Base91Utils.decode("fPNKd"), "test");
		assertEquals(Base91Utils.decode(">OwJh>Io0Tv!8PE"), "Hello World!");
	}
}
