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

import static sun.misc.IOUtils.readNBytes;

/**
 * Created by xanarry on 18-1-7.
 */
public class JudgeTest {

    public static void main(String[] argv) throws IOException {
        Request request = new Request();
        request.setSubmitID(0);
        request.setProblemID(1);
        String language = "c";

        //根据语言选择时间和内存限制
        if (language.equals("c") || language.equals("c++") || language.equals("cpp")) {
            request.setTimeLimit(1000);
            request.setMemLimit(65536);
        } else {
            request.setTimeLimit(2000);
            request.setMemLimit(65536 * 2);
        }

        //设置代码的字节长度
        File sourceCodeFile = new File("src\\main\\java\\com\\xanarry\\onlinejudge\\judge\\testcode\\Main.c");
        String sourcecode = FileUtils.readFileToString(sourceCodeFile);
        //System.out.println(sourcecode);

        byte[] codeBytes = sourcecode.getBytes();
        request.setCodeLength(codeBytes.length);

        request.setLanguage(language);
        request.setTestpointCount(10);
        request.setSourcecode(sourcecode);

        System.out.println(request);
        //192.168.56.102
        foo(request, new InetSocketAddress("127.0.0.1", 2345));
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
            socket.shutdownOutput();

            int dataLength = in.readInt();
            byte[] dataBuf = readNBytes(in, dataLength);
            socket.shutdownInput();

            String responseData = new String(dataBuf);
            System.out.println(responseData);
            Response resp = new Gson().fromJson(responseData, Response.class);
            System.out.println(resp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
