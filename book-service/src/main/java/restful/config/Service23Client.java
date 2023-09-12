package restful.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Service23Client", url = "${service23.url}")
public interface Service23Client {

    @GetMapping("/subSystem3/{systemId}")
    public List<String> getSubSystem3(@PathVariable String systemId);

}
