package mwpf;

public class Path
{
    private Node a,b;
    private double weight;
    private String name;

    public Path(Node a, Node b, double weight)
    {
	this.a=a;
	this.b=b;
	this.weight=weight;
	this.name="";
    }

    public Path(String name, Node a, Node b, double weight)
    {
	this.name=name;
	this.a=a;
	this.b=b;
	this.weight=weight;
    }

    public Node getA()
    {
	return a;
    }

    public Node getB()
    {
	return b;
    }

    public double getWeight()
    {
	return weight;
    }

    public boolean isStart(Node n)
    {
	return a.equals(n);
    }

    public boolean isStart(String s)
    {
	return a.getName().equals(s);
    }

    public boolean isEnd(Node n)
    {
	return b.equals(n);
    }

    public boolean isEnd(String s)
    {
	return b.getName().equals(s);
    }

    public String getName()
    {
	return name;
    }
}