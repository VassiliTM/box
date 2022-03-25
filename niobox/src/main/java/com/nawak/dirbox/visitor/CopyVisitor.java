package com.nawak.dirbox.visitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Predicate;

public class CopyVisitor extends SimpleFileVisitor<Path>
{
	private Path fromPath;
	private Path toPath;
	private StandardCopyOption copyOption;
	private boolean keepStructure;
	private PathMatcher matcher;
	private Predicate<String> predicate;

	public CopyVisitor(Path fromPath, Path toPath)
	{
		this.fromPath = fromPath;
		this.toPath = toPath;
		this.copyOption = StandardCopyOption.REPLACE_EXISTING;
		this.keepStructure = true;
		this.matcher = null;
		this.predicate = null;
	}
	
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		if((Objects.nonNull(matcher) && matcher.matches(file)) || (Objects.nonNull(predicate) && predicate.test(file.toString())))
		{	
			if(keepStructure)
			{
				Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
			}
			else
			{
				Files.copy(file, toPath.resolve(file), copyOption);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
	{
		if(keepStructure)
		{
			Path targetPath = toPath.resolve(fromPath.relativize(dir));
	
			if (!Files.exists(targetPath))
			{
				Files.createDirectory(targetPath);
			}
		}
		return FileVisitResult.CONTINUE;
	}


	/**
	 * @param copyOption the copyOption to set
	 */
	public void setCopyOption(StandardCopyOption copyOption) 
	{
		this.copyOption = copyOption;
	}


	/**
	 * @param keepStructure the keepStructure to set
	 */
	public void setKeepStructure(boolean keepStructure) 
	{
		this.keepStructure = keepStructure;
	}

	/**
	 * @param predicate the predicate to set
	 */
	public void setPredicate(Predicate<String> predicate) 
	{
		this.predicate = predicate;
	}


	public void setPattern(String pattern) 
	{
		this.matcher = FileSystems.getDefault().getPathMatcher(pattern);
	}
}