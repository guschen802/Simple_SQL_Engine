package edu.buffalo.cse562;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.Select;


public class Main {
	
	private static File dataDir = null;
	private static HashMap<String, CreateTable> tables = new HashMap<String, CreateTable>();
	private static ArrayList<File> sqlFiles = new ArrayList<File>();
	
	public static void main(String[] args) {	
		int i;
		for(i = 0; i < args.length; i++ ){
			if(args[i].equals("--data")){
				dataDir = new File(args[i+1]);
				i++;
			}else {
				sqlFiles.add(new File(args[i]));
			}
		}
		for(File sql : sqlFiles){
			FileReader sqlstream;
			try {
				sqlstream = new FileReader(sql);
				CCJSqlParser parser = new CCJSqlParser(sqlstream);
				Statement stmt;
				while((stmt = parser.Statement()) != null){
					if(stmt instanceof CreateTable){
						CreateTable ct = (CreateTable)stmt;
						tables.put(ct.getTable().getName(), ct);
					}
					else if (stmt instanceof Select) {
						Select sl = (Select)stmt;
						SqlIterator output = null;
						SelectBodyScanner selectBodyScanner = new SelectBodyScanner(dataDir, tables);
						sl.getSelectBody().accept(selectBodyScanner);
						output = selectBodyScanner.getIterator();	
						
						if(output != null){
							dump(output);
							//dumpToFile(output, "E://test.dat");
						}
						else {
							System.err.println("no result");
						}
					}
					else {
						System.err.println("ERROR: UNKNOWN STATEMENT!");
						}
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	private static void dump(SqlIterator input){
		LeafValue[] row = input.readNextTuple();
		while(row != null){
			for(int i=0; i< row.length; i++){
				if(row[i] instanceof StringValue){
					StringValue sv = (StringValue) row[i];
					if (i == row.length -1) {
						System.out.print(sv.getNotExcapedValue());
					}else {
						System.out.print(sv.getNotExcapedValue() + "|");
					}
				}else {
					if (i == row.length -1) {
						System.out.print(row[i].toString());
					}else {
						System.out.print(row[i].toString() + "|");
					}
				}
				//String item = row[i].toString();
				//item = item.replace("\'", "");
				
				
			}
			System.out.println("");
			row = input.readNextTuple();
		}
		System.out.println("");
		/*
		for (Column col : input.getInputSchema()) {
			System.out.print(col.toString() + "|");
		}
		System.out.println("");
		*/
	}
	
	@SuppressWarnings("unused")
	private static void dumpToFile(SqlIterator input,String path){
		LeafValue[] row = input.readNextTuple();
		try {
			BufferedWriter outPutWriter = new BufferedWriter(new FileWriter(path));
			
			while(row != null){
				for(int i=0; i< row.length; i++){
					if(row[i] instanceof StringValue){
						StringValue sv = (StringValue) row[i];
						if (i == row.length -1) {
							outPutWriter.write(sv.getNotExcapedValue());
						}else {
							outPutWriter.write(sv.getNotExcapedValue() + "|");
						}
					}else {
						if (i == row.length -1) {
							outPutWriter.write(row[i].toString());
						}else {
							outPutWriter.write(row[i].toString() + "|");
						}
					}	
				}
				outPutWriter.write("\n");
				row = input.readNextTuple();
			}
			outPutWriter.close();
			System.err.println("File dump finished!");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

	