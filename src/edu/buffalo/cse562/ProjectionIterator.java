package edu.buffalo.cse562;

import java.sql.SQLException;

import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;

public class ProjectionIterator implements SqlIterator {
	private SqlIterator mInput;
	private Column[] mSchema;
	private Expression mCondition;
	
	
	public ProjectionIterator(SqlIterator input,Expression condition) {
		mInput = input;
		mSchema = mInput.getInputSchema();
		mCondition = condition;
	}

	@Override
	public LeafValue[] readNextTuple() {	
		LeafValue[] tuple = null;
		do {
			tuple = mInput.readNextTuple();
			if (tuple == null) {
				return null;
			}
			Evaluator eval = new Evaluator(mSchema, tuple);
			try {
				
				//System.err.println(mCondition);
				if (eval.eval(mCondition) == null) {
					System.err.println("eval null");
					for (Column col : mInput.getInputSchema()) {
						System.err.print(col.toString() +":" +col.getColumnName() + "|");
					}
					System.err.println("");
				}
				
				if(eval.eval(mCondition).equals(BooleanValue.FALSE)){
					tuple = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (tuple == null);
		return tuple;
	}

	@Override
	public void reset() {
		mInput.reset();
	}

	@Override
	public Column[] getInputSchema() {
		return mSchema;
	}

}
