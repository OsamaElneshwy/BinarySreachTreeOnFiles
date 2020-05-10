package Pack;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Btree 
{

	public void createIndexFile() throws Exception
	{
		System.out.print("Enter your File Name:");
		Scanner sc = new Scanner(System.in);
		//String name = sc.nextLine();
		
		RandomAccessFile indexFile = new RandomAccessFile("ttt.bin", "rw");
				
		for( int i=0 ; i<5 ; i++ )
		{
			for( int j=0 ; j < 9 ; j++ )
			{
				if( j == 1 )
				{
					indexFile.writeInt(i+1);
				}
				else
				{
					indexFile.writeInt(-1);
				}
			}
		}
		indexFile.close();
		System.out.println("Creation Done");
	}
	
	
	public void Insert(int num) throws Exception
	{
		int position=36;
		
		RandomAccessFile indexFile = new RandomAccessFile("ttt.bin", "rw");
		
		indexFile.seek(4);
		int EmptyNode = indexFile.readInt();

		indexFile.seek((position));
		int checkLeaf = indexFile.readInt();
		position+=4;
		
		if( EmptyNode == 1 )
		{
			indexFile.seek(EmptyNode*36);
			indexFile.writeInt(0);
			
			indexFile.seek((EmptyNode*36)+4);
			indexFile.writeInt(num);
			
			indexFile.seek(4);
			indexFile.writeInt(EmptyNode+1);
		}
		
		if(checkLeaf == 0)
		{
			indexFile.seek(position+24);
			if(indexFile.readInt() == -1 )
			{
				int nodeCounter=0;
				indexFile.seek(position);
				int checkIndex = indexFile.readInt();
				while(checkIndex != -1 && checkIndex < num  && nodeCounter < 4 )
				{
					position+=8;
					indexFile.seek(position);
					checkIndex = indexFile.readInt();
					nodeCounter++;
				}
				if(checkIndex > num && nodeCounter < 4)
				{
					int limit = nodeCounter , LValue,L1 , NValue=num ,N1=-1 ;

					while( limit < 4 )
					{
						indexFile.seek(position);
						LValue = indexFile.readInt();
						
						position+=4;
						indexFile.seek(position);
						L1 = indexFile.readInt();
						position-=4;
						
						indexFile.seek(position);
						indexFile.writeInt(NValue);
						
						position+=4;
						indexFile.seek(position);
						indexFile.writeInt(N1);
						
						NValue=LValue;
						N1=L1;
						
						position+=4;
						limit++;
					}
				}
				else if(nodeCounter < 4)
				{
					indexFile.seek(position);
					indexFile.writeInt(num);
				}
			}
			else
			{
				//******************
			}
			
		}
		
	}
	
	
	public void DisplayIndexFileContent(String fileName) throws Exception
	{
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
		indexFile.seek(0);
        for( int i=0 ; i<5 ; i++ )
		{
			for( int j=0 ; j<9 ; j++ )
			{
				  System.out.print(indexFile.readInt());
			}
			System.out.println();
		}
    }
	
}
