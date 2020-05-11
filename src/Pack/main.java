package Pack;

import java.io.IOException;

public class main 
{

	public static void main(String[] args) throws Exception 
	{
		Btree x =new Btree();
		x.createIndexFile();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(5);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(3);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(21);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(9);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(1);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(13);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(2);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(7);
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		x.Insert(10);

		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
		
		x.delete("ttt.bin", 2 );
		System.out.println();
		x.DisplayIndexFileContent("ttt.bin");
	}

}
