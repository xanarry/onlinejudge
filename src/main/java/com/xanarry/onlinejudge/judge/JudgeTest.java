package com.xanarry.onlinejudge.judge;

import com.google.gson.Gson;
import com.xanarry.onlinejudge.judge.beans.Request;
import com.xanarry.onlinejudge.judge.beans.Response;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static sun.misc.IOUtils.readNBytes;

/**
 * Created by xanarry on 18-1-7.
 */
public class JudgeTest {
    private static final String host = "127.0.0.1";
    private static final int port = 8040;
    private static int sid = 0;
    private static final int n = 200;

    private static synchronized int nextID() {
        return sid++;
    }

    public static void main(String[] args) throws IOException {
        Thread c = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < n; i++) test(nextID(), "c");
                } catch (Exception ignored) {
                }
            }
        });

        Thread java = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < n; i++) test(nextID(), "java");
                } catch (Exception ignored) {
                }
            }
        });

        Thread cpp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < n; i++) test(nextID(), "c++");
                } catch (Exception ignored) {
                }
            }
        });

        Thread python = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < n; i++) test(nextID(), "python3");
                } catch (Exception ignored) {
                }
            }
        });


        Executor s = Executors.newCachedThreadPool();
        for (int i = 0; i < 6; i++) {
            s.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        test(nextID(), "java");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        //c.start();java.start();cpp.start();python.start();
    }


    public static void test(int id, String language) throws IOException {
        Request request = new Request();
        request.setSubmitID(id);
        request.setProblemID(1);

        //根据语言选择时间和内存限制
        if (language.equals("c") || language.equals("c++") || language.equals("cpp")) {
            request.setTimeLimit(1000);
            request.setMemLimit(65536);
        } else {
            request.setTimeLimit(40000);
            request.setMemLimit(65536 * 2);
        }

        File sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Main.c");
        switch (language.toLowerCase()) {
            case "c":
                sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Main.c");
                break;
            case "c++":
                sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Main.cpp");
                break;
            case "java":
                sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Main.java");
                break;
            case "python2":
                sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Mainpy2.py");
                break;
            case "python3":
                sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Mainpy3.py");
                break;
        }
        String sourcecode = FileUtils.readFileToString(sourceCodeFile);
        //System.out.println(sourcecode);

        byte[] codeBytes = sourcecode.getBytes();
        request.setCodeLength(codeBytes.length);

        request.setLanguage(language);
        request.setTestpointCount(10);
        request.setSourcecode(sourcecode);

        System.out.println(request);
        //192.168.56.102
        foo(request, new InetSocketAddress(host, port));
    }

    private static void foo(Request request, InetSocketAddress serverAddress) {
        Socket socket = new Socket();
        try {
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

            int dataLength = in.readInt();
            byte[] dataBuf = readNBytes(in, dataLength);
            socket.close();

            String responseData = new String(dataBuf);
            System.out.println(responseData);
            Response resp = new Gson().fromJson(responseData, Response.class);
            System.out.println(resp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
