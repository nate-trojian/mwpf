package mwpf;

public class PathResult
{
	protected String path;
	protected double weight;
	public PathResult()
	{
		path="";
		weight=0;
	}

	public PathResult(String p, double w)
	{
		path=p;
		weight=w;
	}

	public String getPath()
	{
		return path;
	}

	public double getWeight()
	{
		return weight;
	}

	public void setPath(String p)
	{
		path=p;
	}

	public void setWeight(double w)
	{
		weight=w;
	}

	public PathResult append(PathResult pr)
	{
		return new PathResult(path+pr.getPath(),weight+pr.getWeight());
	}

	public String toString()
	{
		return path + " " + weight;
	}
}
