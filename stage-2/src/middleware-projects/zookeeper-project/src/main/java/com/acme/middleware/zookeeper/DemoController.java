package com.acme.middleware.zookeeper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-04-17
 * @version 1.0.0
 */
@RestController
public class DemoController {

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "message")
    public String message(String message) {
        return message;
    }

}
