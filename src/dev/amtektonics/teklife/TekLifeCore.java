package dev.amtektonics.teklife;

import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class TekLifeCore extends Canvas implements Runnable {
    private boolean running = false;
    private Thread thread;
    public int Width = 160, Height = 120;
    public int Scale = 8;
    public long ticks = 0;
    //
    private BufferedImage image;
    private int[] pixels;
    //
    private GridScreen screen;
    private TekKeyHandler handler;

    public void start(){
        running = true;
        createWindow();
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        running = false;
        try { thread.join(); }catch(Exception e) {e.printStackTrace();}
    }


    protected void createWindow(){
        JFrame frame = new JFrame("Game of Life");
        frame.setPreferredSize(new Dimension(Width * Scale, Height * Scale));
        frame.setMinimumSize(new Dimension(Width * Scale, Height *Scale));
        frame.setMaximumSize(new Dimension(Width *Scale, Height * Scale));
        frame.pack();

        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    protected void init(){
        image = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        screen = new GridScreen(Width, Height, this);
        screen.init();
        handler = new TekKeyHandler(this);
    }

    @Override
    public void run(){
        init();
        while(running){
            update();
            render();
        }
    }
    protected void update(){
        ticks++;
        screen.update();

        if(TekKeyHandler.KeyPressed(KeyEvent.VK_SPACE)){
            screen.simulate = !screen.simulate;
        }

        if(TekKeyHandler.KeyPressed(KeyEvent.VK_C)){
            screen.clear();
        }

        int mouseX = (int)(MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX()) / Scale;
        int mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY()) / Scale;

        if(TekKeyHandler.MouseDown(MouseEvent.BUTTON1)){
            screen.setGridSquare(mouseX, mouseY, true);
        }

        if(TekKeyHandler.MouseDown(MouseEvent.BUTTON3)){
            screen.setGridSquare(mouseX, mouseY, false);
        }


        handler.update();
    }
    //19th at 9
    protected void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, Width, Height);
        for(int i = 0; i < Width * Height; i++){
            pixels[i] = screen.pixels[i];
        }

        image.getRGB(0, 0, Width, Height, pixels, 0, Width);
        g.drawImage(image, 0, 0, Width * Scale, Height * Scale, null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args){
        TekLifeCore core = new TekLifeCore();
        core.start();
    }
}
