package edu.buffalo.cse562;

import java.util.List;

import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;

public class SelectIterator implements SqlIterator {

	private SqlIterator mInput;
	private Column[] mSchema;
	private Column[] mNewSchema;
	private List<String> mAlias;
	
	public SelectIterator(SqlIterator mInput, List<Column> selectColum,List<String> aLias) {
		super();
		this.mInput = mInput;
		this.mSchema = mInput.getInputSchema();
		this.mNewSchema = new Column[selectColum.size()];
		this.mAlias = aLias;
		for (int i = 0; i < selectColum.size(); i++) {
			this.mNewSchema[i] = selectColum.get(i);
		}
	}

	@Override
	public LeafValue[] readNextTuple() {
		int[] index = new int[mNewSchema.length];
		/*
		int j=0;
		for (int i = 0; i < mSchema.length && j<mNewSchema.length; i++) {
			if (
					(mSchema[i].getTable().getAlias().equals(mNewSchema[j].getTable().getAlias())
							||
							mNewSchema[j].getTable().getAlias() == null)
					&&
					mSchema[i].getColumnName().equals(mNewSchema[j].getColumnName())  
					 ){
				index[j] = i;
				j++;
			}
		}
		*/
		
		for (int j = 0; j < index.length; j++) {
			for (int i = 0; i < mSchema.length && j<mNewSchema.length; i++) {
				if (
						(mSchema[i].getTable().getAlias().equals(mNewSchema[j].getTable().getAlias())
								||
								mNewSchema[j].getTable().getAlias() == null)
						&&
						mSchema[i].getColumnName().equals(mNewSchema[j].getColumnName())  
						 ){
					index[j] = i;
					continue;
				}
			}
		}
		
		LeafValue[] oldTuple = mInput.readNextTuple();
		if (oldTuple == null) {
			//System.err.println("oldTuple null");
			return null;
		}
		LeafValue[] newTuple = new LeafValue[index.length];
		for (int i = 0; i < index.length; i++) {
			newTuple[i] = oldTuple[index[i]];
		}
		return newTuple;
	}

	@Override
	public void reset() {
		mInput.reset();

	}

	@Override
	public Column[] getInputSchema() {
		Column[] aliasSchema = new Column[this.mNewSchema.length];
		for (int i = 0; i < aliasSchema.length; i++) {
			aliasSchema[i] = new Column(this.mNewSchema[i].getTable(), mAlias.get(i));
		}
		
		return aliasSchema;
	}

}
