package Pack;

import java.io.IOException;

public class main 
{

	public static void main(String[] args) throws Exception 
	{
		Btree x =new Btree();
		x.createIndexFile();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(1);
		x.Insert(3);
		x.Insert(5);
		x.Insert(7);
		x.Insert(2);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");

	}

}
