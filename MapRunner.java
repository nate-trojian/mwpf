package mwpf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class MapRunner
{
    protected Map map;
    final String nl = System.getProperty("line.separator");

    public static void main(String[] args)
    {
	MapRunner mr = new MapRunner();
    }

    public MapRunner()
    {
	Scanner scan = new Scanner(System.in);
	String fileName = scan.next();
	if(fileName.equals("rGen"))
	{
	    int numNodes = scan.nextInt();
	    generateRandom(numNodes);
	}
	scan.close();
	readFile(fileName);
	if(map==null)
	    throw new NullPointerException();

	Timer timer = new Timer();
	//Set up brute force map before we do anything to map
	Map bruteMap = map.newInstance();

	//Dijkstra's Algorithm
	timer.start();
	System.out.println("DIJKSTRA'S: " + dijkstra());
	timer.printMicro();

	//Actually do the algorithm here
	timer.start();
	System.out.println("MY ALGORITHM: " + myAlgorithm());
	timer.printMicro();

	//Brute force
	timer.start();
	System.out.println("BRUTE FORCE: " + bruteSearch(bruteMap,bruteMap.getStartNode()));
	timer.printMicro();
    }

    public void generateRandom(int numNodes)
    {
	try
	{
	    Writer out = new BufferedWriter(new FileWriter("src/mwpf/rGen.txt"));
	    write(out,String.valueOf(numNodes)+nl);
	    int numPaths = ((int) (Math.random()*2*numNodes)) + numNodes + 4;
	    write(out,String.valueOf(numPaths)+nl);

	    //Create list of node names
	    ArrayList<String> nodeNames = new ArrayList<String>();
	    HashSet<String> connectToEnd = new HashSet<String>();
	    nodeNames.add("START");
	    connectToEnd.add("START");
	    for(int i=0;i<numNodes;i++)
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
		//System.out.println(name);
		nodeNames.add(name);
		connectToEnd.add(name);
	    }
	    nodeNames.add("END");

	    //Randomly add paths - Need to makes sure there is a start and end
	    //Store the paths we have already used
	    HashSet<String> usedPaths = new HashSet<String>();
	    DecimalFormat dc = new DecimalFormat("###.##");
	    boolean start = false;
	    boolean end = false;
	    for(int i=0;i<numPaths-connectToEnd.size();i++)
	    {
		int first = (int)(Math.random()*(numNodes-1));
		int second = (int)(Math.random()*(numNodes-1));
		if(second<first)
		{
		    int temp = second;
		    second = first;
		    first = temp;
		}
		if(numPaths-i<=2 && !start)
		{
		    first=0;
		}
		if(numPaths-i<=2 && !end)
		{
		    second=nodeNames.size()-1;
		}
		while(first==second || usedPaths.contains(first+","+second))
		{
		    first = (int)(Math.random()*(numNodes-1));
		    second = (int)(Math.random()*(numNodes-1));
		    if(second<first)
		    {
			int temp = second;
			second = first;
			first = temp;
		    }
		}
		if(!start && first==0)
		    start=true;
		if(second==nodeNames.size()-1)
		{
		    System.out.println("Connecting to end");
		    if(!end)
			end=true;
		    if(connectToEnd.contains(nodeNames.get(first)))
		    {
			connectToEnd.remove(nodeNames.get(first));
		    }
		}
		if(!connectToEnd.contains(nodeNames.get(second)))
		{
		    Iterator<String> it = usedPaths.iterator();
		    String secondName = nodeNames.get(second);
		    while(it.hasNext())
		    {
			String path = it.next();
			if(path.endsWith(secondName))
			{
			    String firstName = path.split(",")[0];
			    if(connectToEnd.contains(firstName))
				connectToEnd.remove(firstName);
			}
		    }
		}
		write(out,nodeNames.get(first) + " " + nodeNames.get(second)+ " " + dc.format(Math.random()*i + Math.random()*5)+nl);
		//System.out.println(first+","+second);
		usedPaths.add(first+","+second);
	    }
	    if(connectToEnd.size() != 0)
	    {
		Iterator<String> it = connectToEnd.iterator();
		while(it.hasNext())
		{
		    write(out,it.next() + " " + "END " + dc.format(Math.random()*(numPaths-connectToEnd.size()) + Math.random()*5)+nl);
		}
	    }
	}
	catch(Exception e)
	{
	    System.out.println("ERROR: " + e);
	}
    }

    public void write(Writer out, String output)
    {
	try
	{
	    out.write(output);
	    out.flush();
	}
	catch(Exception e)
	{
	    System.out.println("ERROR: "+e);
	}
    }

    public void readFile(String fn)
    {
	try
	{
	    map = new Map();
	    map.setStartNode(new Node("START",this));
	    Scanner scan = new Scanner(new File("src/mwpf/"+fn+".txt"));
	    /*
	     * 1: # of nodes (A-Z, Aa-Zz, etc)
	     * 2: # of paths, p
	     * 3-p+2: [A] [B] [Weight]
	     */
	    int numNodes = Integer.valueOf(scan.nextLine());
	    for(int i=0;i<numNodes;i++)
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
		map.addNode(new Node(name,this));
	    }
	    map.setEndNode(new Node("END",this));
	    int numPaths = Integer.valueOf(scan.nextLine());
	    for(int i=0;i<numPaths;i++)
	    {
		String line = scan.nextLine();
		String[] lineSplit = line.split(" ");
		if(lineSplit.length != 3)
		    continue;
		//System.out.println(lineSplit[0] + " " + lineSplit[1]);
		map.addPath(new Path(map.getFromName(lineSplit[0]),map.getFromName(lineSplit[1]),Double.parseDouble(lineSplit[2])));
	    }
	    scan.close();
	}
	catch(Exception e)
	{
	    System.out.println("Failed reading file: "+e);
	}
    }

    public Map getMap()
    {
	return map;
    }

    public PathResult bruteSearch(Map map, Node curNode)
    {
	PathResult p = new PathResult();
	if(curNode.getName().equals("END"))
	{
	    p.setPath("END");
	    return p;
	}
	PathResult[] depends = new PathResult[curNode.getNumDepend()];
	for(int i=0;i<curNode.getNumDepend();i++)
	{
	    Path path = map.getPath(curNode.getName(),curNode.getDepend(i).getName());
	    depends[i] = (new PathResult(path.getA().getName()+",",path.getWeight())).append(bruteSearch(map,curNode.getDepend(i)));
	    //System.out.println(i + " " + depends[i]);
	}

	int minIndex = 0;
	for(int i=1;i<depends.length;i++)
	{
	    if(depends[i].getWeight()<depends[minIndex].getWeight())
		minIndex=i;
	}
	return depends[minIndex];
    }

    public PathResult dijkstra()
    {
	PathResult p = new PathResult();
	Map dMap = map.newInstance();
	HashSet<Node> evaluated = new HashSet<Node>();
	HashSet<Node> unevaluated = new HashSet<Node>();
	HashMap<String,Double> dist = new HashMap<String,Double>();
	HashMap<String,String> previous = new HashMap<String,String>();
	for(int i=0;i<dMap.getSize();i++)
	{
	    Node toAdd = dMap.getNode(i);
	    unevaluated.add(toAdd);
	    dist.put(toAdd.getName(), Double.MAX_VALUE);
	    previous.put(toAdd.getName(), null);
	}
	dist.put("START", 0D);

	Node curNode = dMap.getStartNode(), temp = null;
	while(!unevaluated.isEmpty())
	{
	    Iterator<Node> it = unevaluated.iterator();
	    curNode = it.next();
	    while(it.hasNext())
	    {
		temp = it.next();
		if(dist.get(temp.getName())<dist.get(curNode.getName()))
		    curNode=temp;
	    }
	    evaluated.add(curNode);
	    unevaluated.remove(curNode);
	    if(dist.get(curNode.getName()) == Double.MAX_VALUE || dMap.getEndNode().equals(curNode))
		break;

	    for(int i=0;i<curNode.getNumDepend();i++)
	    {
		Node depend = curNode.getDepend(i);
		if(evaluated.contains(depend))
		    continue;
		double tempWeight = dist.get(curNode.getName()) + dMap.getPath(curNode.getName(), depend.getName()).getWeight();
		if(tempWeight < dist.get(depend.getName()))
		{
		    dist.put(depend.getName(),tempWeight);
		    previous.put(depend.getName(), curNode.getName());
		}
	    }
	}
	p.setWeight(dist.get(curNode.getName()));

	String name=curNode.getName();
	while(previous.get(curNode.getName())!=null)
	{
	    curNode = dMap.getFromName(previous.get(curNode.getName()));
	    name = curNode.getName()+","+name;
	}
	p.setPath(name);
	return p;
    }

    public boolean contains(Node[] arr, Node search)
    {
	for(int i=0;i<arr.length;i++)
	{
	    if(arr[i]!=null && arr[i].equals(search))
		return true;
	}
	return false;
    }

    public int indexOf(Node[] arr, Node search)
    {
	for(int i=0;i<arr.length;i++)
	{
	    if(arr[i]!=null && arr[i].equals(search))
		return i;
	}
	return -1;
    }

    public PathResult myAlgorithm()
    {
	PathResult p = new PathResult();
	map.orderInRows();
	for(int i=1;i<map.getNumRows()-1;i++)
	{
	    map.condense(i);
	}
	String name = map.getPath("START", "END").getName();
	if(name.equals(""))
	    name = "START,END";
	else
	    name = "START," + name + ",END";
	p.setPath(name);
	p.setWeight(map.getPath("START", "END").getWeight());
	return p;
    }
}
