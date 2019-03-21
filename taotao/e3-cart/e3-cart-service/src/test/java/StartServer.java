
import java.io.IOException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {
	
	@Test
	public void satrt() throws IOException{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("购物车服务开启");
		System.in.read();
		System.out.println("服务关闭");
	}
}
