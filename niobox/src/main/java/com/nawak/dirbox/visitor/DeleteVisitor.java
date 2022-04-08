package com.nawak.dirbox.visitor;

import com.nawak.dirbox.Type;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;

public class DeleteVisitor extends SimpleFileVisitor<Path>
{

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	{
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (exc == null) {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		}
		throw exc;
	}

	public void setPredicate(Predicate<String> predicate) {
	}

	public void setPattern(String pattern) {
	}

	public void setType(Type type) {
	}
}
