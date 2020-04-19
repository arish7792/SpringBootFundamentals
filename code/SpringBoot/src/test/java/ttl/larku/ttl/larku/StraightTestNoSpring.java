package ttl.larku.ttl.larku;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import ttl.larku.controllers.rest.RestResult;
import ttl.larku.controllers.rest.StudentRestController;
import ttl.larku.domain.Student;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StraightTestNoSpring {

    @Autowired
    private StudentRestController studentRestController;

    @Test
    public void testCreateStudent() {
        UriComponentsBuilder uc = UriComponentsBuilder.fromPath("xyz");
        ResponseEntity<?> result = studentRestController.getStudent(2);
        RestResult rs = (RestResult)result.getBody();
        Student s = (Student)rs.getEntity();
        assertEquals(2, s.getId());

        studentRestController.createStudent(s, uc);
    }
}


