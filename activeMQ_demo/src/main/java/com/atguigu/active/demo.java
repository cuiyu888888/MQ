package com.atguigu.active;/**
 * @Author 崔雨
 * @Date 2019/11/15 16:35
 * @Description: ${todo}
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @program: mq
 *
 * @description:
 *
 * @author: Mr.Wang
 *
 * @create: 2019-11-15 16:35
 **/
public class demo {

    public static void main(String[] args) {

        List<String> s = new ArrayList<>();
        s.add("a");
        System.out.println("1");
        for (String ss : s){
            System.out.println(ss);
            s.add("b");
        }

    }
}
