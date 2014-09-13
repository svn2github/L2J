package l2next.commons.compiler;

import org.eclipse.jdt.internal.compiler.apt.util.EclipseFileManager;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Класс компиляции внешних Java файлов<br>
 *
 * @author G1ta0
 */
public class Compiler
{
	private static final Logger _log = LoggerFactory.getLogger(Compiler.class);
	private static final JavaCompiler javac = new EclipseCompiler();
	private final DiagnosticListener<JavaFileObject> listener = new DefaultDiagnosticListener();
	private final StandardJavaFileManager fileManager = new EclipseFileManager(Locale.getDefault(), Charset.defaultCharset());
	private final MemoryClassLoader memClassLoader = new MemoryClassLoader();
	private final MemoryJavaFileManager memFileManager = new MemoryJavaFileManager(fileManager, memClassLoader);

	public boolean compile(File... files)
	{
		// javac options
		List<String> options = new ArrayList<String>();
		options.add("-Xlint:all");
		options.add("-warn:none");
		//options.add("-g:none");
		options.add("-g");
		options.add("-1.7");
		//options.add("-deprecation");

		Writer writer = new StringWriter();
		JavaCompiler.CompilationTask compile = javac.getTask(writer, memFileManager, listener, options, null, fileManager.getJavaFileObjects(files));

		return compile.call();
	}

	public boolean compile(Collection<File> files)
	{
		return compile(files.toArray(new File[files.size()]));
	}

	public MemoryClassLoader getClassLoader()
	{
		return memClassLoader;
	}

	private class DefaultDiagnosticListener implements DiagnosticListener<JavaFileObject>
	{
		@Override
		public void report(Diagnostic<? extends JavaFileObject> diagnostic)
		{
			Diagnostic.Kind kind = diagnostic.getKind();
			if(kind != Diagnostic.Kind.ERROR)
			{
				_log.debug(diagnostic.getSource().getName() + (diagnostic.getPosition() == Diagnostic.NOPOS ? "" : ":" + diagnostic.getLineNumber() + "," + diagnostic.getColumnNumber()) + ": " + diagnostic.getMessage(Locale.getDefault()));
			}
			else
			{
				_log.error(diagnostic.getSource().getName() + (diagnostic.getPosition() == Diagnostic.NOPOS ? "" : ":" + diagnostic.getLineNumber() + "," + diagnostic.getColumnNumber()) + ": " + diagnostic.getMessage(Locale.getDefault()));
			}
		}
	}
}