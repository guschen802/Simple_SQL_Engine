package edu.buffalo.cse562;

import java.sql.SQLException;

import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;

public class Evaluator extends Eval{
	private Column[] mCols;
	private LeafValue[] mTuple;
	public Evaluator(Column[] cols,LeafValue[] tuple ) {
		super();
		// TODO Auto-generated constructor stub
		mCols = cols;
		mTuple = tuple;
	}

	@Override
	public LeafValue eval(Column column) throws SQLException {
		// TODO Auto-generated method stub
		int i=0;
		for(i = 0;i<mCols.length;i++){
			if(
					(mCols[i].getTable().getAlias().equals(column.getTable().getName())
							||
							column.getTable().getName() == null)
					&&
					mCols[i].getColumnName().equals(column.getColumnName())
					){
				break;
			}
		}	
		
		if (i < mCols.length) {
			return mTuple[i];
		}else {
			return null;
		}
		
	}
	
}
