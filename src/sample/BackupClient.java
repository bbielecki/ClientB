package sample;
import java.io.*;
import java.rmi.*;
import java.rmi.server.ExportException;

import com.healthmarketscience.rmiio.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class BackupClient implements Serializable {

    public static FileInterface server;

    public BackupClient(String ip, String port){
        super();
        try{
            server = (FileInterface)Naming.lookup("rmi://" + ip + ":"+ port +"/BackupServer");
            System.err.println("Connected to server: " + ip);

        }
        catch (Exception e){
            System.out.println("Witamasd");
            e.printStackTrace();
        }

        new Scrubwoman();

    }

    public static FileInterface getServer(){
        return server;
    }

    public static void getFile(RemoteInputStream rinput, String filename){
        InputStream input;
        try{
            input = RemoteInputStreamClient.wrap(rinput);
            saveFile(input,filename);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveFile(InputStream stream, String filename) throws IOException, RemoteException {
        FileOutputStream output = null;
        File file = null;
        String extension = filename.substring(filename.lastIndexOf("."),filename.lastIndexOf("-"));
        try {
            file = File.createTempFile(filename, extension, new File("D:\\Client"));
            output = new FileOutputStream(file);

            int chunk = 4096;
            byte [] result = new byte[chunk];
            int readBytes;
            do {
                readBytes = stream.read(result);
                if (readBytes > 0){
                    output.write(result, 0, readBytes);
                }
                System.out.println("Zapisuje...");
            } while(readBytes != -1);
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(output != null){
                output.close();
                if(file.renameTo(new File(file.getParent() + "\\" + filename + extension))){

                    System.out.println("Rename succesful");
                }
                else{
                    System.out.println("Rename failed");
                }
                System.out.println("Zamykam strumień...");
            }
        }
    }

    public static void send(FileInterface server, String filepath, String filename, String extension, long lastModified) throws RemoteException{

        try{
            SimpleRemoteInputStream istream = new SimpleRemoteInputStream(new FileInputStream(filepath));
            server.sendFile(istream.export(), filename, extension, lastModified);
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

    public static ObservableList<ServerTable> getTable(RemoteInputStream ris) throws IOException {
        InputStream in;
        ObjectInputStream ois = null;
        ObservableList<ServerTable> list = FXCollections.observableArrayList();
        try {
            in = RemoteInputStreamClient.wrap(ris);
            ois = new ObjectInputStream(in);
            Object ob = ois.readObject();
            ois.close();
            String[] srv = (String[]) ob;
            for(int i = 0; i<srv.length; i = i+4){
                list.addAll(new ServerTable(srv[i],srv[i+1],srv[i+2],srv[i+3]));
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage() + " Wyjątek");
        }
        return list;
    }

    public static int getNumberOfChunks(RemoteInputStream ris) throws IOException {
        InputStream in;
        ObjectInputStream ois = null;
        int chunks = 0;
        try {
            in = RemoteInputStreamClient.wrap(ris);
            ois = new ObjectInputStream(in);
            chunks = ois.readInt();
            ois.close();

        }
        catch(Exception e){
            System.out.println(e.getMessage() + " Wyjątek23");
        }
        return chunks;
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