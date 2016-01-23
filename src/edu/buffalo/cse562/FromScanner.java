package edu.buffalo.cse562;

import java.io.File;
import java.util.HashMap;







import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;

public class FromScanner implements FromItemVisitor {
	private File mBasePath;
	private HashMap<String, CreateTable> mTables;
	private SqlIterator mInput;

	public FromScanner(File mBasePath, HashMap<String, CreateTable> mTables) {
		super();
		this.mBasePath = mBasePath;
		this.mTables = mTables;
	}

	@Override
	public void visit(Table table) {
		
		//wrong
		CreateTable ct = mTables.get(table.getName());
		mInput = new FromIterator(mBasePath, ct,table.getAlias());
	}

	@Override
	public void visit(SubSelect subSelect) {
		
		SelectBodyScanner selectBodyScanner = new SelectBodyScanner(mBasePath, mTables);
		subSelect.getSelectBody().accept(selectBodyScanner);
		Table tb = new Table(null, subSelect.getAlias());
		tb.setAlias(subSelect.getAlias());
		for (Column cl  : selectBodyScanner.getIterator().getInputSchema()) {
			cl.setTable(tb);
		}
		mInput = selectBodyScanner.getIterator();
		
		
	}

	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		System.err.println("PANIC! SubJoin not finished!");
	}

	public SqlIterator getIterator() {
		return mInput;
	}
}
