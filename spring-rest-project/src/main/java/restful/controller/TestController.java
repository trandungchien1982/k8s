package restful.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private final Log log = LogFactory.getLog(getClass());

	public static List<String> listStr = new LinkedList<>();
	public static AtomicInteger counter = new AtomicInteger(10);
	static {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		for (int i = 1; i <= 10; i++) {
			listStr.add(String.format("Item " + i + " at time: " + curTime));
		}
	}

	@GetMapping("/envs")
	public String listEnvs() {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		log.info("Show list ENVs at time: " + curTime);

		StringBuilder builder = new StringBuilder();

		Map<String, String> env = System.getenv();

		builder.append("<br>Show list ENVs at time: " + curTime + " -- " + counter.getAndIncrement());
		builder.append("<br>");
		env.keySet().stream().sorted().collect(Collectors.toList()).forEach(key -> {
			String val = env.get(key);

			builder.append("<br>" + key + " = " + val);
		});

		return builder.toString();
	}

}
