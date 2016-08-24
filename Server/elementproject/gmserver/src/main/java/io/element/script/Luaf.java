package io.element.script;

import io.element.app.App;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.Compilable;
import javax.script.ScriptException;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class Luaf {

	protected static ScriptEngine se = new ScriptEngineManager().getEngineByExtension(".lua");
	
	public static Bindings createBindings()
	{
		return se.createBindings();
	}
	
	protected static CompiledScript getCompiledScript(String filepath)
	{
		CompiledScript cs;
		try {
			FileReader reader = new FileReader(filepath);
			cs = ((Compilable)se).compile(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			App.LOGGER.warn("can not find file, path is {}",filepath);
			cs = null;
		} catch (ScriptException e) {
			App.LOGGER.warn("the lua script compiled err ,error is {}",e.getMessage());
			cs = null;
		} catch (IOException e) {
			
			cs = null;
		}
	
		return cs;
	}
	
	public static LuaTable getTableInTable( LuaTable lv, String sTable )
	{
		LuaValue inner = lv.get(sTable);
		return inner.istable() ? (LuaTable) inner : null;
	}
	
	public static LuaTable getTableInFile( Bindings b, String filepath, String sTable )
	{
		CompiledScript cs = getCompiledScript(filepath);
		if(cs == null)
			return null;
		
		try {
			cs.eval(b);
			LuaValue lv = (LuaValue)b.get(sTable);
			if( lv != null && lv.istable() )
				return (LuaTable) lv;
		} catch (ScriptException e) {
			App.LOGGER.warn("the lua script eval err ,error is {}", e.getMessage());
		}
		
		return null;
	}
	
	public static LuaTable getTableInFile( Bindings b, Vector<String> filespath, String sTable )
	{
		List<CompiledScript> css = new ArrayList<CompiledScript>();
		
		for (Iterator<String> iterator = filespath.iterator(); iterator.hasNext();) {
			CompiledScript cs = getCompiledScript( iterator.next() );
			if(cs != null) 
				css.add(cs);
			else 
				return null;
		}
		
		for (Iterator<CompiledScript> iterator = css.iterator(); iterator.hasNext();) {
			try {
				CompiledScript cs = (CompiledScript) iterator.next();
				cs.eval(b);
			} catch (ScriptException e) {
				App.LOGGER.warn("the lua script eval err ,error is {}", e.getMessage());
			}
		}
		
		LuaValue lv = (LuaValue)b.get(sTable);
		return lv.istable() ? (LuaTable)lv : null;
	}
	
}
