package com.nero.elasticsearch.elasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElasticsearchDemoApplication.class)
public class UserQueryTest {

    @Autowired
    private UserQuery userQuery;

    @Test
    public void findByAge() throws Exception {

        Integer age = 40;
        User user = userQuery.findByAge(age);
        System.out.println(user.toString());
    }

    @Test
    public void findByAgeAndHid() throws Exception {

        int age = 40;
        List<Long> hidList = Arrays.asList(12L, 13L);

        List<User> userList = userQuery.findByAgeAndHid(age, hidList);
        for(User user : userList){
            System.out.println(user.toString());
        }
    }

    @Test
    public void findByDesc() throws Exception {

        String desc = "好结交朋友，以及时雨而天下闻名";
        List<User> userList = userQuery.findByDesc(desc);
        for(User user : userList){
            System.out.println(user.toString());
        }
    }

}