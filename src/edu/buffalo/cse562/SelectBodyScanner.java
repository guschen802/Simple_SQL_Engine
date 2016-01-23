package edu.buffalo.cse562;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.Union;

public class SelectBodyScanner implements SelectVisitor {
	private File mDataDir;
	private HashMap<String, CreateTable> mTables;
	private SqlIterator mInput =null;
	public SelectBodyScanner(File dataDir, HashMap<String, CreateTable> tables) {
		super();
		this.mDataDir = dataDir;
		this.mTables = tables;
	}

	@Override
	public void visit(PlainSelect ps) {
		//From
		FromScanner fromScan = new FromScanner(mDataDir, mTables);
		//System.out.println(ps.getFromItem());
		ps.getFromItem().accept(fromScan);
		mInput = fromScan.getIterator();
		
		if (ps.getJoins()!=null) {
			//System.out.println(ps.getJoins());
			for (Object o : ps.getJoins()) {
				SqlIterator left = mInput;
				SqlIterator right = null;
				FromScanner fromScan2 = new FromScanner(mDataDir, mTables);
				Join jo = (Join)o;
				jo.getRightItem().accept(fromScan2);
				right = fromScan2.getIterator();
				mInput = new SimpleJoinIterator(left, right);	
				//on expression
				if (jo.getOnExpression()!=null) {
					mInput = new ProjectionIterator(mInput, jo.getOnExpression());
				}
			}		
		}
		//where
		if(ps.getWhere() != null){
			mInput = new ProjectionIterator(mInput, ps.getWhere());
		}
		
		//select
		//TO-DO: need modified!!!
		
		if (ps.getSelectItems().get(0) instanceof AllColumns) {
			
		}else {
			List<Column> seList = new ArrayList<Column>();
			List<String> aLias = new ArrayList<String>();
			
			for(Object o : ps.getSelectItems()){
				SelectScanner selectScanner = new SelectScanner(seList,aLias,mTables);
				SelectItem si = (SelectItem)o;
				si.accept(selectScanner);
			}
			mInput = new SelectIterator(mInput, seList,aLias);
		}
		
		/*
		for (Column col : seList) {
			System.out.print(col.toString() + "|");
		}
		System.err.println("");
		*/
	}

	@Override
	public void visit(Union arg0) {
		// TODO Auto-generated method stub
		System.err.println("TO-DO: Union not finished.");

	}
	
	public SqlIterator getIterator(){
		return this.mInput;
	}

}
