package mwpf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Map
{
    private Node startNode;
    private Node endNode;
    private ArrayList<Node> nodes; //Sort nodes list by row
    private HashBucket<String,String,Path> paths;
    //private ArrayList2D<Node> rows;

    public Map()
    {
	nodes = new ArrayList<Node>();
	paths = new HashBucket<String,String,Path>();
	//rows = new ArrayList2D<Node>();
    }

    /*public Map(ArrayList2D<Node> rows)
    {
    	this.rows=rows;
    	nodes = new ArrayList<Node>();


    	if(rows.getRow(0).size()!=1)
    		setEndNode(new Node("START",this));
    	else
    		setEndNode(rows.get(0, 0));

    	for(int i=1; i<rows.getNumRows()-1;i++)
    		nodes.addAll(rows.getRow(i));

    	if(rows.getRow(rows.getNumRows()-1).size()!=1)
    		setEndNode(new Node("END",this));
    	else
    		setEndNode(rows.get(rows.getNumRows()-1, 0));
    }*/

    public void setStartNode(Node start)
    {
	startNode=start;
	nodes.add(startNode);
    }

    public void addNode(Node node)
    {
	nodes.add(node);
    }

    public void setEndNode(Node end)
    {
	endNode=end;
	nodes.add(endNode);
    }

    public void addPath(Path p)
    {
	try
	{
	    if(!p.getA().isDepend(p.getB().getName()))
	    {
		paths.addToBucket(p.getA().getName(),p.getB().getName(),p);
		p.getA().addDepend(p.getB().getName());
	    }
	    else
	    {
		Path path = getPath(p.getA().getName(),p.getB().getName());
		//System.out.println(path==null);
		if(path.getWeight() > p.getWeight())
		{
		    paths.removeFromBucket(path.getA().getName(),path.getB().getName());
		    paths.addToBucket(p.getA().getName(),p.getB().getName(),p);
		}
	    }
	}
	catch(NullPointerException ne)
	{
	    System.out.println("A Node Does Not Exist - " + p.getA().getName() + ": " + nodes.contains(p.getA()) + " - " + p.getB().getName() + ": " + nodes.contains(p.getB()));

	}
    }

    public void orderInRows()
    {
	for(int i=0;i<nodes.size();i++)
	{
	    Node temp = nodes.get(i);
	    if(!temp.equals(startNode))
	    {
		int maxRow = 0;
		for(int j=0;j<temp.getNumParent();j++)
		{
		    if(!temp.getParent(j).equals(startNode))
		    {
			if(temp.getParent(j).getRow()>maxRow)
			    maxRow = temp.getParent(j).getRow();
		    }
		}
		temp.setRow(maxRow+1);
	    }
	}

	for(int i=0;i<nodes.size();i++)
	{
	    System.out.println(nodes.get(i).getName() + " " + nodes.get(i).getRow());
	}
    }

    public Node getFromName(String name)
    {
	//System.out.println("Name to get " + name);
	for(int i=0;i<nodes.size();i++)
	{
	    if( ((nodes.get(i)).getName()).equals(name))
	    {
		return nodes.get(i);
	    }
	}

	return null;
    }

    public Node getStartNode()
    {
	return startNode;
    }

    public Node getEndNode()
    {
	return endNode;
    }

    public int getSize()
    {
	return nodes.size();
    }

    public int getNumRows()
    {
	return nodes.get(nodes.size()-1).getRow()+1;
    }

    public Node getNode(int i)
    {
	if(i<nodes.size())
	{
	    return nodes.get(i);
	}
	return null;
    }

    public int indexOf(Node n)
    {
	for(int i=0;i<nodes.size();i++)
	{
	    if(nodes.get(i).equals(n))
		return i;
	}
	return -1;
    }

    public Path getPath(String start, String end)
    {
	return paths.getFromBucket(start, end);
    }

    public void condense(int row)
    {
	ArrayList<Node> numRow = new ArrayList<Node>();
	ArrayList<Path> pathsToAdd = new ArrayList<Path>();

	for(int i=0;i<nodes.size();i++)
	    if(nodes.get(i).getRow()==row)
		numRow.add(nodes.get(i));

	for(int i=0;i<numRow.size();i++)
	{
	    Node n = numRow.get(i);
	    //If n has no parents or dependents then we don't care about it
	    if(n.getNumParent()==0 || n.getNumDepend()==0)
		continue;
	    //System.out.println(n.getName());
	    //get minimum path where n is end node
	    Path p = null,temp;
	    for(int j=0;j<n.getNumParent();j++)
	    {
		temp = getPath(n.getParent(j).getName(),n.getName());
		if(p==null || p.getWeight()>temp.getWeight())
		{
		    p=temp;
		}
		paths.removeFromBucket(temp.getA().getName(),temp.getB().getName());
	    }

	    //create the new paths and store them in separate array
	    for(int j=0;j<n.getNumDepend();j++)
	    {
		temp = getPath(n.getName(),n.getDepend(j).getName());
		//System.out.println(n.getName() + " " + n.getDepend(j).getName() + " " + temp==null);
		String newName = (p.getName().equals("")?n.getName():p.getName()+","+n.getName());
		pathsToAdd.add(new Path(newName,p.getA(),temp.getB(),p.getWeight()+temp.getWeight()));
		paths.removeFromBucket(temp.getA().getName(),temp.getB().getName());
	    }
	}

	for(int i=0;i<pathsToAdd.size();i++)
	{
	    Path p = pathsToAdd.get(i), temp = getPath(p.getA().getName(),p.getB().getName());
	    if(temp==null)
	    {
		addPath(p);
	    }
	    else
	    {
		if(temp.getWeight()>p.getWeight())
		{
		    paths.removeFromBucket(temp.getA().getName(),temp.getB().getName());
		    paths.addToBucket(p.getA().getName(),p.getB().getName(),p);
		}
	    }
	}
    }

    //Need to redo - Fixed
    public Map newInstance()
    {
	Map m = new Map();
	m.setStartNode(new Node("START",startNode.getRunner()));
	for(int i=1;i<nodes.size()-1;i++)
	{
	    Node n = nodes.get(i);
	    m.addNode(new Node(n.getName(),n.getWeight(),n.getRunner()));
	    //System.out.println(nodes.get(i).getNumDepend());
	}
	m.setEndNode(new Node("END",endNode.getRunner()));
	/*for(int i=0;i<paths.size();i++)
	{
	    Path p = paths.get(i);
	    m.addPath(new Path(m.getFromName(p.getA().getName()),m.getFromName(p.getB().getName()),p.getWeight()));
	}*/
	Iterator<HashMap<String,Path>> it1 = paths.bucketIterator();
	while(it1.hasNext())
	{
	    Iterator<Path> it2 = it1.next().values().iterator();
	    while(it2.hasNext())
	    {
		Path p = it2.next();
		m.addPath(new Path(m.getFromName(p.getA().getName()),m.getFromName(p.getB().getName()),p.getWeight()));
	    }
	}
	return m;
    }
}