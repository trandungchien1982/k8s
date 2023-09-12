package restful.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class UserController {

	private final Log log = LogFactory.getLog(getClass());

	public static AtomicInteger counter = new AtomicInteger(10);

	@GetMapping("/userName/{userId}")
	public String getUserName(@PathVariable String userId) {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg01 = "    ==> UserService :: getUserName() with userId: " + userId + ", time: " + curTime + " -- " + counter.getAndIncrement();
		msg01 += "<br><span style='padding-left:30px; color:red;'>UserService: ServerInfo: " + serverInfo() + "</span>";
		log.info(msg01);

		return msg01;
	}

	@GetMapping("/userAddress/{userId}")
	public String getUserAddress(@PathVariable String userId) {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg01 = "    ==> UserService :: getUserAddress() with userId: " + userId + ", time: " + curTime + " -- " + counter.getAndIncrement();
		msg01 += "<br><span style='padding-left:30px; color:blue;'>UserService: ServerInfo: " + serverInfo() + "</span>";
		log.info(msg01);

		return msg01;
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
