package com.example.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class MyThread extends Thread {
    Handler myHandler;
    String name;
    private static final int PORT = 5555;


    public MyThread(String name, Handler myHandler) {
        this.name=name;

        this.myHandler = myHandler;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = new Socket("192.168.1.165",PORT);

            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            // Αποστολή του αρχείου GPX στον server
            out.println(name);
            out.flush();

            // Λήψη των αποτελεσμάτων από τον server
            String sdist = in.readLine();
            String sele = in.readLine();
            String stime = in.readLine();
            String sumd = in.readLine();
            String sume = in.readLine();
            String sumt = in.readLine();
            String sums = in.readLine();

            double dist = Double.parseDouble(sdist);
            double time = Double.parseDouble(stime);
            double ele = Double.parseDouble(sele);
            double sumdist = Double.parseDouble(sumd);
            double sumele = Double.parseDouble(sume);
            double sumspeed = Double.parseDouble(sums);
            double speed = dist / time ;
            double sumtime =Double.parseDouble(sumt);


            double dist1 = (sumdist-dist)/sumdist ;
            double ele1 = (sumele-ele) /sumele ;
            double time1 = (sumtime-time) / sumtime ;
            double sp1 = (sumspeed-speed) /sumspeed ;

            // Αποστολή των αποτελεσμάτων στο Activity2 μέσω του Handler
            //Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("distance", sdist);
            bundle.putString("elevation", sele);
            bundle.putString("time",stime);
            bundle.putString("speed", String.valueOf(speed));
            bundle.putString("newD",String.valueOf(dist1));
            bundle.putString("newEle",String.valueOf(ele1));
            bundle.putString("newTime",String.valueOf(time1));
            bundle.putString("newSpeed",String.valueOf(sp1));

            Message message = new Message();
            message.setData(bundle);
            myHandler.sendMessage(message);

            //xclientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
