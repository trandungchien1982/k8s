package restful.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.config.Service1Client;
import restful.config.Service2Client;

import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MainController {

	private final Log log = LogFactory.getLog(getClass());

	public static List<String> listStr = new LinkedList<>();
	public static AtomicInteger counter = new AtomicInteger(10);
	static {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		for (int i = 1; i <= 10; i++) {
			listStr.add(String.format("Item " + i + " at time: " + curTime));
		}
	}

	@Autowired
	private Service1Client service01Client;

	@Autowired
	private Service2Client service02Client;

	@Value("${service01.url}")
	private String service01Url;

	/**
	 * Hiển thị thông tin chính, bao gồm:
	 * 	+ Thông tin tự tạo trong Function này
	 * 	+ Thông tin lấy từ 2 Services con trong Cluster
	 * @return
	 */
	@GetMapping("/main-info")
	public String mainInfo() {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg = "<h2 style='color:red; border: 1px solid yellow; padding: 10px; margin: 0px; margin-top:10px;'>Show MAIN information at time: " + curTime + " -- " + counter.getAndIncrement() + "</h2>";
		log.info(msg);

		log.info("service01Url: " + service01Url);

		StringBuilder builder = new StringBuilder();

		Map<String, String> env = System.getenv();

		builder.append(msg);
		String k8sHost = env.get("KUBERNETES_SERVICE_HOST");
		String curHost = env.get("HOSTNAME");

		log.info("k8sHost: " + k8sHost);
		log.info("curHost: " + curHost);
		builder.append("<br>k8sHost: " + k8sHost);
		builder.append("<br>curHost: " + curHost);

		String mInfo = " >> MAIN Info at position: " + counter.get();
		mInfo += " >> MainService-ServerInfo: " + serverInfo();
		log.info(mInfo);
		builder.append("<br>" + mInfo);

		// Info from Service1
		String userID = "userID1";
		String s1Info = service01Client.getUserName(userID) + "<br/>"
				+ service01Client.getUserAddress(userID);
		builder.append("<br>" + s1Info);

		String bookID = "bookID2";
		String s2Info = "<br/>" + service02Client.getBookName(bookID)
				+ "<br/>"
				+ service02Client.getBookPublisher(bookID);
		builder.append("<br>" + s2Info);

		String subSystemID = "subSystemID";
		String s2Sub = "<br/>" + service02Client.getSubSystem(subSystemID);
		builder.append("<br>" + s2Sub);

		return builder.toString();
	}

	private String serverInfo() {
		String resultInfo = "[]";
		try {
			InetAddress inetadd = InetAddress.getLocalHost();
			String name = inetadd.getHostName();
			String address = inetadd.getHostAddress();
			resultInfo = " >> HostName: " + name + ", IP Address: " + address;
		}
		catch(Exception u){
			log.error("Exception : " + u.getMessage());
		}

		return resultInfo;
	}

}
