package data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import witparking.springbootConfig.WitParkingBanner;

@ComponentScan(basePackages = {"data"})
//由于依赖了mongodb驱动,官方默认会加载该策略
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBanner(new WitParkingBanner());
        app.run(args);
        System.out.println("启动成功了");
    }
}
