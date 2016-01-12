package test.algorithmTest;

import algorithm.*;
import algorithm.Matrix.*;

import java.util.ArrayList;

public class TestBS 
{
	
	public static void main(String[] args)
	{ 	
		TestBS test = new TestBS();
		//error test.testRotation();
		test.testAdding();
	}
	
	public static BasicShape constructTriangle (IntegerMatrix v1, IntegerMatrix v2, IntegerMatrix v3)
	{
		ArrayList <IntegerMatrix> vertices = new ArrayList<Matrix.IntegerMatrix>();
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		IntegerMatrix adjMat = new IntegerMatrix (3, 3);
		for (int cRow = 0; cRow < adjMat.getRows(); ++cRow)
		{
			for (int cCol = 0; cCol < adjMat.getRows(); ++cCol)
			{
				if (cRow != cCol)
					adjMat.setCell(cRow, cCol, 1);
			}
		}
		return new BasicShape (vertices, adjMat);
	}
	
	public TestBS()
	{
		//0, 0, 0
		Matrix.IntegerMatrix v1 = new Matrix.IntegerMatrix(3, 1);
		//1, 0, 0
		Matrix.IntegerMatrix v2 = new Matrix.IntegerMatrix(3, 1);
		v2.setCell(0, 0, 1);
		//0, 1, 0
		Matrix.IntegerMatrix v3 = new Matrix.IntegerMatrix(3, 1);
		v3.setCell(1, 0, 1);
		
		mBs = constructTriangle (v1, v2, v3);
	}
	
	public void testRotation()
	{
		System.out.println ("Before rotation");
		mBs.print (System.out);
		Matrix<Double> rotMat = BasicShape.rotationMatrix(90.0, 90.0);
		mBs.rotate (rotMat);
		System.out.println ("After rotation");
		mBs.print(System.out);
		Matrix<Double> rotBackMat = BasicShape.rotationMatrix(-90.0, -90.0);
		mBs.rotate (rotBackMat);
		System.out.println ("Undoing rotation");
		mBs.print(System.out);
	}
	
	public void testAdding()
	{
		IntegerMatrix v1, v2, v3;
		v1 = new IntegerMatrix (3, 1);
		v2 = new IntegerMatrix (3, 1);
		v3 = new IntegerMatrix (3, 1);
		
		v2.setCell (0, 0, -1);
		v3.setCell (1, 0, -1);
		
		BasicShape addShape = constructTriangle (v1, v2, v3);
		System.out.println ("Trying to add");
		addShape.print(System.out);
		
		System.out.println ("Before adding");
		mBs.print (System.out);
		mBs.addShape (addShape);
		System.out.println ("After adding");
		mBs.print (System.out);
	}
	
	private BasicShape mBs;
}