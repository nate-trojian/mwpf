package mwpf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class MapGenerator
{
    public static void main(String[] args) throws IOException
    {
	Scanner scan = new Scanner(System.in);
	int a = scan.nextInt();
	MapGenerator mg = new MapGenerator(a);
    }

    public MapGenerator(int numNodes) throws IOException
    {
	Writer out = new BufferedWriter(new FileWriter("gen.txt"));

	HashList<String,Integer> weightedList = new HashList<String,Integer>();
	ArrayList<String> conStart = new ArrayList<String>(), conEnd = new ArrayList<String>();
	int numPaths = numNodes*2 + (int) Math.round(Math.random()*Math.abs(numNodes-3));
	System.out.println(numPaths);
	//write(out,""+numNodes);
	//write(out,""+numPaths);

	//Weight key - 4 = no connections, 3 = connected to end, 2 = connected to start, 1 = connected to both

	for(int i=1;i<numNodes-1;i++)
	{
	    int temp = i;
	    String name = "";
	    int log = (int)Math.floor(Math.log(temp)/Math.log(26));
	    if(log==Integer.MIN_VALUE)
		log=0;
	    //name = ""+((char)(65+temp/((int)Math.pow(26, log))));
	    for(int j=0;j<=log;j++)
	    {
		name=((char)(65+temp%26))+name;
		temp= (int)Math.floor(temp/26D)-1;
	    }
	    weightedList.add(name, 4);
	}

	String first = weightedList.getKeyAt((int)Math.round(Math.random()*(numNodes-2)));
	conStart.add("START");
	conStart.add(first);
	weightedList.add("START", 2, 0);
	weightedList.add(first, 2);
	System.out.println("1: START " + first);
	//write(out,"START " + first + " " + (int)(Math.random()*10));
	String second = weightedList.getKeyAt((int)Math.round(Math.random()*(numNodes-1)));
	conEnd.add(second);
	conEnd.add("END");
	weightedList.add(second, 3);
	weightedList.add("END", 3);
	System.out.println(numPaths + ": " + second + " END");
	//write(out, second + " END" + (int)(Math.random()*10));

	for(int i=2;i<numPaths-getNeedConnect(weightedList);i++)
	{
	    first = getWeightedNum(weightedList);
	    second = getWeightedNum(weightedList);
	    while(first.equals(second))
	    {
		first = getWeightedNum(weightedList);
		second = getWeightedNum(weightedList);
	    }
	    if((first.equals("END") || second.equals("START")) || (!first.equals("START") && !second.equals("END") && first.compareTo(second) > 0))
	    {
		String temp = first;
		first = second;
		second = temp;
	    }
	    if(conStart.contains(first) && !conStart.contains(second))
	    {
		//Connects to start
		conStart.add(second);
		weightedList.add(second, weightedList.get(second)-2);
	    }
	    if(conEnd.contains(second) && !conEnd.contains(first))
	    {
		//Connects to end
		conEnd.add(first);
		weightedList.add(first, weightedList.get(first)-1);
	    }
	    System.out.println(i+": " + first + " " + second);
	}

	Iterator<String> it = weightedList.keySet().iterator();
	while(it.hasNext())
	{
	    String name = it.next();
	    int val = weightedList.get(name);
	    if(val!=1)
	    {
		String select="";
		if(val==4 || val==3) //Connected to end - need to connect to start
		{
		    select = conStart.get((int) Math.round(Math.random()*(conStart.size()-1)));
		    //while name is greater than name, reselect
		    //System.out.println("PICKING FOR START "+name);
		    while(!select.equals("START") && (select.equals("END") || name.compareTo(select)<0))
		    {
			select = conStart.get((int) Math.round(Math.random()*(conStart.size()-1)));
			//System.out.println(select);
		    }
		    //System.out.println("SELECTED " + select);
		    //add path - update
		    conStart.add(name);
		    weightedList.add(name, weightedList.get(name)-2);
		    //System.out.println("val=" + val + " " + select + " " + name);
		    val-=2;
		}
		if(val==2) //Connected to start - need to connect to end
		{
		    select = conEnd.get((int) Math.round(Math.random()*(conEnd.size()-1)));
		    //while name is less than select, reselect
		    //System.out.println("PICKING FOR END "+name);
		    while(!select.equals("END") && (select.equals("START") || name.compareTo(select)>0))
		    {
			select = conEnd.get((int) Math.round(Math.random()*(conEnd.size()-1)));
		    }
		    //System.out.println("SELECTED " + select);
		    //add path - update
		    conEnd.add(name);
		    weightedList.add(name, weightedList.get(name)-1);
		    //System.out.println("val=2 " + name + " " + select);
		}
	    }
	    else
	    {
		first = getWeightedNum(weightedList);
		second = getWeightedNum(weightedList);
		while(first.equals(second))
		{
		    first = getWeightedNum(weightedList);
		    second = getWeightedNum(weightedList);
		}
		if((first.equals("END") || second.equals("START")) || (!first.equals("START") && !second.equals("END") && first.compareTo(second) > 0))
		{
		    String temp = first;
		    first = second;
		    second = temp;
		}
		System.out.println(first + " " + second);
	    }
	}
    }

    public int getNeedConnect(HashList<String,Integer> arr)
    {
	int count = 0;
	Iterator<Integer> it = arr.values().iterator();
	while(it.hasNext())
	{
	    if(it.next()!=1)
		count++;
	}
	return count;
    }

    public String getWeightedNum(HashList<String,Integer> arr)
    {
	ArrayList<String> list = new ArrayList<String>();
	Iterator<String> it = arr.keySet().iterator();
	while(it.hasNext())
	{
	    String name = it.next();
	    int count = 0, val = arr.get(name);
	    do
	    {
		list.add(name);
		count++;
	    } while(count<val);
	}
	return list.get((int) Math.round(Math.random()*(list.size()-1)));
    }

    public void write(Writer out, String output) throws IOException
    {
	out.write(output);
	out.flush();
    }
}