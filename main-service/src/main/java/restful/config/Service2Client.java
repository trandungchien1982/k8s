package restful.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Service2Client", url = "${service02.url}")
public interface Service2Client {

    /**
     * Lấy Book Name dựa vào bookId
     * @return
     */
    @GetMapping("/bookName/{bookId}")
    public String getBookName(@PathVariable String bookId);

    /**
     * Lấy Book Publisher dựa vào bookId
     * @return
     */
    @GetMapping("/bookPublisher/{bookId}")
    public String getBookPublisher(@PathVariable String bookId);

    /**
     * Lấy thông tin SubSystem
     * @return
     */
    @GetMapping("/subSystem/{systemId}")
    public String getSubSystem(@PathVariable String systemId);
}
