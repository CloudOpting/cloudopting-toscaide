package eu.cloudopting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloudopting.CloudoptingToscaideApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CloudoptingToscaideApplication.class)
@WebAppConfiguration
public class CloudoptingTiscaideApplicationTests {

	@Test
	public void contextLoads() {
	}

}
