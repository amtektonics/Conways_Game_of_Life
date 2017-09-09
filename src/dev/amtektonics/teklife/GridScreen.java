package dev.amtektonics.teklife;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GridScreen {
    public int[] pixels;
    private int[] buffer;
    public int width, height;
    private TekLifeCore core;
    public boolean simulate = false;


    public GridScreen(int width, int height, TekLifeCore core) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        buffer = new int[width * height];
        this.core = core;
    }

    public void setGridSquare(int x, int y, boolean mode) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        if (mode) {
            pixels[x + y * width] = 0xFFFFFF;
        } else {
            pixels[x + y * width] = 0x0;
        }
    }

    public boolean getGridSquare(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        if (pixels[x + y * width] == 0xFFFFFF){
            return true;
        }else{
            return false;
        }
    }

    public void init(){
    }

    public void update(){
        if(simulate && core.ticks % 100 == 0){
            for (int i = 0; i < width * height; i++) {
                buffer[i] = pixels[i];
            }
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    boolean live = getGridSquare(x, y);
                    int c = getNeighborCount(x, y);
                    if (c < 2) {
                        if (live) {
                            //underpopulation
                            buffer[x + y * width] = 0;
                        }
                    } else if (c == 2 || c == 3) {
                        //live on to the next generation
                        if (!live && c == 3) {
                            //birth
                            buffer[x + y * width] = 0xFFFFFF;
                        }
                    } else if (c > 3) {
                        if (live) {
                            //overpopulation
                            buffer[x + y * width] = 0;
                        }
                    }
                }
            }
            for (int i = 0; i < width * height; i++) {
                pixels[i] = buffer[i];
            }
        }
    }


    protected int getNeighborCount(int x, int y){
        int count = 0;
        if(getGridSquare(x - 1, y - 1)) count++;
        if(getGridSquare(x + 0, y - 1)) count++;
        if(getGridSquare(x + 1, y - 1)) count++;
        if(getGridSquare(x - 1, y + 0)) count++;
        if(getGridSquare(x + 1, y + 0)) count++;
        if(getGridSquare(x - 1, y + 1)) count++;
        if(getGridSquare(x + 0, y + 1)) count++;
        if(getGridSquare(x + 1, y + 1)) count++;
        return count;
    }

    public void clear(){
        for(int i = 0; i < width * height; i++){
            pixels[i] = 0;
        }
    }
}
