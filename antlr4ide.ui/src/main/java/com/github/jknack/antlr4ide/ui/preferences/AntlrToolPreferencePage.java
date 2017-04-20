package com.github.jknack.antlr4ide.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class AntlrToolPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AntlrToolPreferencePage() {
		super(GRID);
//		setPreferenceStore(activator.getDefault().getPreferenceStore());
		setDescription("ANTLR Tool Configuration");
	}
	
	
	@Override
	public void init(IWorkbench workbench) { 
	    setPreferenceStore(workbench.getPreferenceStore());
}

	@Override
	protected void createFieldEditors() {
		// ------------ Orginal skeleton:
		//v ANTLR Tool 
		//  [ ] Tool enabled
		//        List of available distributions
		//        Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//v Options
		//    Directory (-o) _______
		//    Library (-lib) _______
		//    [ ] Generate parse tree listener (-listner)
		//    [ ] Generate parse tree visitors (-visitor)
		//    [ ] Delete generated files when clean build is triggered
		//    [ ] Mark generated files as derived
		//    Encoding (-encoding) _______
		//v VM Arguments
		//    ________________________________________
		// -------
		
		
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_TOOLENABLED,	"Tool Enabled",	getFieldEditorParent()));
		addField(new AntrVersionListEditor(AntlrToolPreferenceConstants.P_DISTRIBUTIONS, "Distributions", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrToolPreferenceConstants.P_OUTDIRECTORY,"Directory (-o)", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrToolPreferenceConstants.P_LIB, "Library (-lib)", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_GENLISTENER,	"Generate parse tree listener (-listener)",	getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_GENVISITOR,	"Generate parse tree visitors (-visitor)",	getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_DELETEGEN,	"Delete generated files when clean build is triggered",	getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrToolPreferenceConstants.P_MARKDERIVED,	"Mark generated files as derived",	getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrToolPreferenceConstants.P_ENCODING, "Encoding (-encoding)", getFieldEditorParent()));
		
		addField(new StringFieldEditor(AntlrToolPreferenceConstants.P_VMARGS, "VM Arguments", getFieldEditorParent()));

		
		}
	
	
	
	private class AntrVersionListEditor extends ListEditor {
		private String del=",";

		public AntrVersionListEditor(String label, String content, Composite parent) {
			super(label,content,parent);
		}

		@Override
		protected String createList(String[] items) {
			StringBuffer out=new StringBuffer();
			int m=items.length;
			for(int i=0; i<m; i++) { 
				out.append(items[i]);
				if(i+1<m) out.append(del);
			}
			return out.toString();
		}

		@Override
		protected String getNewInputObject() {
			// TODO: Add popup prompting for tool-jar
			
			return "v4.5.3 ./tooljar/antlr4.jar";
		}

		@Override
		protected String[] parseString(String stringList) {
//			Exception ex=new Exception();
//			ex.printStackTrace();
			
			
			return stringList.split(del);
		}
		
	}

}
