package mwpf;

public class Helper
{
	public static int min(Comparable[] arr)
	{
		int ret=0;
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i].compareTo(arr[ret])==-1)
			{
				ret=i;
			}
		}
		return ret;
	}
	
	public static int max(Comparable[] arr)
	{
		int ret=0;
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i].compareTo(arr[ret])==1)
			{
				ret=i;
			}
		}
		return ret;
	}
	
	public static boolean contains(Comparable[] arr, Comparable search)
	{
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i].compareTo(search)==0)
			{
				return true;
			}
		}
		return false;
	}
}
