package com.nawak.dirbox.visitor;

import com.nawak.dirbox.Type;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

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
		this.matcher = FileSystems.getDefault().getPathMatcher(pattern);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		if (this.matcher.matches(file))
		{
			this.size.addAndGet(attrs.size());
		}

		return FileVisitResult.CONTINUE;
	}

	public long getSize() {
		return this.size.get();
	}

	public void setPattern(String pattern) {
	}

	public void setPredicate(Predicate<String> predicate) {
	}

	public void setType(Type type) {
	}
}
