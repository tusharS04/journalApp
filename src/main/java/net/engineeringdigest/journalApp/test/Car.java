package net.engineeringdigest.journalApp.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Car {

    @Autowired //dependency injection - no need to create new object, stores this in ioc container
    public Dog dog;

    @GetMapping("/ok")
    public String ok() {
        return dog.fun();
    }
}
