package com.github.jknack.antlr4ide.ui.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.emf.ecore.resource.Resource;
import com.github.jknack.antlr4ide.lang.Grammar;
import com.github.jknack.antlr4ide.lang.GrammarType;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class Antlr4BuilderParticipant extends org.eclipse.xtext.builder.BuilderParticipant {

	static boolean DEBUG=true;
	
	/**
	 * Build dependency strategy
	 * ===
	 * 
	 * - TODO: If a grammar is used as IMPORT in other grammars, figure out if it should be built 
	 * - Build sequence
	 * - - Lexer grammars
	 * - - Parser grammars
	 * - - Combined grammars
	 * 
	 */
	@Override
	protected List<Delta> 
	          getRelevantDeltas(IBuildContext context) 
	{
		if(DEBUG)
		System.out.println(">>> Antlr4BuilderParticipant getRelevantDeltas "+context.getClass());

		List<Delta> result = super.getRelevantDeltas(context);
		if (result.size()<=1) return result; // no need to sort just one entry
		
		List<Delta> resultLexers  = new ArrayList<Delta>(); 
		List<Delta> resultParsers = new ArrayList<Delta>(); 
		List<Delta> resultOthers  = new ArrayList<Delta>();

		List<Delta> resultFinal  = new ArrayList<Delta>();
		
		int i=0;
		for (Delta delta : result) {
			Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
			Grammar grammar = grammarFromResource(resource);
			if(DEBUG)
			System.out.println(">>> Antlr4BuilderParticipant getRelevantDeltas ["+i+"]>"+delta.getUri()+"< type>"+grammar.getType()+"<");
				
			if (grammar.getType()==GrammarType.LEXER)
				resultLexers.add(delta);
			else
  		    if (grammar.getType()==GrammarType.PARSER)
				resultParsers.add(delta);
			else
				resultOthers.add(delta);
				
			i++;
		}
		
		
		Comparator<Delta> compareUri = new CompareUri() ;
		
		Collections.sort(resultLexers, compareUri);
		Collections.sort(resultParsers, compareUri);
		Collections.sort(resultOthers, compareUri);
		
		resultFinal.addAll(resultLexers);
		resultFinal.addAll(resultParsers);
		resultFinal.addAll(resultOthers);
		
		if(DEBUG) {
		  i=0;
		  for (Delta delta : resultFinal) {
  			System.out.println("++> Antlr4BuilderParticipant getRelevantDeltas ["+i+"]>"+delta.getUri()+"<");
			i++;
	  	  }
		}
		return resultFinal;
	}
	

	/**
	 * Reads a grammar using a Resource as input.
	 * This implementation is inspired from @see(com.github.jknack.antlr4ide.ui.services.DefaultGrammarResource) 
	 */
	private Grammar grammarFromResource(Resource _resourceFrom) {
	    EList<EObject> _contents = _resourceFrom.getContents();
	    EObject _head = IterableExtensions.<EObject>head(_contents);
	    return ((Grammar) _head);
	}
	
	/**
	 * Compare two resource URIs as strings.
	 */
	private class CompareUri implements Comparator<Delta> {
	    public int compare(Delta delta1
			             , Delta delta2)
	    {
	    	return delta1.getUri().toString().compareTo(delta2.getUri().toString());
	    }
	}
	
}

