package edu.buffalo.cse562;

import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

public class SelectScanner implements SelectItemVisitor {
	private List<Column> mSelectColums;
	private List<String> mColumAlias;
	private HashMap<String, CreateTable> mTables;
	
	public SelectScanner(List<Column> selectColums,List<String>columAlias, HashMap<String, CreateTable> tables) {
		super();
		this.mSelectColums = selectColums;
		this.mColumAlias = columAlias;
		this.mTables = tables;
	}

	@Override
	public void visit(AllColumns arg0) {
		// TODO Auto-generated method stub
		System.err.println("PANIC! AllColumns implement outside Scanner, there must be somthing wrong!");
	}

	@Override
	public void visit(AllTableColumns atc) {
		CreateTable ct = mTables.get(atc.getTable().getName());
		for (Object o : ct.getColumnDefinitions()) {
			ColumnDefinition cd = (ColumnDefinition)o;
			Column cl = new Column(atc.getTable(), cd.getColumnName());
			mSelectColums.add(cl);	
			mColumAlias.add(null);
		}
		
		
	}

	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		if (selectExpressionItem.getExpression() instanceof Column) {
			
			Column cl = (Column) selectExpressionItem.getExpression();
			cl.getTable().setAlias(cl.getTable().getName());
			
			mSelectColums.add(cl);	
			mColumAlias.add(selectExpressionItem.getAlias());
		}
		
	}
	public List<Column> getSelectColumns() {
		return mSelectColums;
	}
}
