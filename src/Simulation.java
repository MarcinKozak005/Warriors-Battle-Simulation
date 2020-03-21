import java.awt.*;
import java.awt.image.BufferStrategy;

public class Simulation extends Canvas implements Runnable
{
    Handler handler;
    private static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    // $ apka działa na razie(?) na 1 wątku
    private Thread thread;
    private boolean running = false;

    public Simulation()
    {
        // $ Handler -> obsluguje wszystkie obiekty
        handler = new Handler();
        handler.addSimulationObject(new Infantry(100,400, Alliance.Red,handler));
        handler.addSimulationObject(new Infantry(Simulation.WIDTH-100,100,Alliance.Blue,handler));
        handler.addSimulationObject(new Infantry(Simulation.WIDTH-100,300,Alliance.Blue,handler));
        handler.addSimulationObject(new Infantry(Simulation.WIDTH-100,500,Alliance.Blue,handler));
        // $ Window -> My Class
        new Window(WIDTH, HEIGHT, "Warriors Simulation", this);



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
        // $ Game Loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
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
                tick();
                delta--;
            }
            if(running)
                render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                //System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
        // $ Game Loop
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0, WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Simulation();
    }

}
