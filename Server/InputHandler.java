package Server;

import laba.com.company.Ticket;
import laba.commands.Exit;
import laba.commands.general.Command;
import laba.sercurity.User;
import laba.com.company.DataBaseHandler;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class InputHandler implements Runnable {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final SocketChannel channel;
    protected static List<Ticket> collection;

    public InputHandler(SocketChannel channel){
        this.channel = channel;
    }

    @Override
    public void run() {
        if (channel == null) throw new IllegalArgumentException(ANSI_RED+"Channel is null"+ANSI_RESET);
        try {
            while (!channel.socket().isClosed()) {
                /*try{
                    collection = Collections.synchronizedList(DataBaseHandler.getTicketCollection());
                }
                catch(SQLException e){
                }*/
                Object object = receiveObject();
                if (!(object instanceof Command))
                    if(object instanceof User){
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        ObjectOutputStream stream = new ObjectOutputStream(byteArray);
                        stream.writeObject(DataBaseHandler.checkLoginAccuracy((User)object));
                        channel.write(ByteBuffer.wrap(byteArray.toByteArray()));
                        continue;
                    } else continue;
                Server.processingInquiryPool.execute(new ProcessingHandler((Command) object, channel));
                if(object instanceof Exit) break;
            }
        } catch (IOException e) {
        }
    }

    /**
     * Метод получает объект
     *
     * @return Объект, полученный от сервера
     */
    private Object receiveObject() throws IOException {
        if (channel == null) {
            System.err.println(ANSI_RED+"Соединене не создано."+ANSI_RESET);
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        channel.read(buffer);
        try {
            ObjectInputStream objStream = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            return objStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}

