package restful.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Service21Client", url = "${service21.url}")
public interface Service21Client {

    @GetMapping("/subSystem1/{systemId}")
    public List<String> getSubSystem1(@PathVariable String systemId);

}
