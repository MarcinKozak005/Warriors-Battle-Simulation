package Simulation;

import Exceptions.VictoryException;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ConcurrentModificationException;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    Menu menu;
    public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Thread thread;
    private boolean running = false;

    public enum STATE {
        Menu,
        KarksiBattleSimulation,
        FullPowerSimulation
    }

    public STATE simulationState = STATE.Menu;

    public Simulation()
    {
        handler = new Handler();

        menu = new Menu(this, handler);
        this.addMouseListener(menu);

        new Window(SCREEN_WIDTH, SCREEN_HEIGHT, "Warriors Simulation", this);
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try{
            thread.join();
            running = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >=1)
            {
                try { tick(); }
                catch (VictoryException e){ stop(); }
                delta--;
            }
            if(running) {
                try {
                    render();
                }catch (ConcurrentModificationException ignore){}
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                // System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        if (simulationState == STATE.KarksiBattleSimulation || simulationState == STATE.FullPowerSimulation)
            handler.tick();
        else
            menu.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if (simulationState == STATE.KarksiBattleSimulation || simulationState == STATE.FullPowerSimulation)
            handler.render(g);
        else
            menu.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
