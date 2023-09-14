package restful.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class TestController {

	@Value("${userdb.url}")
	String userDbUrl;

	@Value("${username.gmail}")
	String userNameGmail;

	@Value("${mongodb.url}")
	String mongoDbUrl;

	private final Log log = LogFactory.getLog(getClass());

	public static List<String> listStr = new LinkedList<>();
	public static AtomicInteger counter = new AtomicInteger(10);
	static {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		for (int i = 1; i <= 10; i++) {
			listStr.add(String.format("Item " + i + " at time: " + curTime));
		}
	}

	@GetMapping("/use-config-map")
	public String listEnvs() {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		log.info("Show list ENVs at time: " + curTime);

		StringBuilder builder = new StringBuilder();

		Map<String, String> env = System.getenv();

		builder.append("<br><h2 style='color:red; font-weight:bold'>Show list ENVs at time: " + curTime + " -- " + counter.getAndIncrement() + "</h2>");
		builder.append("<hr/>");
		builder.append("<br>");
		env.keySet().stream().sorted().collect(Collectors.toList()).forEach(key -> {
			String val = env.get(key);

			builder.append("<br>" + key + " = " + val);
		});

		// Kiểm tra nội dung 3 field định nghĩa sẵn
		builder.append("<br><br><h2>Các field định nghĩa sẵn ...</h2>");
		builder.append("<hr/>");
		builder.append("<pre>");
		builder.append("\n - [USER_DB_URL]    userdb.url:     " + userDbUrl);
		builder.append("\n - [USERNAME_GMAIL] username.gmail: " + userNameGmail);
		builder.append("\n - [MONGODB_URL]    mongodb.url:    " + mongoDbUrl);
		builder.append("</pre>");

		return builder.toString();
	}

}
