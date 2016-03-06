package testing.algoTest;

import gui.PieceRenderPanel;

import java.util.*;
import java.io.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import algorithm.*;
import models.*;
import models.Matrix.*;

public class DynamicAlgoTest 
{
	public static void main (String[] args) throws IOException, ShapeParser.BadFileStructureException
	{
		int[] quants = {2, 3, 1};
		boolean[] inf = {false, false, false};
		int d = 5, w = 5, h = 5;

		ArrayList <Resource> res = new ArrayList<>();
		String fileName = "parcels.txt";
		ShapeParser input = new ShapeParser (new File (fileName));
		input.parse();
		
		DynamicAlgoTest test = new DynamicAlgoTest();
		test.setResources (input.getBlocks(), quants, inf);
		test.setContainer (d, w, h);
		test.run();
		test.drawResult();
		
	}
	
	public void setResources (ArrayList<Block> blocks, int[] quants, boolean[] infFlags)
	{
		mRes = new ArrayList<>();
		for (int cBlock = 0; cBlock < blocks.size(); ++cBlock)
		{
			Block b = blocks.get (cBlock);
			mRes.add (new Resource (b, quants[cBlock], b.getVolume(), infFlags[cBlock]));
		}
	}
	
	public void setContainer (int d, int w, int h)
	{
		mCont = new Container (d, w, h);
	}
	
	public void run()
	{
		mAlgo = new DynamicAlgo();
		mAlgo.init (mCont, mRes);
		mAlgo.run();
	}
	
	public void printSubsets()
	{
		DynamicAlgo algo = new DynamicAlgo();
		algo.init (mCont, mRes);
		algo.generatePowerSet();
		System.out.println ("subsets generated " + algo.mSubsets.size());
		for (DynamicAlgo.Subset subs : algo.mSubsets)
			System.out.println (subs);
	}
	
	public void drawResult()
	{
		JFrame frame = new JFrame ("result");
		frame.setSize (400, 400);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.setLayout (new BorderLayout());
		
		PieceRenderPanel render = new PieceRenderPanel(mCont);
		
		PieceRenderPanel.RotationListener rotListen = render.new RotationListener();
		PieceRenderPanel.ResizeListener resizeListen = render.new ResizeListener();
		PieceRenderPanel.ZoomListener zoomListen = render.new ZoomListener();
		
		frame.addMouseListener (rotListen);
		frame.addMouseMotionListener(rotListen);
		frame.addMouseWheelListener(zoomListen);
		frame.addComponentListener(resizeListen);
		
		frame.add (render, BorderLayout.CENTER);
		frame.setVisible(true);
		render.init();
	}
	
	private ArrayList<Resource> mRes;
	private Container mCont;
	private DynamicAlgo mAlgo;
}