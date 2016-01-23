package edu.buffalo.cse562;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class FromIterator implements SqlIterator {
	private BufferedReader mInput;
	private File mFile;
	private ColumnDefinition[] mSchema;
	private Column[] inputSchema;
	public FromIterator(File dataPatgh, CreateTable sourceTable,String aLias) {
		
		this.mFile = new File(dataPatgh,sourceTable.getTable().getName() + ".dat");
		mSchema = new ColumnDefinition[sourceTable.getColumnDefinitions().size()];
		inputSchema = new Column[sourceTable.getColumnDefinitions().size()];
		
		for (int i =0; i<sourceTable.getColumnDefinitions().size();i++){
			ColumnDefinition cd = (ColumnDefinition) sourceTable.getColumnDefinitions().get(i);
			mSchema[i] = cd;
			Table tb = new Table(null, sourceTable.getTable().getName());
			if (aLias !=null) {
				tb.setAlias(aLias);
			}else {
				tb.setAlias(sourceTable.getTable().getName());
			}
			inputSchema[i] = new Column(tb,cd.getColumnName());
		}
		reset();
	}

	@Override
	public LeafValue[] readNextTuple() {
		if (mInput == null) {	
					return null;
				}
		String line = null;
		try {
			line = mInput.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (line == null) {
			return null;
		}
		String cols[] = line.split("\\|");
		LeafValue[] tuple = new LeafValue[mSchema.length];
		for(int i=0;i<mSchema.length;i++){
			if (mSchema[i].getColDataType().getDataType().equals("string") 
					|| mSchema[i].getColDataType().getDataType().equals("char") 
					|| mSchema[i].getColDataType().getDataType().equals("varchar")) {
				tuple[i] = new StringValue(" " + cols[i] + " ");		
			}else if (mSchema[i].getColDataType().getDataType().equals("int")) {
				tuple[i] = new LongValue(cols[i]);
			}else if (mSchema[i].getColDataType().getDataType().equals("decimal")) {
				tuple[i] = new DoubleValue(cols[i]);
			}else if (mSchema[i].getColDataType().getDataType().equals("date")) {
				tuple[i] = new DateValue(" " + cols[i] + " ");
			}
		}
		
		return tuple;
	}

	@Override
	public void reset() {
		try {
			mInput = new BufferedReader(new FileReader(this.mFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mInput = null;
		}

	}

	@Override
	public Column[] getInputSchema() {
		return inputSchema;
	}

}
