package com.example.demo.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Profile("demo")
@RequiredArgsConstructor
class DemoDataInitializer {

    private final List<DemoInitializer> demoInitializers;

    @PostConstruct
    void initDemoData() {
        demoInitializers.forEach(DemoInitializer::init);
    }
}
