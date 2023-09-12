package restful.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Service1Client", url = "${service01.url}")
public interface Service1Client {

    /**
     * Lấy user Name dựa vào userId
     * @return
     */
    @GetMapping("/userName/{userId}")
    public String getUserName(@PathVariable String userId);

    /**
     * Lấy user Address dựa vào userId
     * @return
     */
    @GetMapping("/userAddress/{userId}")
    public String getUserAddress(@PathVariable String userId);
}
