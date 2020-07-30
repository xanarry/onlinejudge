package com.xanarry.onlinejudge.datacache;

import com.xanarry.onlinejudge.OjConfiguration;
import com.xanarry.onlinejudge.OnlinejudgeApplication;
import com.xanarry.onlinejudge.dao.TestPointDao;
import com.xanarry.onlinejudge.model.TestPointBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.*;

public class JedisTest {
    public static Jedis jedis = new Jedis("localhost", 6379);  //指定Redis服务Host和port
    static ConfigurableApplicationContext context = null;
    private static TestPointDao testPointDao = null;

    public static void main$(String[] args) {
        context = SpringApplication.run(OnlinejudgeApplication.class, args);
        testPointDao = context.getBean(TestPointDao.class);
        OjConfiguration configuration = context.getBean(OjConfiguration.class);
        System.out.println(configuration);

        String path = "D:/prolems";
        File f = new File(path);
        if (f.isDirectory()) {
            for (File pd : Objects.requireNonNull(f.listFiles())) {
                System.out.println(pd);
            }
        }

        List<TestPointBean> testPointBeans = testPointDao.getTestPointList(2);

        Map<String, String> testData = new TreeMap<>();
        for (TestPointBean tb : testPointBeans) {
            testData.put(tb.getTestPointID() + ".in", tb.getInputTextPath() + tb.getInputTextLength());
            testData.put(tb.getTestPointID() + ".out", tb.getOutputTextPath() + tb.getOutputTextLength());
        }

        jedis.hmset("p" + 2, testData);

        //jedis.auth("xxxx"); //如果Redis服务连接需要密码，制定密码
        Map<String, String> data = jedis.hgetAll("p2"); //访问Redis服务
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println(entry.getKey() + ": ");
        }

        jedis.close(); //使用完关闭连接
    }

    public static void cacheToRedis(int problemID, int testPointID, String stdIn, String stdOut) {
    }

    public static boolean check() {
        boolean r = true;
        for (boolean b : Arrays.asList(true, false, true)) {
            r = r && b;
        }
        return r;
    }

    public static void main(String[] args) {
        System.out.println(check());
    }
}

/*
{
    p1: [
        1: {"in": "asdfasdf", "out": "asdfasdfadsf"},
        2: {"in": "asdfasdf", "out": "asdfasdfadsf"},
        3: {"in": "asdfasdf", "out": "asdfasdfadsf"}
    ]

    p2: [
        1: {"in": "asdfasdf", "out": "asdfasdfadsf"},
        2: {"in": "asdfasdf", "out": "asdfasdfadsf"},
        3: {"in": "asdfasdf", "out": "asdfasdfadsf"}
    ]

    p1: {1.in: "asdfasdf", 1.out: "asdfasdfadsf", 2.in: "asdfasdf", 2.out: "asdfasdfadsf", 3.in: "asdfasdf", 3.out: "asdfasdfadsf"}
    p2: {1.in: "asdfasdf", 1.out: "asdfasdfadsf", 2.in: "asdfasdf", 2.out: "asdfasdfadsf", 3.in: "asdfasdf", 3.out: "asdfasdfadsf"}

    HMSET p1 i.in "Hello" 1.out "World" 1.in "dsadsfasdf" 2.out "asdfasdfawefasdfasdf"
    hmset p1 1.in: "asdfasdf" 1.out: "asdfasdfadsf" 2.in: "asdfasdf" 2.out: "asdfasdfadsf" 3.in: "asdfasdf" 3.out: "asdfasdfadsf"
    hmset p2 1.in: "asdfasdf" 1.out: "asdfasdfadsf" 2.in: "asdfasdf" 2.out: "asdfasdfadsf" 3.in: "asdfasdf" 3.out: "asdfasdfadsf"

 */