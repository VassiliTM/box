package com.nawak.dirbox.visitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import com.nawak.dirbox.Type;

public class ListVisitor extends SimpleFileVisitor<Path>
{
	private Set<Path> list;
	private Type type;
	private PathMatcher matcher;
	private Predicate<String> predicate;

	public ListVisitor()
	{
		list = new TreeSet<>();
		this.type = Type.FILE;
		this.predicate = null;
		this.matcher = null;		
	}
	

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		if(type == Type.FILE || type == Type.BOTH) 
		{
			if((Objects.nonNull(matcher) && matcher.matches(file)) || (Objects.nonNull(predicate) && predicate.test(file.toString())))
			{
				list.add(file);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) throws IOException
	{
		if(type == Type.FOLDER || type == Type.BOTH) 
		{
			list.add(file);
		}
		
		return FileVisitResult.CONTINUE;
	}

	public List<Path> getList()
	{
		return new ArrayList<>(list);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) 
	{
		this.type = type;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) 
	{
		this.matcher = FileSystems.getDefault().getPathMatcher(pattern);
	}

	/**
	 * @param predicate the predicate to set
	 */
	public void setPredicate(Predicate<String> predicate) 
	{
		this.predicate = predicate;
	}
}
