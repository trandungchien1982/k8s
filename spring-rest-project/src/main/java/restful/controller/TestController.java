package restful.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class TestController {

	@Value("${user.pwd}")
	String userPwd;

	@Value("${mongodb.pwd}")
	String mongoDBPwd;

	@Value("${mysql.pwd}")
	String mysqlPwd;

	private final Log log = LogFactory.getLog(getClass());

	public static List<String> listStr = new LinkedList<>();
	public static AtomicInteger counter = new AtomicInteger(10);
	static {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		for (int i = 1; i <= 10; i++) {
			listStr.add(String.format("Item " + i + " at time: " + curTime));
		}
	}

	@GetMapping("/use-secrets")
	public String listEnvs() {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		log.info("Show list ENVs at time: " + curTime);

		StringBuilder builder = new StringBuilder();

		Map<String, String> env = System.getenv();

		builder.append("<br><h2 style='color:blue; font-weight:bold'>Show list ENVs at time: " + curTime + " -- " + counter.getAndIncrement() + "</h2>");
		builder.append("<hr/>");
		builder.append("<br>");
		env.keySet().stream().sorted().collect(Collectors.toList()).forEach(key -> {
			String val = env.get(key);

			builder.append("<br>" + key + " = " + val);
		});

		// Kiểm tra nội dung 3 field định nghĩa sẵn
		builder.append("<br><br><h2>Các field định nghĩa sẵn có sử dụng Secret ...</h2>");
		builder.append("<hr/>");
		builder.append("<pre>");
		builder.append("\n - [USER_PASSWORD]    user.pwd:     " + userPwd);
		builder.append("\n - [MONGO_DB_PWD]     mongodb.pwd:  " + mongoDBPwd);
		builder.append("\n - [MYSQL_PWD]        mysql.pwd:    " + mysqlPwd);
		builder.append("<pre>");

		builder.append("<h2>Nội dung files lấy từ Secrets: </h2>");
		builder.append("<pre>");
		builder.append("\n--------------------------------------------------------------");
		builder.append("\n[1]Content of file `secret.txt`: " + getFileData("/app/storage/secret.txt"));

		builder.append("\n\n--------------------------------------------------------------");
		builder.append("\n[2]Content of file `server.crt`: " + getFileData("/app/storage/server.crt"));
		builder.append("\n --------------------------------------------------------------");

		builder.append("</pre>");

		return builder.toString();
	}

	private String getFileData(String path) {
		try {
			return FileUtils.readFileToString(new File(path), "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
