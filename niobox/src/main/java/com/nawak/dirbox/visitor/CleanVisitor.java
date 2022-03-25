package com.nawak.dirbox.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanVisitor extends SimpleFileVisitor<Path>
{
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}
}
