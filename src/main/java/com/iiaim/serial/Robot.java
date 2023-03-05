package com.iiaim.serial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author 陈杰炫
 */
@Component(value = "robot")
public class Robot {
    @Autowired
    @Qualifier("read")
    Socket readInfo;

    @Autowired
    @Qualifier("write")
    Socket writeInfo;


    //flag:0represents platform has no running works now
    private int flag;
    //batchsize,used to determine whether script should use
    private int batchSize = 0;
    static byte[] info = new byte[952];
    static double[] realPos = new double[6];
    static double[] CartesianPos = new double[6];
    static double[] speed = new double[6];
    static double[] CartesianSpeed = new double[6];
    static int status;
    static boolean isHome;
    InputStream in;
    OutputStream os;


    @PostConstruct
    public void init() throws IOException {
        in = writeInfo.getInputStream();
        os = writeInfo.getOutputStream();
    }
    /**
     * run procedure
     * @param script
     * @param speed
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String exeScript(String script, int speed) throws IOException, InterruptedException {
        if(speed == -1){
            return send_order(String.format("run(%s)",script));
        }
        else{
            return send_order(String.format("run(%s,%d)",script, speed));
        }
    }

    /**
     * run robot script
     * @param script
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String exeScript(String script) throws IOException, InterruptedException {
             return send_order(String.format("%s",script));
    }

    /**
     * set the speed of robot
     * @param speed
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String setSpeed(int speed) throws IOException, InterruptedException {
             return send_order(String.format("speed(%d);",speed));
    }

    public String send_order(String message) throws IOException, InterruptedException {
        //OutputStream os = writeInfo.getOutputStream();
        os.write(message.getBytes());
        Thread.sleep(100);
        os.flush();
        String s = read_short_feedback();
        System.out.println(s);
        return s;
    }

    private String read_short_feedback() throws IOException {
        //InputStream in = writeInfo.getInputStream();
        byte[] bf = new byte[1024];
        int n = in.read(bf);
        while(n==0) n = in.read(bf);
        return new String(bf);
    }

    /**
     * robot will return "scripit finish" when latest script finished
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean ready() throws IOException,InterruptedException {
        //InputStream in = writeInfo.getInputStream();
        byte[] bf=new byte[1024];
        Thread.sleep(800);
        int n = in.read(bf);
        String feedback = null;
        //while(n<=13) n = in.read(bf);
        while(n!=14) {
            if (n!=-1){
                feedback = new String(bf, 0, n);
                System.out.println(feedback);
            }
            if ("Syntax error\n".equals(feedback)) {
                System.out.println("不符合语法规则");
            }else if ("System error\n".equals(feedback)){
                System.out.println("系统异常");
            }
            n = in.read(bf);
        }
        feedback = new String(bf, 0, n);
        if ("Script finish\n".equals(feedback)){
            System.out.println(feedback);
            return true;
        }
        else{
            System.out.println(feedback);
            return false;
        }
    }

    /**
     * byte array--->long--->double
     * @param idx
     * @return
     */
    private static double bytes2Double(int idx) {
        long value = 0;
        for (int i = idx; i < idx+8; i++) {
            value |= ((long) (info[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }


    /**
     * transfer the bytes data in 2001 port to double
     * @param arr
     * @param idx
     */
    private static void readDouble(double[] arr, int idx){
        for(int i=0; i<6; ++i){
            arr[i] = bytes2Double(idx+i*8);
        }
    }

    private static void read_real_pos(){ readDouble(realPos, 0);}

    private static void read_cartesian_pos(){ readDouble(CartesianPos, 392);}

    private static void read_status(){
        isHome = (info[846]>>4) == 0x01;
        status = info[846] & 0x0f;
    }

    private static void reed_speed(){
        readDouble(speed, 112);
    }

    private static void read_cartesian_speed(){
        readDouble(CartesianSpeed, 488);
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
