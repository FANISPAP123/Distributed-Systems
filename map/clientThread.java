import java.io.*;
import java.net.*;
public class clientThread extends Thread {
    private Socket Socket;
    private String fileName;
    private int numWorkers;

    public clientThread(Socket Socket,String fileName,int numWorkers){
        this.Socket=Socket;
        this.fileName=fileName;
        this.numWorkers=numWorkers;

    }
    
    public void run(){
        try (PrintWriter out = new PrintWriter(Socket.getOutputStream(),true)) {
            System.out.println(fileName);
     
            // Στέλνουμε το όνομα του αρχείου και τον αριθμό των workers στον server
            System.out.println("fileName");
            out.println(fileName);
            
            out.println(numWorkers);
            
            out.flush();
            out.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
          
        try {
           
            Socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
