package algorithm;

import java.io.PrintStream;
import java.util.ArrayList;

import algorithm.Matrix.*;

public class BasicShape
{
	@SuppressWarnings("serial")
	public static class BadNumberOfRowsException extends IllegalArgumentException
	{
		public BadNumberOfRowsException() {super(); }
		
		public BadNumberOfRowsException (String message) { super (message); }
	}
	
	@SuppressWarnings("serial")
	public static class BadNumberOfCollumsException extends IllegalArgumentException
	{
		public BadNumberOfCollumsException() {super(); }
		
		public BadNumberOfCollumsException (String message) { super (message); }
	}
	
	@SuppressWarnings("unchecked")
	public BasicShape(ArrayList <IntegerMatrix> vectors, IntegerMatrix adjMatrix){

		this.vectors = (ArrayList<IntegerMatrix>) vectors.clone();
		if (!numberOfCols(vectors)) 
			throw new BadNumberOfCollumsException ("The vectors introduced are not 3x1");
		dimensions = new ArrayList<Integer>();
		calcDim (vectors);
		this.adjMatrix = adjMatrix.clone();
	}
	
	/** Creates a rotation matrix based on given angles of rotation
	 * @param angle1 Desired amount of rotation in x2 axis (in degrees)
	 * @param angle2 Desired amount of rotation in x3 axis (in degrees)
	 * @return rotation matrix
	 */
	public static Matrix<Double> rotationMatrix(double angle1, double angle2){
		double radAngle1 = Math.toRadians (angle1);
		double radAngle2 = Math.toRadians (angle2);
		//rotation matrix for y axis
		Matrix.DoubleMatrix rotationMatrix1 = new Matrix.DoubleMatrix (3, 3);
		rotationMatrix1.setCell (0, 0, Math.cos (radAngle1));
		rotationMatrix1.setCell (2, 0, -Math.sin (radAngle1));
		rotationMatrix1.setCell (1, 1, 1.0);
		rotationMatrix1.setCell (0, 2, Math.sin (radAngle1));
		rotationMatrix1.setCell (2, 2, Math.cos (radAngle1));
		//rotation matrix for z axis
		Matrix.DoubleMatrix rotationMatrix2 = new Matrix.DoubleMatrix (3, 3);
		rotationMatrix2.setCell (0, 0, Math.cos (radAngle2));
		rotationMatrix2.setCell (1, 0, Math.sin (radAngle2));
		rotationMatrix2.setCell (0, 1, -Math.sin (radAngle2));
		rotationMatrix2.setCell (1, 1, Math.cos (radAngle2));
		rotationMatrix2.setCell (2, 2, 1.0);

		return rotationMatrix1.multiply (rotationMatrix2, new Matrix.DoubleMatrix (3, 3));
	}
	
	/**
	 * construct basic shape by copying clone
	 * @param clone another basic shape
	 */
	public BasicShape (BasicShape clone)
	{
		this (clone.vectors, clone.adjMatrix);
	}
	
	/**
	 * @param index index of point to look up connections for
	 * @return array list containing vectors to points connected to point at index
	 */
	public ArrayList <IntegerMatrix> lookUpConnections(int index){

		ArrayList<IntegerMatrix> connections = new ArrayList<IntegerMatrix>();
		for(int counter=0; counter<adjMatrix.getRows(); counter++){
			if(adjMatrix.getCell(index,counter)!=0){
					connections.add (vectors.get(counter));
			}
		}
		return connections;
	}
	
	/**
	 * @param index index of vertex
	 * @return vertex at index
	 */
	public IntegerMatrix getVertex (int index)
	{
		return vectors.get(index);
	}
	
	/**
	 * @param minPos the position at which this blocks upper left corner is sitting
	 * @return the maxPos, which is equivalent to the position of the bottom right corner
     */
	public Position getMaxDimension(Position minPos) 
	{
		ArrayList<Integer> maxPos = new ArrayList<Integer>();
		maxPos.add(getDimensions(0) + minPos.getPosition().get(0));
		maxPos.add(getDimensions(1) + minPos.getPosition().get(1));
		maxPos.add(getDimensions(2) + minPos.getPosition().get(2));
		Position max = new Position(maxPos);
		return max;
	}
	
	/** calculates the maximum vector value
	* @param vector ArrayList containing all the vectors
	* @param index The index of the vector in the Matrix Handler
	* @return the maximum value.
	*/
	public int maximum(ArrayList <IntegerMatrix> vectors, int index){

		int max = Integer.MIN_VALUE;
    	for(Matrix<Integer> temp : vectors){
       		if(temp.getCell (index, 0) > max){
          		  max = temp.getCell (index, 0);
       		}
   		}
    	return max;

	}
	/** calculates the minimum vector value
	* @param vector ArrayList containing all the vectors
	* @param index The index of the vector in the Matrix Handler
	* @return the minimum value.
	*/
	public int minimum(ArrayList<IntegerMatrix> vectors, int index){

		int min = Integer.MAX_VALUE;
    	for(Matrix<Integer> temp: vectors){
       		if(temp.getCell (index, 0) < min){
          		  min= temp.getCell (index, 0);
       		}
   		}
    	return min;
	}
	
