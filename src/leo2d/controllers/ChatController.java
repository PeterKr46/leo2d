package leo2d.controllers;


import game.packet.ChatPacket;
import leo2d.client.ClientOutThread;
import leo2d.core.Camera;
import leo2d.input.Input;
import leo2d.math.Vector;
import leo2d.sprite.font.Font;

import java.awt.event.KeyEvent;


/**
 * Created by Peter on 18.11.2015.
 */
public class ChatController extends EditorController {

    private static Font font = Font.load("assets/Font.png");
    private static String[] lines = new String[] {"","","","","","","","",""};

    public static String currentChatLine = "";
    public static boolean chatActive = false;

    public static void addLine(String line) {
        System.arraycopy(lines, 0, lines, 1, lines.length - 1);
        lines[0] = line;
    }

    public ChatController() {
        super();
    }

    @Override
    public void update() {
        double lineHeight = Camera.main().getVerticalSize()/20;
        Vector min = Camera.main().getMin().toFixed();
        for(int i = 0; i < lines.length; i++) {
            font.write(
                    min.add(0, lineHeight * (i+1)),
                    lines[i],
                    lineHeight,
                    0);
        }
        if(chatActive) {
            if(Input.getKeyDown(KeyEvent.VK_BACK_SPACE)) {
                currentChatLine = currentChatLine.substring(0, Math.max(0,currentChatLine.length()-1));
            } else if(Input.getKeyDown(KeyEvent.VK_ESCAPE)){
                chatActive = false;
                currentChatLine = "";
            } else {
                if(Input.getKeyDown(KeyEvent.VK_ENTER) && currentChatLine.length() > 1) {
                    ClientOutThread.outQueue.add(new ChatPacket(currentChatLine));
                    currentChatLine = "";
                    chatActive = false;
                } else {
                    currentChatLine += Input.getPressedKey();
                }
            }
            font.write(min, "> " + currentChatLine + (((int)System.currentTimeMillis()/500) % 2 == 0 ? "#" : "_"), lineHeight, 0);
        }
        if(!chatActive && Input.getKeyDown(KeyEvent.VK_T)) {
            chatActive = true;
        }
    }
}
