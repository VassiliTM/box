package com.nawak.dirbox;

import com.nawak.dirbox.visitor.*;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author v_ta
 * <p>
 * TODO : Ajouter move avec predicate et pattern
 * TODO : Ajouter Delete avec predicate et pattern
 */

public class DirBox {

	private final Path path;
	private int maxDepth;
	private Predicate<String> predicate;
	private String pattern;
	private Type type;
	private boolean keepStructure;

	public DirBox(Path path) {
		check();
		this.path = path;
		this.maxDepth = Integer.MAX_VALUE;
		this.predicate = null;
		this.pattern = null;
		this.type = Type.FILE;
		this.keepStructure = true;
	}

	/**
	 * walk thru the directory structure and applies the given function to each file
	 *
	 * @param function
	 * @throws IOException
	 */
	public void apply(Function<Path, FileVisitResult> function) throws IOException {
		Files.walkFileTree(this.path, new FunctionVisitor(function));
	}

	/**
	 * Copies the folder structure and all files to a folder, without depth restriction.
	 *
	 * @param to
	 * @throws IOException
	 */
	public void copy(Path to) throws IOException {
		CopyVisitor copyVisitor = new CopyVisitor(this.path, to);
		copyVisitor.setPredicate(this.predicate);
		copyVisitor.setPattern(this.pattern);
		copyVisitor.setKeepStructure(this.keepStructure);

		Files.walkFileTree(this.path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), this.maxDepth, copyVisitor);
	}

	/**
	 * Moves a folder, all its sub-folders and files into a new folder
	 *
	 * @param to
	 * @throws IOException
	 */
	public void move(Path to) throws IOException {
		copy(to);
		Files.walkFileTree(this.path, new DeleteVisitor());
	}


	/**
	 * Deletes all files and folders, including the initial folder.
	 *
	 * @throws IOException
	 */
	public void delete() throws IOException {
		DeleteVisitor deleteVisitor = new DeleteVisitor();
		deleteVisitor.setPredicate(this.predicate);
		deleteVisitor.setPattern(this.pattern);
		deleteVisitor.setType(this.type);

		Files.walkFileTree(this.path, deleteVisitor);
	}

	/**
	 * give the size of folder
	 *
	 * @return size
	 * @throws IOException
	 */
	public long folderSize() throws IOException {
		SizeVisitor sizeVisitor = new SizeVisitor();
		sizeVisitor.setPattern(this.pattern);
		sizeVisitor.setPredicate(this.predicate);
		sizeVisitor.setType(this.type);
		Files.walkFileTree(this.path, sizeVisitor);

		return sizeVisitor.getSize();
	}


	/**
	 * List paths depending option chosen
	 *
	 * @return list of path
	 * @throws IOException the exception :)
	 */
	public List<Path> list() throws IOException {
		ListVisitor listVisitor = new ListVisitor();

		listVisitor.setPattern(this.pattern);
		listVisitor.setPredicate(this.predicate);
		listVisitor.setType(this.type);

		Files.walkFileTree(this.path, Collections.emptySet(), this.maxDepth, listVisitor);

		return listVisitor.getList();
	}

	/**
	 * validate path
	 */
	private void check() {
		Objects.requireNonNull(this.path);

		if (!Files.exists(this.path)) {
			throw new SecurityException(String.format("%s does not exist", this.path.toString()));
		}

		if (!Files.isDirectory(this.path)) {
			throw new IllegalArgumentException(String.format("%s is not a directory", this.path.toString()));
		}
	}

	public void setPredicate(Predicate<String> predicate) {
		this.predicate = predicate;
	}

	/**
	 * See example of pattern here
	 * {@link java.nio.file.FileSystem#getPathMatcher(String)}
	 *
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public void setKeepStructure(boolean keepStructure) {
		this.keepStructure = keepStructure;
	}
}