	/** compares that all the Matrix Handlers have the same number of rows
	* @param vectors ArrayList containing all the vectors
	* @return false if one Matrix Handler doesn't have the same number of rows
	*/
	public boolean numberOfMH(ArrayList<IntegerMatrix> vectors){

		int numberOfRows=vectors.get(0).getRows();
		for(Matrix<Integer> temp: vectors){
			if(temp.getRows() != numberOfRows)
				return false;
		}
		return true;
	}
	
	/**
	 * @param vectors set of vectors
	 * @return true if all vectors have the same number of rows
	 */
	public boolean numberOfCols(ArrayList<IntegerMatrix> vectors){

		int numberOfCols = 0;
		for(Matrix<Integer> temp: vectors){
			if (numberOfCols == 0)
				numberOfCols = temp.getColumns();
			else if(temp.getColumns() != numberOfCols)
				return false;
		}
		return true;
	}
	
	/** @return the dimensions of a shape given an index.
	 */
	public int getDimensions(int index){

		return dimensions.get(index);
	}
	
	/**
	 * @return number of vertices defining the shape
	 */
	public int getNumberOfVertices()
	{
		return vectors.size();
	}
	
	/**
	 * @param vertex vertex to search index for
	 * @return index of vertex or vectors.size() if vertex was not found
	 */
	public int getVertexIndex (IntegerMatrix vertex)
	{
		for (int cVertex = 0; cVertex < vectors.size(); ++cVertex)
		{
			if (vectors.get(cVertex).equals(vertex))
				return cVertex;
		}
		return vectors.size();
	}
	
	/**
	 * Expands this shape by adding vectors of bs and connecting bs' vertices with existing ones
	 * @param bs shape to add to this shape
	 */
	public void addShape (BasicShape bs)
	{
		int prevNumVertices = getNumberOfVertices();
		addVertices (bs.vectors);
		IntegerMatrix newAdjMat = new IntegerMatrix (vectors.size(), vectors.size());
		newAdjMat.copyValues(adjMatrix, 0, 0, 0, 0, adjMatrix.getRows(), adjMatrix.getColumns());
		for (int cVertex = prevNumVertices; cVertex < getNumberOfVertices(); ++cVertex)
		{
			int indBSVertex = bs.getVertexIndex(getVertex (cVertex));
			ArrayList <IntegerMatrix> connected = bs.lookUpConnections (indBSVertex);
			
			for (IntegerMatrix connection : connected)
			{
				int connectionIndex = this.getVertexIndex (connection);
				newAdjMat.setCell(cVertex, connectionIndex, 1);
				newAdjMat.setCell(connectionIndex, cVertex, 1);
			}
		}
		adjMatrix = newAdjMat;
	}
	
	/** Calculates the dimensions of a shape
	** @param vectors ArrayList containing all the vectors
	*/
	public void calcDim(ArrayList<IntegerMatrix> vectors) throws BadNumberOfRowsException{
		
		if (!numberOfMH(vectors)) 
			throw new BadNumberOfRowsException ("vectors don't have the same dimension");

		for(int i=0; i<vectors.size(); i++){

			int max = maximum (vectors,i);
			int min = minimum (vectors,i);
			dimensions.add(max-min);

		}
	}
	
	/** Performs actual rotation
	 * @param rotMatrix created from rotationMatrix()
	 * @return matrix after rotation
	 */
	public void rotate (Matrix<Double> rotMatrix){

		for(int cCounter=0; cCounter<vectors.size();cCounter++){
			Matrix.DoubleMatrix result = new Matrix.DoubleMatrix (3,1);
			Matrix.DoubleMatrix vec = vectors.get(cCounter).toDoubleMatrix();
			rotMatrix.multiply (vec, result);
			vectors.set (cCounter, result.toIntegerMatrix());
		}

	}
	
	
	public void print(PrintStream p)
	{
		p.println ("Printing vertices of basic shape");
		for (int cVec = 0; cVec < getNumberOfVertices(); ++cVec)
		{
			p.println ("vector");
			vectors.get(cVec).print(System.out);
			p.print("connections: ");
			for (int cConnect = 0; cConnect < getNumberOfVertices(); ++cConnect)
			{
				if (adjMatrix.getCell (cVec, cConnect).equals(1))
					p.print (cConnect + ", ");
			}
		}
	}
	
	/**
	 * Adds newVertices to vectors in the exact order, removing duplicates
	 * @param newVertices set of vectors being vertices to add
	 */
	private void addVertices (ArrayList <IntegerMatrix> newVertices)
	{
		for (int cBSVertex = 0; cBSVertex < newVertices.size(); ++cBSVertex)
		{
			int cVertex = getVertexIndex(newVertices.get (cBSVertex));
			if (cVertex == vectors.size())
				vectors.add (newVertices.get(cBSVertex).clone());
		}
	}
	
	private ArrayList<IntegerMatrix> vectors;
	private ArrayList<Integer> dimensions;
	private IntegerMatrix adjMatrix;
}