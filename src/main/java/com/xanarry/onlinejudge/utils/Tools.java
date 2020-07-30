package com.xanarry.onlinejudge.utils;


import com.xanarry.onlinejudge.controller.beans.RankBean;
import com.xanarry.onlinejudge.controller.beans.UserProblemStatisticBean;
import com.xanarry.onlinejudge.judge.JudgeClient;
import com.xanarry.onlinejudge.model.ContestBean;
import com.xanarry.onlinejudge.model.ContestProblemBean;
import com.xanarry.onlinejudge.model.ContestUserBean;
import com.xanarry.onlinejudge.model.ViewSubmitRecordBean;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;


/**
 * Created by xanarry on 18-1-1.
 */
public class Tools {
    public static BufferedImage getValidateCode(final int width, final int height, String randStr) {
        int codeX = (width - 10) / (randStr.length() + 1);
        //height - 10 集中显示验证码
        int fontHeight = height - 10;
        int codeY = height - 10;

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.LIGHT_GRAY);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("consolas", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.gray);
        for (int i = 0; i < 16; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < randStr.length(); i++) {
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(randStr.charAt(i) + "", (i + 1) * codeX, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(randStr.charAt(i));
        }
        return buffImg;
    }


    public String TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    public static void showFiles(String path) {
        // get file list where the path has
        File file = new File(path);
        // get the folder list
        if (file.isDirectory()) {
            File[] array = file.listFiles();

            for (int i = 0; i < array.length; i++) {
                if (array[i].isFile()) {
                    // only take file name
                    System.out.println("^^^^^" + array[i].getName());
                    // take file path and name
                    System.out.println("#####" + array[i]);
                    // take file path and name
                    System.out.println("*****" + array[i].getPath());
                } else if (array[i].isDirectory()) {
                    showFiles(array[i].getPath());
                }
            }
        } else {
            System.out.println("giving path is a file");
        }
    }

