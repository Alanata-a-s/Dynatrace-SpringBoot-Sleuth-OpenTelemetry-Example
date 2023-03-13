package ambit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class DemoController {

    @Autowired
    Tracer tracer;

    @Autowired
    DemoService demoService;

    // span pre POST na controllery vytvori sleuth pomocou OTEL
    // tomcat sleuth instrumentacia je vypnuta (spring.sleuth.web.tomcat.enabled=false)
    @PostMapping(path = "/")
    public @ResponseBody MyData root(@RequestBody MyData myData) {
        // Pre DYNATRACE nastavit tag na zaciatku spanu!
        Span newSpan = this.tracer.nextSpan()
                .name("handle")
                .tag("myId", myData.getMyId());

        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
            log.info("Handling myData: {}", myData);

            newSpan.event("myDataHandled");

            return myData;
        } finally {
            newSpan.end();
        }
    }

    // span pre POST na controllery vytvori sleuth pomocou OTEL
    // tomcat sleuth instrumentacia je vypnuta (spring.sleuth.web.tomcat.enabled=false)
    @PostMapping(path = "/handle")
    public @ResponseBody MyData handle(@RequestBody MyData myData) {
       log.info("Handling myData: {}", myData);
        return demoService.handleInService(myData);
    }

    // span pre GET na controllery vytvori sleuth pomocou OTEL
    // tomcat sleuth instrumentacia je vypnuta (spring.sleuth.web.tomcat.enabled=false)
    @GetMapping(path = "/hello/{myId}")
    public @ResponseBody MyData hello(@PathVariable(value = "myId") String myId) {
        log.info("Handling hello: {}", myId);
        // tag je zadany v spane vytvorenom v service
        return demoService.helloInService(myId);
    }
}

