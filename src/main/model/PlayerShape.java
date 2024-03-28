package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

//Visual component of each block in world
//Code partially adapted from SimpleDrawingPlayer

public class PlayerShape extends BlockShape {

    private Player player;
    private static final String IDLE1 = "./data/idleframe1.png";
    private static final String IDLE2 = "./data/idleframe2.png";
    private static final String AIR = "./data/airframe.png";
    private static final String IDLE1RIGHT = "./data/idleframe1right.png";
    private static final String IDLE2RIGHT = "./data/idleframe2right.png";
    private static final String AIRRIGHT = "./data/airframeright.png";

    public PlayerShape(int x, int y, Color color, Player player) {
        super(x,y,color);
        this.player = player;
    }

    @Override
    public void draw(Graphics g) {
        String source = "";
        if (player.getIsLeft()) {
            if (player.getFalling() | player.getJumping()) {
                source = AIR;
            } else if (player.getIdleFrame() == 1) {
                source = IDLE1;
            } else if (player.getIdleFrame() == 2) {
                source = IDLE2;
            }
        } else {
            if (player.getFalling() | player.getJumping()) {
                source = AIRRIGHT;
            } else if (player.getIdleFrame() == 1) {
                source = IDLE1RIGHT;
            } else if (player.getIdleFrame() == 2) {
                source = IDLE2RIGHT;
            }
        }

        try {
            Image image = ImageIO.read(new File(source));
            g.drawImage(image, getXpos(), getYpos(), null);
        } catch (Exception e) {
            //do nothing
        }
    }
}
