package dev.amtektonics.teklife;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class TekKeyHandler implements KeyListener, MouseListener {

    public static int KeySize = 1024;
    public static int MouseKeySize = 10;
    private static boolean[] clicked  = new boolean[KeySize];
    private static boolean[] down = new boolean[KeySize];
    private static boolean[] mouseClicked = new boolean[MouseKeySize];
    private static boolean[] mouseDown = new boolean[MouseKeySize];


    public TekKeyHandler(TekLifeCore core){
        core.addKeyListener(this);
        core.addMouseListener(this);
    }


    public void update(){
        for(int i = 0; i < KeySize; i++){
            if(clicked[i]){
                down[i] = true;
            }
        }
        for(int i = 0; i < MouseKeySize; i++){
            if(mouseClicked[i]){
                mouseDown[i] = true;
            }
        }
    }

    public static boolean KeyPressed(int keycode){
        return clicked[keycode] && !down[keycode];
    }

    public static boolean KeyDown(int keycode){
        return down[keycode] || mouseClicked[keycode];
    }

    public static boolean MousePressed(int button){
        return mouseClicked[button] && !mouseDown[button];
    }

    public static boolean MouseDown(int button){
        return mouseDown[button] || mouseClicked[button];
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e) {
        clicked[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        clicked[e.getKeyCode()] = false;
        down[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClicked[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseClicked[e.getButton()] = false;
        mouseDown[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
