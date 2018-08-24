package tech.iooo.boot.example.service;

import org.springframework.stereotype.Service;

/**
 * Created on 2018/8/24 下午2:51
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Service
public class Greeter {

	public String sayHello(String name) {
		return "Hello " + name;
	}
}
