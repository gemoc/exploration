/*
* generated by Xtext
*/
grammar InternalEmfExecutionConfiguration;

options {
	superClass=AbstractInternalAntlrParser;
	backtrack=true;
	
}

@lexer::header {
package fr.inria.aoste.timesquare.backend.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package fr.inria.aoste.timesquare.backend.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import fr.inria.aoste.timesquare.backend.services.EmfExecutionConfigurationGrammarAccess;

}

@parser::members {

/*
  This grammar contains a lot of empty actions to work around a bug in ANTLR.
  Otherwise the ANTLR tool will create synpreds that cannot be compiled in some rare cases.
*/
 
 	private EmfExecutionConfigurationGrammarAccess grammarAccess;
 	
    public InternalEmfExecutionConfigurationParser(TokenStream input, EmfExecutionConfigurationGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }
    
    @Override
    protected String getFirstRuleName() {
    	return "EMFExecutionConfiguration";	
   	}
   	
   	@Override
   	protected EmfExecutionConfigurationGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}
}

@rulecatch { 
    catch (RecognitionException re) { 
        recover(input,re); 
        appendSkippedTokens();
    } 
}




// Entry rule entryRuleEMFExecutionConfiguration
entryRuleEMFExecutionConfiguration returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getEMFExecutionConfigurationRule()); }
	 iv_ruleEMFExecutionConfiguration=ruleEMFExecutionConfiguration 
	 { $current=$iv_ruleEMFExecutionConfiguration.current; } 
	 EOF 
;

// Rule EMFExecutionConfiguration
ruleEMFExecutionConfiguration returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getEMFExecutionConfigurationAccess().getImportsImportStatementParserRuleCall_0_0()); 
	    }
		lv_imports_0_0=ruleImportStatement		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEMFExecutionConfigurationRule());
	        }
       		add(
       			$current, 
       			"imports",
        		lv_imports_0_0, 
        		"ImportStatement");
	        afterParserOrEnumRuleCall();
	    }

)
)+(
(
		{ 
	        newCompositeNode(grammarAccess.getEMFExecutionConfigurationAccess().getForcedClockMappingsForcedClockMappingParserRuleCall_1_0()); 
	    }
		lv_forcedClockMappings_1_0=ruleForcedClockMapping		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEMFExecutionConfigurationRule());
	        }
       		add(
       			$current, 
       			"forcedClockMappings",
        		lv_forcedClockMappings_1_0, 
        		"ForcedClockMapping");
	        afterParserOrEnumRuleCall();
	    }

)
)*)
;





// Entry rule entryRuleForcedClockMapping
entryRuleForcedClockMapping returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getForcedClockMappingRule()); }
	 iv_ruleForcedClockMapping=ruleForcedClockMapping 
	 { $current=$iv_ruleForcedClockMapping.current; } 
	 EOF 
;

// Rule ForcedClockMapping
ruleForcedClockMapping returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='When' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getForcedClockMappingAccess().getWhenKeyword_0());
    }
(
(
		{ 
		  /* */ 
		}
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getForcedClockMappingRule());
	        }
        }
	otherlv_1=RULE_ID
	{
		newLeafNode(otherlv_1, grammarAccess.getForcedClockMappingAccess().getClockClockCrossReference_1_0()); 
	}

)
)	otherlv_2='DSA' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getForcedClockMappingAccess().getDSAKeyword_2());
    }
	otherlv_3='returns' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getForcedClockMappingAccess().getReturnsKeyword_3());
    }
	otherlv_4='true' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getForcedClockMappingAccess().getTrueKeyword_4());
    }
	otherlv_5='avoid' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getForcedClockMappingAccess().getAvoidKeyword_5());
    }
(
(
		{ 
		  /* */ 
		}
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getForcedClockMappingRule());
	        }
        }
	otherlv_6=RULE_ID
	{
		newLeafNode(otherlv_6, grammarAccess.getForcedClockMappingAccess().getClockToAvoidWhenTrueClockCrossReference_6_0()); 
	}

)
)	otherlv_7=';' 
    {
    	newLeafNode(otherlv_7, grammarAccess.getForcedClockMappingAccess().getSemicolonKeyword_7());
    }
)
;





// Entry rule entryRuleImportStatement
entryRuleImportStatement returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getImportStatementRule()); }
	 iv_ruleImportStatement=ruleImportStatement 
	 { $current=$iv_ruleImportStatement.current; } 
	 EOF 
;

// Rule ImportStatement
ruleImportStatement returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='import' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getImportStatementAccess().getImportKeyword_0());
    }
(
(
		lv_importURI_1_0=RULE_STRING
		{
			newLeafNode(lv_importURI_1_0, grammarAccess.getImportStatementAccess().getImportURISTRINGTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getImportStatementRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"importURI",
        		lv_importURI_1_0, 
        		"STRING");
	    }

)
)	otherlv_2=';' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getImportStatementAccess().getSemicolonKeyword_2());
    }
)
;







RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;

