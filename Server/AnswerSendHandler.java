package Server;

import laba.commands.general.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class AnswerSendHandler extends Thread {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private final Command command;
    private final SocketChannel channel;

    public AnswerSendHandler(Command command, SocketChannel channel){
        this.command = command;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            sendObject(command);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Проблемы в передаче данных." + ANSI_RESET);
        }
    }

    private void sendObject(Object obj) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArray);
        stream.writeObject(obj);
        channel.write(ByteBuffer.wrap(byteArray.toByteArray()));
    }
}
