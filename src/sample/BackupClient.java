package sample;
import java.io.*;
import java.rmi.*;
import com.healthmarketscience.rmiio.*;
//import org.apache.commons.logging.*;
import sample.ClientInterface;

import java.io.File.*;
import java.util.concurrent.TimeUnit;

public class BackupClient implements ClientInterface, Serializable {

    public static FileInterface server;

    public BackupClient(String ip){
        super();
        try{
            server = (FileInterface)Naming.lookup("rmi://" + ip + "/BackupServer");
            System.err.println("Connected to server: " + ip);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public static FileInterface getServer(){
        return server;
    }

    public void getFile(RemoteInputStream rinput){
        InputStream input = null;
        try{
            input = RemoteInputStreamClient.wrap(rinput);
            saveFile(input);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveFile(InputStream stream) throws RemoteException, IOException {
        FileOutputStream output = null;

        try {
            File file = File.createTempFile("dataloool", ".jpg", new File("C:\\Users\\Bartłomiej\\Pictures\\Saved Pictures\\601f4aa3fb0c02e17437e7fd58681c1a.jpg"));
            output = new FileOutputStream(file);
            int chunk = 4096;
            byte [] result = new byte[chunk];

            int readBytes = 0;
            do {
                readBytes = stream.read(result);
                if (readBytes > 0)
                    output.write(result, 0, readBytes);
                System.out.println("Zapisuje...");
            } while(readBytes != -1);
            System.out.println(file.length());
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(output != null){
                output.close();
                System.out.println("Zamykam strumień klienta...");
            }
        }
    }

    public static void send(String filepath) throws RemoteException{
        try{
            SimpleRemoteInputStream istream = new SimpleRemoteInputStream(new FileInputStream(filepath));
            server.sendFile(istream.export());
            istream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }

    /*
    public static void main(String[] args) {
        String fileName = "D:\\Zło\\Interstellar (2014) (2014) [1080p]\\Interstellar.mp4";
        System.out.println("Client started");
        try {
            ClientStreamRmi client = new ClientStreamRmi();
            System.out.println("SENDING FILE: " + fileName);
            long start = System.nanoTime();
            long end = System.nanoTime();
            System.out.println("Time: " + TimeUnit.NANOSECONDS.toSeconds(end-start));
            //client.getFile(server.passAStream("D:\\lel.jpg"));
        } catch(Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
        */

}