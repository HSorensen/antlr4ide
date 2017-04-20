package com.github.jknack.antlr4ide.ui.generator

import com.github.jknack.antlr4ide.generator.CodeGeneratorListener
import com.github.jknack.antlr4ide.generator.ToolOptions
import com.google.common.base.Function
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.NullProgressMonitor

class RefreshProjectProcessor implements CodeGeneratorListener {

  static val ROOT = #[".", "./", ".\\"]

  override beforeProcess(IFile file, ToolOptions options) {
  }

  override afterProcess(IFile file, ToolOptions options) {
    val ex=new Exception()
    System::out.println("---\n--- RefreshProject file>"+file+"<")
    for(StackTraceElement s: ex.stackTrace) System::out.println("    "+s.toString)      
      
    val project = file.project
    val monitor = new NullProgressMonitor

    project.refreshLocal(IResource.DEPTH_INFINITE, monitor)
//    project.refreshLocal(IResource.DEPTH_ONE, monitor)  // only refresh target directory ?
    val outfldr=project.getFolder(options.outputDirectory)
//    outfldr.refreshLocal(IResource.DEPTH_INFINITE, monitor)
    
    val output = options.output(file)
    val relative = output.relative
    if (project.exists(relative)) {
      val container = if (ROOT.contains(relative.toString))
          project
        else
          project.getFolder(output.relative)

      /**
       * Mark files as derived
       */
      val Function<IResource, String> fileName = [
        it.location.removeFileExtension.lastSegment.toLowerCase
      ]
      val fname = fileName.apply(file)
      container.accept [ generated |
        val gname = fileName.apply(generated)
        // TODO: make me stronger
    System::out.println("--- RefreshProject file>"+file.name+"<>"+generated.name+"<>"+gname
        +"<>"+(file.name != generated.name)
        +"<>"+(gname.startsWith(fname))
        +"<>"+generated.derived+"<"
    )    
        if (file.name != generated.name && gname.startsWith(fname)) {
            if (!generated.derived)
               generated.setDerived(options.derived, monitor)
        }
        return true
      ]
    }
    
    
  }

}
