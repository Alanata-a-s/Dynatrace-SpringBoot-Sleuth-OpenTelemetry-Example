package ambit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Service
public class DemoService {

    @Autowired
    Tracer tracer;

    public MyData handleInService(MyData myData) {
        Span newSpan = this.tracer.nextSpan()
                .name("handleInService")
                .tag("myId", myData.getMyId());

        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
            log.info("Handling myData in service: {}", myData);

            newSpan.event("myDataInServiceHandled");

            return myData;
        } finally {
            newSpan.end();
        }
    }

    @NewSpan("helloInService")
    public MyData helloInService(@SpanTag("myId") String myId) {
        log.info("Handling hello in service: {}", myId);

        tracer.currentSpan().event("helloInServiceHandled");

        return MyData.builder()
                .myId(myId)
                .build();
    }
}
