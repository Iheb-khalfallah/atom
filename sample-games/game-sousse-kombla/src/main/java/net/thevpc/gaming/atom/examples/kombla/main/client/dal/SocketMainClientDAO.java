/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

import java.io.ByteArrayInputStream;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.ProtocolConstants;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;
/**
 *
 * @author iheb_kh
 */
public class SocketMainClientDAO implements MainClientDAO{
    
    private MainClientDAOListener listener;
    private AppConfig properties;
    private DatagramSocket datagramSocket;
    private Socket clientSocket;

    @Override
    public void start(MainClientDAOListener listener, AppConfig properties) {
        try {
            this.listener = listener;

            String serverAddress = properties.getServerAddress();
            int serverPort = properties.getServerPort();

            this.datagramSocket = new DatagramSocket();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @Override
    public StartGameInfo connect(AppConfig properties) {
        try {
            clientSocket = new Socket(properties.getServerAddress(), properties.getServerPort());
            // Initialisation des flux de la socket TCP
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());//ecrire les données 
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());//lire les données 
            outputStream.writeObject("ConnectionRequest");
            StartGameInfo startGameInfo = (StartGameInfo) inputStream.readObject();
            return startGameInfo;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la connexion au serveur");
        }
    }


    public StartGameInfo readStartGameInfo(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        int playerId = objectInputStream.readInt();
        int mazeSize = objectInputStream.readInt();
        int[][] maze = new int[mazeSize][mazeSize];

        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                maze[i][j] = objectInputStream.readInt();
            }
        }

        return new StartGameInfo(playerId, maze);
    }
    
    public DefaultPlayer readPlayer(InputStream inputStream) throws IOException, ClassNotFoundException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int playerId = dataInputStream.readInt();
        String playerName = dataInputStream.readUTF();
        
        int propertiesSize = dataInputStream.readInt();
        Map<String, String> properties = new HashMap<>();

        for (int i = 0; i < propertiesSize; i++) {
            String key = dataInputStream.readUTF();
            String value = dataInputStream.readUTF();
            properties.put(key, value);
        }

        return new DefaultPlayer(playerId, playerName, properties);
    }
    
    public DefaultSprite readSprite(InputStream inputStream) throws IOException {
        
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int id = dataInputStream.readInt();
        String kind = dataInputStream.readUTF();
        String name = dataInputStream.readUTF();
        double x = dataInputStream.readDouble();
        double y = dataInputStream.readDouble();
        double direction = dataInputStream.readDouble();
        int playerId = dataInputStream.readInt();

        int propertiesSize = dataInputStream.readInt();
        Map<String, String> properties = new HashMap<>();

        for (int i = 0; i < propertiesSize; i++) {
            String key = dataInputStream.readUTF();
            String value = dataInputStream.readUTF();
            properties.put(key, value);
        }

        DefaultSprite defaultSprite = new DefaultSprite(id, kind, name, x, y, direction, playerId, properties);
        return defaultSprite;
    }
    
    public void onLoopReceiveModelChanged(MainClientDAOListener listener) {
        try {
           
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                DynamicGameModel dynamicGameModel = (DynamicGameModel) inputStream.readObject();
                System.out.println("Received model change: " + dynamicGameModel);                
            }

        } catch (IOException | ClassNotFoundException e) {
            
            e.printStackTrace();
            throw new RuntimeException("Error receiving model changes from the server");
        }
    }


    @Override 
    public void sendMoveLeft(){
        try {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
                int serverPort = 1234;

                String moveLeftCommand = String.valueOf(ProtocolConstants.LEFT);

                byte[] data = moveLeftCommand.getBytes();

                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

                datagramSocket.send(packet);
            } else {
                System.out.println("DatagramSocket is not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendMoveRight() {
        sendData(ProtocolConstants.RIGHT);
    }

    @Override
    public void sendMoveUp() {
        sendData(ProtocolConstants.UP);
    }

    @Override
    public void sendMoveDown() {
        sendData(ProtocolConstants.DOWN);
    }

    @Override
    public void sendFire() {
        sendData(ProtocolConstants.FIRE);
    }
    
    public void sendData(int command){
        try {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
                int serverPort = 1234;

                String moveLeftCommand = String.valueOf(command);

                byte[] data = moveLeftCommand.getBytes();

                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

                datagramSocket.send(packet);
            } else {
                System.out.println("DatagramSocket is not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
