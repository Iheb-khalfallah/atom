/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.gaming.atom.examples.kombla.main.server.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;

/**
 *
 * @author iheb_kh
 */
public class SocketMainServerDAO implements MainServerDAO{
    
    private Map<Integer, ClientSession> playerToSocketMap = new HashMap<>();
    private ServerSocket serverSocket;
    private ExecutorService clientThreadPool;

    public Integer getPlayerIdFromSocket(Socket clientSocket) {
        for (Map.Entry<Integer, ClientSession> entry : playerToSocketMap.entrySet()) {
            if (entry.getValue().getSocket() == clientSocket) {
                return entry.getKey();
            }
        }
        return null;
    }

    private int processClient(ClientSession clientSession) {
        Integer playerId = getPlayerIdFromSocket(clientSession.getSocket());

        if (playerId != null) {
            return playerId;
        } else {
            System.out.println("ID du joueur non trouvé");
        }
        
        return -1;
    }


    public void writePlayer(Player player, ObjectOutputStream outputStream) throws IOException {

        outputStream.writeInt(player.getId());
        outputStream.writeObject(player.getName());

        Map<String, Object> properties = player.getProperties();
        outputStream.writeInt(properties.size()); // Write the size of the properties map
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            outputStream.writeObject(entry.getKey());   // Write property key
            outputStream.writeObject(String.valueOf(entry.getValue())); // Convert value to String and write
        }

        outputStream.flush();
    }
    
    public void writeSprite(Sprite sprite, ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(sprite.getId());
        outputStream.writeObject(sprite.getKind());
        outputStream.writeObject(sprite.getName());
        outputStream.writeObject(sprite.getLocation());
        outputStream.writeObject(sprite.getDirection());
        outputStream.writeInt(sprite.getPlayerId());

        Map<String, Object> properties = sprite.getProperties();
        outputStream.writeInt(properties.size()); // Write the size of the properties map
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            outputStream.writeObject(entry.getKey());   // Write property key
            outputStream.writeObject(String.valueOf(entry.getValue())); // Convert value to String and write
        }

        outputStream.flush();
    }

    private static class ClientSession {
        private final int playerId;
        private final Socket socket;
        private final ObjectInputStream inputStream;
        private final ObjectOutputStream outputStream;

        public ClientSession(int playerId, Socket socket) throws IOException {
            this.playerId = playerId;
            this.socket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        public int getPlayerId() {
            return playerId;
        }

        public Socket getSocket() {
            return socket;
        }

        public ObjectInputStream getInputStream() {
            return inputStream;
        }

        public ObjectOutputStream getOutputStream() {
            return outputStream;
        }
    }
    
    @Override
    public void sendModelChanged(DynamicGameModel dynamicGameModel) {
        for (ClientSession clientSession : playerToSocketMap.values()) {
            ObjectOutputStream outputStream = clientSession.getOutputStream();
            try {
                // Write frame
                outputStream.writeLong(dynamicGameModel.getFrame());

                // Write players
                List<Player> players = dynamicGameModel.getPlayers();
                outputStream.writeInt(players.size());
                for (Player player : players) {
                    writePlayer(player, outputStream);
                }

                // Write sprites
                List<Sprite> sprites = dynamicGameModel.getSprites();
                outputStream.writeInt(sprites.size());
                for (Sprite sprite : sprites) {
                    writeSprite(sprite, outputStream);
                }

                // Flush the output stream to ensure data is sent
                outputStream.flush();
            } catch (IOException ex) {
                Logger.getLogger(SocketMainServerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void start(MainServerDAOListener listener, AppConfig properties) {
        try {
            int serverPort = properties.getServerPort(); // Use the server port from properties
            serverSocket = new ServerSocket(serverPort);

            Thread listeningThread = new Thread(() -> {
                try {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();

                        int playerId = processClient(new ClientSession(0, clientSocket));
                        ClientSession clientSession = new ClientSession(playerId, clientSocket);

                        playerToSocketMap.put(clientSession.getPlayerId(), clientSession);

                        Thread clientProcessingThread = new Thread(() -> processClient(clientSession));
                        clientProcessingThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            listeningThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
