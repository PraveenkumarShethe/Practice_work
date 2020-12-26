package com.example.URLShortningApp;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * Created by Praveenkumar on 12/21/2020.
 */
@RestController("rest/url")
public class URLShortnerController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/{id}")
    public String getURL(@PathVariable String id){

        return stringRedisTemplate.opsForValue().get(id);
    }

    @PostMapping
    public String create(@RequestBody String url){
        String id = "";
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http","https"}
        );
        if (urlValidator.isValid(url)){
            id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            stringRedisTemplate.opsForValue().set(id, url);
            return id;
        }
        throw new RuntimeException("Url invalid : " +url);
    }

}
