package restful.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class BookSubController02 {

	private final Log log = LogFactory.getLog(getClass());

	public static AtomicInteger counter = new AtomicInteger(10);

	@GetMapping("/subSystem2/{systemId}")
	public List<String> getSubSystem2(@PathVariable String systemId) {
		String msg022 = "        <br> ==> Service-II - SubSystem2 :: getSubSystem2() with systemId: " + systemId + " -- " + counter.getAndIncrement();
		msg022 += "<br><span style='padding-left:70px; color:maroon;'>Service-II - SubSystem2: " + serverInfo() + "</span>";
		log.info(msg022);

		List<String> retList = new LinkedList<>();
		retList.add(msg022);
		for (int i = 1; i <= 3; i++) {
			retList.add("<br><span style='padding-left:90px;'>............... getSubSystem2() at position: " + counter.get() + i + "</span>");
		}

		return retList;
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
