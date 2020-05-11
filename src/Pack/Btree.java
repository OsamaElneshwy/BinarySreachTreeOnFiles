package Pack;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

import javax.crypto.spec.PSource;
import javax.swing.text.Position;

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
	
	public int search(String fileName,int num) throws Exception
	{
		int position = 40, Node;
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
		indexFile.seek(position);
		
		while(true)
		{
			indexFile.seek(position);
			int index = indexFile.readInt();
			if(num == index)
			{
				indexFile.close();
				return position;
			}
			else if (num < index)
			{
				position+=4;
				indexFile.seek(position);
				Node = indexFile.readInt();
				if(Node == -1)
				{
					indexFile.close();
					return -1;
				}
				position=((Node*36)+4);
			}
			else if(num > index)
			{ 
				if(index == -1)
				{
					indexFile.close();
					return -1;
				}
				position+=8;
				
			}		
		}	
	}
	
	public void delete(String fileName,int num ) throws Exception
	{	
		int position = search(fileName, num);
		int  Node;
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
		if(position != -1 )
		{
			indexFile.seek(position+4);
			Node = indexFile.readInt();
			if(Node == -1)
			{
				indexFile.seek(position);
				indexFile.writeInt(-1);
				
			}
			
			
		}
		else
		{
			System.out.println("This number cannot be found");
			DisplayIndexFileContent(fileName);
		}
			
		
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
			InsertIndex("ttt.bin",position,num,EmptyNode);
		}
		else if(checkLeaf == 1)
		{
			int flag = 0;
			indexFile.seek(position);
			int start = indexFile.readInt();
			
			while( start < num )
			{
				position+=8;
				indexFile.seek(position);
				start = indexFile.readInt();
				if(start == -1)
				{
					flag=1;
					break;
				}
			}
			if(flag == 0)
			{
				position+=4;
				indexFile.seek(position);
				int childNode = indexFile.readInt();
				
				InsertIndex("ttt.bin",childNode*36+4,num,EmptyNode);
			}
			else if(flag == 1)
			{
				position-=8;
				indexFile.seek(position);
				indexFile.writeInt(num);
				
				position+=4;
				indexFile.seek(position);
				int childNode = indexFile.readInt();
				InsertIndex("ttt.bin",childNode*36+4,num,EmptyNode);
			}
		}
	}
	
	public Vector<Integer> arrangeRow(String fileName,int p,int num) throws Exception
	{
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
		int count =0;
		Vector<Integer> vec = new Vector<Integer>();
		vec.add(num);
		while( count < 4 )
		{
			indexFile.seek(p);
			int a =indexFile.readInt();
			vec.add(a);
			p+=8;
			count++;
		}
		Collections.sort(vec);
		return vec;
	}
	
	public void editChild(String fileName,int p, Vector<Integer> vec,int start,int end) throws Exception
	{
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
		p+=4;
		for(int i=start; i < end;i++)
		{
			indexFile.seek(p);
			indexFile.writeInt(vec.get(i));
			p+=8;
		}
	}
	
	public void InsertIndex(String fileName,int position,int num , int EmptyNode) throws Exception
	{
		RandomAccessFile indexFile = new RandomAccessFile(fileName, "rw");
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
			Vector<Integer> v = arrangeRow("ttt.bin",position,num);
			
			position-=4;
			indexFile.seek(position);
			int flag1 = indexFile.readInt();
			position+=4;
			if( flag1 == 0 && position == 40 )
			{
				indexFile.seek(position-4);
				indexFile.writeInt(1);
				
				indexFile.seek(position);
				indexFile.writeInt(v.get(v.size()/2));
				
				position+=4;
				indexFile.seek(position);
				indexFile.writeInt(EmptyNode);	
				indexFile.seek(EmptyNode*36);
				indexFile.writeInt(0);
				editChild("ttt.bin",EmptyNode*36,v,0,(v.size()/2)+1);
				
				EmptyNode++;
				
				position+=4;
				indexFile.seek(position);
				indexFile.writeInt(v.get(v.size()-1));
				
				position+=4;
				indexFile.seek(position);
				indexFile.writeInt(EmptyNode);
				indexFile.seek(EmptyNode*36);
				indexFile.writeInt(0);
				editChild("ttt.bin",EmptyNode*36,v,(v.size()/2)+1,v.size());
				
				for(int i = 0; i <4;i++)
				{
					position+=4;
					indexFile.seek(position);
					indexFile.writeInt(-1);
				}
				
				EmptyNode++;
				
				indexFile.seek(4);
				indexFile.writeInt(EmptyNode);
			}
			else if (flag1 == 0 && position != 40)
			{	
				indexFile.seek(EmptyNode*36);
				indexFile.writeInt(0);
				editChild("ttt.bin",EmptyNode*36,v,0,(v.size()/2)+1);
				
				EmptyNode--;
				indexFile.seek(EmptyNode*36);
				indexFile.writeInt(0);
				editChild("ttt.bin",EmptyNode*36,v,(v.size()/2)+1,v.size());
				
				
				position+=(v.size()-(v.size()/2)+1)*4;
				for(int i = 0; i <4;i++)
				{		
					indexFile.seek(position);
					indexFile.writeInt(-1);
					position+=4;
				}
				
				position-=(EmptyNode)*36;
				Vector<Integer> v1 = arrangeRow("ttt.bin",position+4,num);
				editChild("ttt.bin",position,v1,v1.size()/2,v1.size());
				
				indexFile.seek(position+16);
				indexFile.writeInt(4);
				
				indexFile.seek(position+24);
				indexFile.writeInt(3);
				
				indexFile.seek(4);
				indexFile.writeInt(-1);
			}
		}
	}
	
	
}
