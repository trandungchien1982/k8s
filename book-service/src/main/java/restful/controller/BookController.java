package restful.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import restful.config.Service21Client;
import restful.config.Service22Client;
import restful.config.Service23Client;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class BookController {

	private final Log log = LogFactory.getLog(getClass());

	public static AtomicInteger counter = new AtomicInteger(10);

	@Autowired
	Service21Client service21Client;

	@Autowired
	Service22Client service22Client;

	@Autowired
	Service23Client service23Client;


	@GetMapping("/bookName/{bookId}")
	public String getBookName(@PathVariable String bookId) {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg02 = "    ==> BookService :: getBookName() with bookID: " + bookId + ", time: " + curTime + " -- " + counter.getAndIncrement();
		msg02 += "<br><span style='padding-left:50px; color:green;'>BookService-ServerInfo: " + serverInfo() + "</span>";
		log.info(msg02);

		return msg02;
	}

	@GetMapping("/bookPublisher/{bookId}")
	public String getBookPublisher(@PathVariable String bookId) {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg02 = "    ==> BookService :: getBookPublisher() with bookID: " + bookId + ", time: " + curTime + " -- " + counter.getAndIncrement();
		msg02 += "<br><span style='padding-left:50px; color:magenta;'>BookService-ServerInfo: " + serverInfo() + "</span>";
		log.info(msg02);

		return msg02;
	}

	@GetMapping("/subSystem/{systemId}")
	public String getSubSystem(@PathVariable String systemId) {
		String curTime = DateFormatUtils.format(new Date(), "HH:mm:ss");
		String msg02 = "    ==> BookService :: getSubSystem() with systemId: " + systemId + ", time: " + curTime + " -- " + counter.getAndIncrement();
		msg02 += "<br><span style='padding-left:50px; color:teal;'>BookService-ServerInfo: " + serverInfo() + "</span>";
		final StringBuilder builder = new StringBuilder();
		builder.append(msg02);
		log.info(msg02);

		List<String> sub1 = service21Client.getSubSystem1(systemId);
		List<String> sub2 = service22Client.getSubSystem2(systemId);
		List<String> sub3 = service23Client.getSubSystem3(systemId);

		sub1.forEach(str -> builder.append(str));
		sub2.forEach(str -> builder.append(str));
		sub3.forEach(str -> builder.append(str));

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
