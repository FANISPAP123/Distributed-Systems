import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 8080;

    private static ArrayList<Integer> w = new ArrayList<Integer>();
    private static ArrayList<String> s3 = new ArrayList<String>();
    private static userStats userStats = new userStats();
    private static Object lock = new Object();

    public static void main(String[] args) {
        s3.add("10.26.34.174");
        s3.add("10.26.34.174");
        s3.add("10.26.34.174");// jimmy ip
        s3.add("10.26.34.174");// jimmy ip
        s3.add("10.26.34.174");// fanis ip
        w.add(5520);
        w.add(5528);
        w.add(5524);
        w.add(5530);
        w.add(5538);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started.");

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                Server server = new Server();
                Server.ClientHandler clientHandler = server.new ClientHandler(socket);
                clientHandler.start();

            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    class ClientHandler extends Thread {

        private Socket clientSocket = null;
        private double sdist = 0.0;
        private double sele = 0.0;
        private double stime = 0.0;
        private double avgspeed = 0.0;
        private double newsdist = 0.0;
        private double newsele = 0.0;
        private String creator;
        private double newstime;
        private ArrayList<String> gpxLines = new ArrayList<String>();
        private ArrayList<Integer> starts = new ArrayList<Integer>();
        private ArrayList<Integer> ends = new ArrayList<Integer>();

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                Random random = new Random();
                BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // System.out.println("ObjectInputStream: " + in);

                String filename = in1.readLine(); // Διαβάζουμε το όνομα του αρχείου από τον client
                //filename = filename + ".gpx";

                GPXGenerator gpxGenerator = new GPXGenerator(filename);
                gpxGenerator.generateGPXFile();
                int numThreads = random.nextInt(4) +2; // Διαβάζουμε τον αριθμό των threads από τον client
                System.out.println("Received file name: " + filename + ", Number of threads: " + numThreads);
                filename = filename +".gpx";
                Master gpxFix = new Master(filename, numThreads);
                gpxFix.run();
                //creator = gpxFix.getCreator();
                gpxLines = gpxFix.getGpxLines();

                starts = gpxFix.getStarts();
                ends = gpxFix.getEnds();
                ArrayList<Server2> s=new ArrayList<Server2>();

                int i = 0;
                for (int j = 0; j <= 4; j++) {


                    try {

                        Socket clientSocket1 = new Socket(s3.get(j), w.get(j));

                        int start = starts.get(i);
                        int end = ends.get(i);
                        Server2 s1 = new Server2(clientSocket1, start, end);
                        s.add( s1);

                        s1.start();




                        i++;
                        if(i==numThreads){
                            break;
                        }



                    } catch (IOException e) {
                        System.err.println("Error processing chunk with worker at port " + w.get(j));
                    }
                    if (j == 4) {
                        j = -1;
                    }
                }
                for(Server2 s3:s){
                    try {
                        s3.join(); // το κύριο νήμα περιμένει μέχρι να ολοκληρωθεί το άλλο νήμα
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                UserMetrics user1stats = new UserMetrics(sdist,sele,stime,avgspeed);
                int sum1 = 0;
                if(userStats.containsKey(creator)){
                    sum1++;
                    UserMetrics oldMe = userStats.getUserMetrics(creator);
                    double newsdist1 = (oldMe.getAverageDistance() + sdist)/2;
                    double newsele1 = (oldMe.getAverageElevation() + sele)/2;
                    double newtime = (oldMe.getAverageExerciseTime() + stime)/2;
                    double newspeed = newsdist1/newtime;
                    UserMetrics user2stats = new UserMetrics(newsdist1,newsele1,newtime,newspeed);
                    userStats.updateUserMetrics(creator, user2stats);
                    String results = "The user's statistics are"+ userStats.getUserMetrics(creator).toString();
                    System.out.println(results);
                }else{
                    userStats.addUserMetrics(creator,user1stats);
                    sum1++;
                    String results = "The user's statistics are"+ userStats.getUserMetrics(creator).toString();
                    System.out.println(results);
                }
                Set<Map.Entry<String, UserMetrics>> entries = userStats.entrySet();
                double sumdist = 0.0;
                double sumele = 0.0;
                double sumtime = 0.0;
                double sumspeed = 0.0;
                int sum = 0;
                for (Map.Entry<String, UserMetrics> entry : entries) {
                    String cr = entry.getKey();
                    UserMetrics user12 = entry.getValue();
                    if((cr != creator) || (sum1 == 1)) {
                        sumdist = sumdist + user12.getAverageDistance();
                        sumele = sumele + user12.getAverageElevation();
                        sumtime = sumtime + user12.getAverageExerciseTime();
                        sumspeed = sumspeed + user12.getAvgSpead();
                        sum++;
                    }
                }
                //UserMetrics sumstat = new UserMetrics(sumdist/sum,sumele/sum,sumtime/sum,sumspeed/sum);
                double newd = sumdist/sum;
                double newe = sumele/sum;
                double newt = sumtime/sum;
                double news = sumspeed/sum;
                System.out.println(newd);
                System.out.println(newe);
                System.out.println(newt);
                System.out.println(news);

                /*String Sdist = String.valueOf(sdist);
                String Sele = String.valueOf(sele);
                String Stime = String.valueOf(stime);*/
                out.println(sdist);
                out.flush();
                out.println(sele);
                out.flush();
                out.println(stime);
                out.flush();
                out.println(newd);
                out.flush();
                out.println(newe);
                out.flush();
                out.println(newt);
                out.flush();
                out.println(news);
                out.flush();
                out.close();
                clientSocket.close();
                System.out.println(sdist+ " "+ sele+ " "+stime );

                //clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        class Server2 extends Thread {
            private Socket socket;
            private int start;
            private int end;
            private static userStats userStats = new userStats();

            public Server2(Socket socket, int start, int end) {

                this.socket = socket;
                this.start = start;
                this.end = end;
            }

            @Override
            public void run() {
                try (BufferedReader in2BufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);

                    int s1 = gpxLines.size();

                    out2.println(s1);
                    out2.flush();
                    for (String s : gpxLines) {
                        out2.println(s);
                        out2.flush();
                    }

                    //System.out.println("gpx");
                    out2.println(start);
                    out2.flush();
                    if (end == gpxLines.size()) {
                        end = end - 1;
                    }
                    out2.println(end);
                    out2.flush();

                    newsdist = Double.parseDouble(in2BufferedReader.readLine());
                    newsele = Double.parseDouble(in2BufferedReader.readLine());
                    newstime = Double.parseDouble(in2BufferedReader.readLine());
                    sdist = sdist + newsdist;
                    sele = sele + newsele;
                    stime = stime + newstime;

                   // socket.close();

                } catch (NumberFormatException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                avgspeed = sdist / stime;

            }

        }

    }

}

