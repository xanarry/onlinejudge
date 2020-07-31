package com.xanarry.onlinejudge.judge;


import com.google.gson.Gson;
import com.xanarry.onlinejudge.ApplicationContextHolder;
import com.xanarry.onlinejudge.OjConfiguration;
import com.xanarry.onlinejudge.dao.*;
import com.xanarry.onlinejudge.judge.beans.Request;
import com.xanarry.onlinejudge.judge.beans.Response;
import com.xanarry.onlinejudge.model.*;
import org.springframework.context.ApplicationContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static sun.misc.IOUtils.readNBytes;

/**
 * Created by xanarry on 20-7-28.
 */

public class JudgeClient {
    //最终结果
    public static final String QUEUING = "Queuing";
    public static final String COMPILING = "Compiling";
    public static final String RUNNING = "Running";
    public static final String ACCEPTED = "Accepted";
    public static final String PRESENTATION_ERROR = "Presentation Error";
    public static final String WRONG_ANSWER = "Wrong Answer";
    public static final String TIME_LIMIT_EXCEEDED = "Time Limit Exceeded";
    public static final String MEMORY_LIMIT_EXCEEDED = "Memory Limit Exceeded";
    public static final String OUTPUT_LIMIT_EXCEEDED = "Output Limit Exceeded";
    public static final String RUNTIME_ERROR = "Runtime Error";
    public static final String SYSTEM_ERROR = "System Error";
    public static final String COMPILATION_ERROR = "Compilation Error";

    //中间状态
    private static final String SUBMIT_STATE = "submit";
    private static final String WAIT_STATE = "wait";
    private static final String FINISH_STATE = "finish";
    private static final String STATE = "state";
    private static JudgeClient instance;

    //获取spring上下文
    ApplicationContext context = ApplicationContextHolder.getContext();
    //访问提交记录
    private final SubmitRecordDao submitRecordDao = context.getBean(SubmitRecordDao.class);
    //访问系统错误
    private final SystemErrorDao systemErrorDao = context.getBean(SystemErrorDao.class);
    //访问评测详细结果
    private final JudgeDetailDao judgeDetailDao = context.getBean(JudgeDetailDao.class);
    //访问编译结果
    private final CompileInfoDao compileInfoDao = context.getBean(CompileInfoDao.class);
    //访问题目结果
    private final ProblemDao problemDao = context.getBean(ProblemDao.class);
    //访问测试点结果
    private final TestPointDao testPointDao = context.getBean(TestPointDao.class);
    //访问oj配置数据
    private final OjConfiguration configuration = context.getBean(OjConfiguration.class);


    private final InetSocketAddress serverAddress;
    private final Executor executor;

    private JudgeClient() {
        executor = new ThreadPoolExecutor(15, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000));
        serverAddress = new InetSocketAddress(configuration.getJudgeServerIP(), configuration.getJudgeServerPort());
    }

    public static synchronized JudgeClient getInstance() {
        if (instance == null) {
            instance = new JudgeClient();
        }
        return instance;
    }


    public void sumbitJudgeTask(SubmitRecordBean submitRecord) {
        /*
        Request:
        private int submitID;
        private int problemID;
        private int timeLimit;
        private int memLimit;
        private int codeLength;
        private String language;
        private int testpointCount;
        private String sourcecode;
         */

        ProblemBean problemBean = problemDao.getProblemByID(submitRecord.getProblemID()); //获取提交代码所属的题目

        Request request = new Request();
        request.setSubmitID(submitRecord.getSubmitID());
        request.setProblemID(submitRecord.getProblemID());
        String language = submitRecord.getLanguage().toLowerCase();

        //根据语言选择时间和内存限制
        if (language.equals("c") || language.equals("c++")) {
            request.setTimeLimit(problemBean.getDynamicLangTimeLimit());
            request.setMemLimit(problemBean.getDynamicLangMemLimit());
        } else {
            request.setTimeLimit(problemBean.getStaticLangTimeLimit());
            request.setMemLimit(problemBean.getStaticLangMemLimit());
        }

        //设置代码的字节长度
        String sourcecode = submitRecord.getSourceCode();
        byte[] codeBytes = sourcecode.getBytes();
        request.setCodeLength(codeBytes.length);

        request.setLanguage(submitRecord.getLanguage());
        List<TestPointBean> testpointsFile = testPointDao.getTestPointList(submitRecord.getProblemID());
        request.setTestpointCount(testpointsFile.size());
        request.setSourcecode(submitRecord.getSourceCode());

        executor.execute(() -> {
            Socket socket = new Socket();
            try {
                System.out.println("connect to " + serverAddress);
                socket.connect(serverAddress);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                out.writeInt(request.getSubmitID());
                out.writeInt(request.getProblemID());
                out.writeInt(request.getTimeLimit());
                out.writeInt(request.getMemLimit());
                out.writeInt(request.getCodeLength());
                out.writeInt(request.getTestpointCount());
                byte[] zeroFilledBytes = new byte[20];
                byte[] languageBytes = request.getLanguage().getBytes();
                System.arraycopy(languageBytes, 0, zeroFilledBytes, 0, languageBytes.length);
                out.write(zeroFilledBytes, 0, 20);
                out.write(request.getSourcecode().getBytes());
                out.flush();
                //socket.shutdownOutput();

                System.out.println(submitRecord);

                //更新状态
                submitRecord.setResult(RUNNING);
                submitRecordDao.updateSubmitRecord(submitRecord);

                int dataLength = in.readInt();
                byte[] dataBuf = readNBytes(in, dataLength);
                //socket.shutdownInput();

                String responseData = new String(dataBuf);
                System.out.println(responseData);
                Response resp = new Gson().fromJson(responseData, Response.class);
                System.out.println(resp);

                //存入结果
                if (resp.getCompileResult().length() > 0) {
                    compileInfoDao.insertCompileResult(new CompileInfoBean(submitRecord.getSubmitID(), resp.getCompileResult()));
                }

                submitRecord.setJudgeTime(resp.getJudgeTime());
                submitRecord.setTimeConsume(resp.getTimeConsume());
                submitRecord.setMemConsume(resp.getMemConsume());
                submitRecord.setResult(resp.getFinalResult());
                submitRecordDao.updateSubmitRecord(submitRecord);

                for (JudgeDetailBean judgeDetail : resp.getJudgeDetails()) {
                    judgeDetail.setSubmitID(request.getSubmitID());
                    judgeDetailDao.insertJudgeDetail(judgeDetail);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /*评测中间状态变化, 处理以下四种情况
     * Queuing
     * Compiling
     * Running
     * System Error
     * */
    public void onStateChange(SubmitRecordBean submit, String state, String detail) {
        submit.setResult(state);
        submit.setJudgeTime(new Date().getTime());

        submitRecordDao.updateSubmitRecord(submit);

        /*如果是system error, 保持错误信息*/
        if (state.equals(JudgeClient.SYSTEM_ERROR)) {
            systemErrorDao.addErrorMessage(new SystemErrorBean(submit.getSubmitID(), detail));
        }
    }
}

/*
发送到服务器的json
{
    "submitID":129,
    "language":"C",
    "timeLimit":1000,
    "memLimit":65535,
    "runningFolder":"/home/xanarry/Desktop/running-dir/submit129",
    "testPointDataFolder":"/home/xanarry/Workspace/IdeaProjects/oj/out/artifacts/oj_war_exploded/test-points/p1007"
}

*/
