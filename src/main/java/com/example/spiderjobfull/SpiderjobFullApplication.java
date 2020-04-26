package com.example.spiderjobfull;

import com.example.spiderjobfull.script.Script16880908com;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpiderjobFullApplication {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = SpringApplication.run(SpiderjobFullApplication.class, args);
        Script16880908com script16880908com = (Script16880908com) applicationContext.getBean(Script16880908com.class);
        script16880908com.run();
    }

}
