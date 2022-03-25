package com.nawak.dirbox.visitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyPatternVisitor extends SimpleFileVisitor<Path>
{
	private Path fromPath;
	private Path toPath;
	private PathMatcher matcher;

	public CopyPatternVisitor(Path fromPath, Path toPath, String pattern)
	{
		this.fromPath = fromPath;
		this.toPath = toPath;
		this.matcher = FileSystems.getDefault().getPathMatcher(pattern);
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
	{
		Path targetPath = toPath.resolve(fromPath.relativize(dir));

		if (!Files.exists(targetPath))
		{
			Files.createDirectory(targetPath);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		if (matcher.matches(file))
		{
			Files.copy(file, toPath.resolve(fromPath.relativize(file)));
		}
		return FileVisitResult.CONTINUE;
	}
}