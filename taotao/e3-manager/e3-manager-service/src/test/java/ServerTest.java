import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class ServerTest {
	@Test	
	public void Start() throws IOException{		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
	System.out.println("管理服务开启");
		System.in.read();
		System.out.println("服务关闭");
	}
}
