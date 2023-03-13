package ambit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Boot2SleuthOtelApplicationTests {


    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPost() {
        ResponseEntity<MyData> response = restTemplate.postForEntity("http://localhost:" + port + "/",
                MyData.builder().myId("my-id").build(),
                MyData.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(MyData.builder().myId("my-id").build()));
    }

    @Test
    void testHandle() {
        ResponseEntity<MyData> response = restTemplate.postForEntity("http://localhost:" + port + "/handle",
                MyData.builder().myId("my-id").build(),
                MyData.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(MyData.builder().myId("my-id").build()));
    }

    @Test
    void testHello() {
        ResponseEntity<MyData> response = restTemplate.getForEntity("http://localhost:" + port + "/hello/my-id",
                MyData.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(MyData.builder().myId("my-id").build()));
    }
}
