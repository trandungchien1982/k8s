package restful.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Service22Client", url = "${service22.url}")
public interface Service22Client {

    @GetMapping("/subSystem2/{systemId}")
    public List<String> getSubSystem2(@PathVariable String systemId);

}
