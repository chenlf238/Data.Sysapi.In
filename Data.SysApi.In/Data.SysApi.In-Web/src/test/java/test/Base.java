package test;

import data.Application;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*
*
clean install -Dmaven.test.skip=true

*
* */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Base {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
}