    public static boolean saveFile(InputStream inputStream, String path) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(path));
            byte[] buf = new byte[8192];
            int len = 0;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            return false;
        }
        return true;
    }

    public static synchronized int saveTestPoint(String testPointDir, String inputData, String outputData) {

        File inputDataDir = new File(testPointDir + "/input");
        File outputDataDir = new File(testPointDir + "/output");

        //检查文件夹是否存在, 不存在则创建
        try {
            if (!inputDataDir.exists()) { //不存在则创建目录
                FileUtils.forceMkdir(inputDataDir);
            }

            if (!outputDataDir.exists()) {
                FileUtils.forceMkdir(outputDataDir);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        //计算新的测试点应该使用什么ID, 根据已有文件推算
        File[] files = inputDataDir.listFiles();
        ArrayList<Integer> testPointIDs = new ArrayList<>(15);
        for (File f : files) {
            String name = f.getName();
            if (name.endsWith(".in")) {
                int val = Integer.parseInt(name.substring(0, name.lastIndexOf(".")));
                testPointIDs.add(val);
            }
        }

        testPointIDs.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        int newID = testPointIDs.size() > 0 ? testPointIDs.get(testPointIDs.size() - 1) + 1 : 1;

        if (testPointIDs.size() > 0 && testPointIDs.get(0) > 1) {
            newID = 1;
        } else {
            for (int i = 0; i < testPointIDs.size() - 1; i++) {
                if (testPointIDs.get(i) + 1 != testPointIDs.get(i + 1)) {
                    newID = testPointIDs.get(i) + 1;
                }
            }
        }


        //写入输入输出文件
        String inputTextPath = inputDataDir + "/" + newID + ".in";
        String outputTextPath = outputDataDir + "/" + newID + ".out";

        try {
            System.out.println("inputTextFile: " + inputTextPath);
            //System.out.println("inputTextData: " + inputData);
            PrintWriter inPrintWriter = new PrintWriter(inputTextPath);
            inPrintWriter.write(inputData);
            inPrintWriter.flush();
            inPrintWriter.close();

            System.out.println("outputTextFile: " + outputTextPath);
            //System.out.println("outputTextData: " + outputData);
            PrintWriter outPrintWriter = new PrintWriter(outputTextPath);
            outPrintWriter.write(outputData);
            outPrintWriter.flush();
            outPrintWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return newID;
    }

    public static synchronized boolean deleteTestPoint(String testPointDir, int testPointID) {
        String inputTextPath = testPointDir + "/input/" + testPointID + ".in";
        String outputTextPath = testPointDir + "/output/" + testPointID + ".out";

        System.out.println("delete inputText: " + inputTextPath);
        System.out.println("delete outputText: " + outputTextPath);

        File inputText = new File(inputTextPath);
        File outputText = new File(outputTextPath);
        if (inputText.delete() && outputText.delete()) {
            //检查文件夹是否为空, 为空的话删除文件夹
            File inputDataDir = new File(testPointDir + "/input");
            if (inputDataDir.listFiles() != null && inputDataDir.listFiles().length == 0) {
                try {
                    FileUtils.deleteDirectory(new File(testPointDir));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }


    public static String readFileToString(String path) {
        System.out.println("read file path: " + path);
        String content = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            content = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     */
    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    public static List<ContestProblemBean> getContestProblemStatistic(List<ViewSubmitRecordBean> submitList, List<ContestProblemBean> problemList) {
        TreeMap<String, TreeMap<String, Integer>> problemStatistic = new TreeMap<>();
        //初始化题目的信息, 单独初始化一遍的原因是, 可能有的题目没人提交, 单独从记录中提取不到这个题目的信息
        TreeMap<Integer, TreeSet<Integer>> accepted = new TreeMap<>();
        TreeMap<Integer, Integer> submitted = new TreeMap<>();

        for (ContestProblemBean p : problemList) {
            accepted.put(p.getProblemID(), new TreeSet<>());
            submitted.put(p.getProblemID(), 0);
        }

        for (ViewSubmitRecordBean s : submitList) {
            if (s.getResult().equals(JudgeClient.ACCEPTED)) {
                accepted.get(s.getProblemID()).add(s.getUserID());
            }
            //对任意一次提交做统计
            submitted.put(s.getProblemID(), submitted.get(s.getProblemID()) + 1);
        }

        for (ContestProblemBean p : problemList) {
            p.setAccepted(accepted.get(p.getProblemID()).size());
            p.setSubmitted(submitted.get(p.getProblemID()));
        }
        return problemList;
    }

    /**
     * 返回每个题目第一个通过者的ID
     *
     * @param submitList 对于一个比赛的所有提交记录, 要求按提交时间先后排序
     * @return 返回一个题目ID到用户ID的map, 标记每个题目的第一个通过者
     */
    private static TreeMap<Integer, Integer> firstAcceptedMap(List<ViewSubmitRecordBean> submitList) {
        TreeMap<Integer, Integer> firstAcMap = new TreeMap<>();
        for (ViewSubmitRecordBean s : submitList) {
            //只检查通过的记录
            if (s.getResult().equals(JudgeClient.ACCEPTED)) {
                if (!firstAcMap.containsKey(s.getProblemID())) {
                    firstAcMap.put(s.getProblemID(), s.getUserID());//插入第一个正确提交代码的人
                }
            }
        }
        return firstAcMap;
    }

    private static RankBean getOnesRank(ContestBean contest, List<ViewSubmitRecordBean> submitList, List<ContestProblemBean> problemList, int userID) {
        //传入的submitList需要事先按照提交时间排序
        submitList.sort(new Comparator<ViewSubmitRecordBean>() {
            @Override
            public int compare(ViewSubmitRecordBean o1, ViewSubmitRecordBean o2) {
                //结果相同比较提交时间
                return (int) (o1.getSubmitTime() - o2.getSubmitTime());
            }
        });


        //建立每个题目第一次提交的人的映射关系
        TreeMap<Integer, Integer> firstAcMap = firstAcceptedMap(submitList);

        //每个用户比赛的信息保存在RankBean中, 对于Map, 手动初始化一下, 否则后面报空指针错误
        RankBean rankBean = new RankBean();
        rankBean.setProblems(new TreeMap<>());

        //保存该用户已经通过了的题目
        HashSet<Integer> acProblems = new HashSet<>(20);

        for (ViewSubmitRecordBean s : submitList) {
            if (s.getUserID() == userID) {//只处理指定用户的信息

                rankBean.setUserID(s.getUserID());
                rankBean.setUserName(s.getUserName());

                if (!rankBean.getProblems().containsKey(s.getProblemID())) {//该用户还没有该记录对应题目的信息, 创建数据结构保存
                    UserProblemStatisticBean problem = new UserProblemStatisticBean();
                    problem.setProblemID(s.getProblemID());//题目ID
                    problem.setAccepted(false);//是否通过
                    problem.setFirstAccepted(false);//是否是第一个通过的人
                    problem.setTryTimes(0);//通过之前一共错误了多少次
                    problem.setTimeConsume(0L); //罚时
                    rankBean.getProblems().put(s.getProblemID(), problem);
                }

                System.out.println(String.format("uid:%d, pid:%d, stime:%d, result:%s", s.getUserID(), s.getProblemID(), s.getSubmitTime(), s.getResult()));


                if (s.getResult().equals(JudgeClient.ACCEPTED)) {
                    if (!acProblems.contains(s.getProblemID())) {
                        System.out.println(s.getProblemID() + " 首次AC");
                        acProblems.add(s.getProblemID());
                        rankBean.getProblems().get(s.getProblemID()).setAccepted(true);
                        //check is first ac here
                        rankBean.getProblems().get(s.getProblemID()).setFirstAccepted(firstAcMap.get(s.getProblemID()) == s.getUserID());
                        rankBean.getProblems().get(s.getProblemID()).setTimeConsume(s.getSubmitTime() - contest.getStartTime());
                        System.out.println("更新: " + rankBean);
                    }
                } else if (s.getResult().equals(JudgeClient.WRONG_ANSWER) ||
                        s.getResult().equals(JudgeClient.TIME_LIMIT_EXCEEDED) ||
                        s.getResult().equals(JudgeClient.MEMORY_LIMIT_EXCEEDED) ||
                        s.getResult().equals(JudgeClient.OUTPUT_LIMIT_EXCEEDED) ||
                        s.getResult().equals(JudgeClient.RUNTIME_ERROR)) {
                    //对于以上情况,罚时20分钟
                    if (!acProblems.contains(s.getProblemID())) {//对于还没有通过的题目增加罚时
                        System.out.println("错误提交");
                        //System.out.println(rankBean.getTotalTimeConsume());
                        rankBean.setTotalTimeConsume(rankBean.getTotalTimeConsume() + 20 * 60 * 1000);
                        int oldTryTimes = rankBean.getProblems().get(s.getProblemID()).getTryTimes();
                        rankBean.getProblems().get(s.getProblemID()).setTryTimes(oldTryTimes + 1);
                        System.out.println("更新: " + rankBean);
                    } else {
                        System.out.println("已经AC, 不用更新");
                    }
                }

            }
        }

        //将所有罚时与每个题目的提交时间加起来
        rankBean.setAC_Count(acProblems.size());
        for (Integer pid : rankBean.getProblems().keySet()) {
            UserProblemStatisticBean tmp = rankBean.getProblems().get(pid);
            if (tmp.isAccepted()) { //如果题目通过, 则加上该题通过是时候的时间跨度
                rankBean.setTotalTimeConsume(rankBean.getTotalTimeConsume() + tmp.getTimeConsume());
            }
        }
        return rankBean;
    }


    public static List<RankBean> calculateRank(ContestBean contestBean, //比赛信息
                                               List<ContestUserBean> contestUserList,//参加比赛的用户信息
                                               List<ContestProblemBean> contestProblemList,//比赛中的题目信息
                                               List<ViewSubmitRecordBean> submitList) { //比赛提交列表
        //初始化排名列表, 将每个参赛选手插入到map
        List<RankBean> rankList = new ArrayList<>(50);

        for (ContestUserBean user : contestUserList) {
            System.out.println("\n\n\n处理: " + user.getUserID());
            RankBean rankBean = getOnesRank(contestBean, submitList, contestProblemList, user.getUserID());
            rankBean.setUserName(user.getUserName());
            rankBean.setUserID(user.getUserID());
            rankList.add(rankBean);
            System.out.println("最终结果:" + rankBean);
        }

        //排序, 排名

        System.out.println("排序前列表:" + rankList);
        rankList.sort(new Comparator<RankBean>() {
            @Override
            public int compare(RankBean o1, RankBean o2) {
                if (o1.getAC_Count() != o2.getAC_Count()) {
                    //解题数量逆序, 越多越靠前,所以加负号
                    return -(o1.getAC_Count() - o2.getAC_Count());
                } else {
                    //罚时顺序,越少越靠前,
                    return (int) (o1.getTotalTimeConsume() - o2.getTotalTimeConsume());
                }
            }
        });

        return rankList;
    }


    public static String saltBase64Encode(String message) {
        byte[] encodedBytes = Base64.getEncoder().encode(message.getBytes());
        return new String(Base64.getEncoder().encode((sha1Hex(message) + new String(encodedBytes)).getBytes()));
    }

    public static String saltBase64Decode(String code) {
        String codeInCode = new String(Base64.getDecoder().decode(code));
        String trueCode = codeInCode.substring(40);
        byte[] decodedBytes = Base64.getDecoder().decode(trueCode);
        return new String(decodedBytes);
    }

}

