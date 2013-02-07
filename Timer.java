package mwpf;

public class Timer
{
    private long start;

    public Timer()
    {
    } 

    // return time (in seconds) since this object was created
    public double elapsed()
    {
        long now = System.nanoTime();
        return (now - start);
    }
    
    public void start()
    {
        start = System.nanoTime();
    }
    
    public void printElapsed()
    {
        long now = System.nanoTime();
        System.out.println((now-start) + " ns");
    }
    
    public double elapsedMicro()
    {
        long now = System.nanoTime();
        return (now - start)/1000;
    }
    
    public void printMicro()
    {
        long now = System.nanoTime();
        System.out.println(((now-start)/1000) + " microsec");
    }
}