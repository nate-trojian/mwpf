package mwpf;

import java.util.ArrayList;
public class Node
{
    private String name;
    private double weight;
    private int row;
    private MapRunner runner;
    private ArrayList<String> dependent = new ArrayList<String>();
    private ArrayList<String> parents = new ArrayList<String>();

    Node(String n, MapRunner r)
    {
	name=n;
	runner=r;
	weight=0;
	row=0;
    }

    Node(String n, double w, MapRunner r)
    {
	name=n;
	runner=r;
	weight=w;
	row=0;
    }

    public void addParent(String name)
    {
	Node temp = runner.getMap().getFromName(name);
	if(temp != null)
	{
	    parents.add(name);
	    if(!temp.isDepend(getName()))
	    {
		temp.addDepend(getName());
	    }
	}
    }

    public int getNumParent()
    {
	return parents.size();
    }

    public Node getParent(int i)
    {
	if(i<getNumParent() && i>=0)
	{
	    return runner.getMap().getFromName(parents.get(i));
	}
	return null;
    }

    public void addDepend(String name)
    {
	Node temp = runner.getMap().getFromName(name);
	if(temp != null)
	{
	    dependent.add(name);
	    if(!temp.isParent(getName()))
	    {
		temp.addParent(getName());
	    }
	}
    }

    public int getNumDepend()
    {
	return dependent.size();
    }

    public Node getDepend(int i)
    {
	if(i<getNumDepend() && i>=0)
	{
	    return runner.getMap().getFromName(dependent.get(i));
	}
	return null;
    }

    public String getName()
    {
	return name;
    }

    public double getWeight()
    {
	return weight;
    }

    public int getRow()
    {
	return row;
    }

    public MapRunner getRunner()
    {
	return runner;
    }

    public boolean isParent(String name)
    {
	return parents.contains(name);
    }

    public boolean isDepend(String name)
    {
	return dependent.contains(name);
    }

    public void setWeight(double num)
    {
	weight=num;
    }

    public void setRow(int r)
    {
	row=r;
    }

    public boolean equals(Node n)
    {
	return n.getName().equals(name) && n.getWeight()==weight;
    }
}