package net.engineeringdigest.journalApp.test;

import org.springframework.stereotype.Component;

@Component //stores the instance of class in ioc container (stores all the bean)
public class Dog {

    public String fun() {
        return "Something";
    }
}
