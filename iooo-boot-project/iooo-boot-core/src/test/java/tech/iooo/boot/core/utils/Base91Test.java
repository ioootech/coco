package tech.iooo.boot.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created on 2018/8/2 下午5:43
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class Base91Test {

	@Test
	public void encode() {
		assertEquals(Base91.encode("test"), "fPNKd");
		assertEquals(Base91.encode("Hello World!"), ">OwJh>Io0Tv!8PE");
	}

	@Test
	public void decode() {
		assertEquals(Base91.decode("fPNKd"), "test");
		assertEquals(Base91.decode(">OwJh>Io0Tv!8PE"), "Hello World!");
	}
}
