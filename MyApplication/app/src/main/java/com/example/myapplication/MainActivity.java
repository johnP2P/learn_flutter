package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                testRunShellCmd();
            }
        }).start();

    }

    private void testRunShellCmd(){
        String cmd = String.format(
                "input swipe 400 800 400 100\n");
        for (int i = 0; i < 10; i++) {
//            try {
//                Process exeEcho = Runtime.getRuntime().exec(cmd);
//                exeEcho.getOutputStream().write(cmd.getBytes());
//                exeEcho.getOutputStream().flush();
//            } catch (IOException e) {
//                Log.e("Excute exception: ", e.getMessage());
//            }
//            rootCommand(cmd);
            execCmd(cmd);
            Log.d("cmd", "adb count:" + i);
            int endNum = 50;
            int startNum = 2;
            int sTime = new Random().nextInt(endNum - startNum) + startNum;
            try {
                Thread.sleep(sTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** 执行控制台命令，参数为命令行字符串方式，申请Root控制权限*/
    public static boolean rootCommand(String command){
        Process process = null;
        DataOutputStream os = null;
      /*  BufferedReader buf=null;
        InputStream is = null;*/
        try{
            process = Runtime.getRuntime().exec(command);//执行这一句，superuser.apk就会弹出授权对话框
//            os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes(command + "\n");
///*
//            buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
//*/
//         /*   String result=buf.readLine();
//            Log.d(TAG,"result is "+result);*/
//            os.writeBytes("exit\n");
//            os.flush();
            process.waitFor();
        } catch (Exception e){
            Log.e(TAG, "获取root权限失败： " + e.getMessage());
            return false;
        }
        Log.d(TAG, "获取root权限成功");
        return true;
    }

    private void execCmd(String cmd){
        Process su = null;
        try {
            su = Runtime.getRuntime().exec("su");
            su.getOutputStream().write(cmd.getBytes());
            su.getOutputStream().write("exit\n".getBytes());
            su.waitFor();
            Log.d(TAG, "exec 成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "exec 失败");
        } finally {
            if (su != null) {
                su.destroy();
            }
        }
    }
}
