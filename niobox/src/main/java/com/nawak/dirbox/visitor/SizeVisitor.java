package com.nawak.dirbox.visitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

public class SizeVisitor extends SimpleFileVisitor<Path>
{
	private final AtomicLong size = new AtomicLong(0);
	private PathMatcher matcher;

	public SizeVisitor()
	{
		this("glob:**.*");
	}

	public SizeVisitor(String pattern)
	{
		matcher = FileSystems.getDefault().getPathMatcher(pattern);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		if (matcher.matches(file))
		{
			size.addAndGet(attrs.size());
		}

		return FileVisitResult.CONTINUE;
	}

	public long getSize()
	{
		return size.get();
	}
}
