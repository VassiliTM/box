package com.nawak.dirbox;


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

import com.nawak.dirbox.visitor.CleanVisitor;
import com.nawak.dirbox.visitor.CopyVisitor;
import com.nawak.dirbox.visitor.DeleteVisitor;
import com.nawak.dirbox.visitor.FunctionVisitor;
import com.nawak.dirbox.visitor.ListVisitor;
import com.nawak.dirbox.visitor.SizeVisitor;

/**
 * 
 * @author v_ta
 * 
 * TODO : Ajouter move avec predicate et pattern
 * TODO : Ajouter Delete avec predicate et pattern
 */

public class DirBox
{
	private Path path;

	public DirBox(Path path) 
	{
		check();
		this.path = path;
	}
	
	/**
	 * walk thru the directory structure and applies the given function to each file
	 * 
	 * @param function
	 * @throws IOException
	 */
	public void apply(Function<Path, FileVisitResult> function) throws IOException
	{
		Files.walkFileTree(path, new FunctionVisitor(function));
	}
	
	/**
	 * Deletes all files in a folder and its sub-folders. <br>
	 * Leave the folder structure intact.<br>
	 * If initial folder does not exist nothing is done
	 *
	 * @throws IOException
	 */
	public void clean() throws IOException
	{
		Files.walkFileTree(path, new CleanVisitor());
	}

	/**
	 * Copies the folder structure and all files to a folder, without depth restriction.
	 *
	 * @param to
	 * @throws IOException
	 */
	public void copy(Path to) throws IOException
	{
		CopyVisitor copyVisitor = new CopyVisitor(path, to);
		Files.walkFileTree(path, copyVisitor);
	}
	
	/**
	 * Copies all files to a folder, without depth restriction.
	 * if keepStructure is set to true, the folder structure is kept.
	 *
	 * @param to
	 * @throws IOException
	 */
	public void copy(Path to, boolean keepStructure) throws IOException
	{
		CopyVisitor copyVisitor = new CopyVisitor(path, to);
		copyVisitor.setKeepStructure(keepStructure);
		
		Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, copyVisitor);
	}

	/**
	 * Copies the folder structure and all files to a folder, without depth restriction.<br>
	 * Only consider records that are validated by the predicate<br>
	 * 
	 * @param to
	 * @param predicate
	 * @throws IOException
	 */

	public void copy(Path to, Predicate<String> predicate) throws IOException
	{
		CopyVisitor copyVisitor = new CopyVisitor(path, to);
		copyVisitor.setPredicate(predicate);
		
		Files.walkFileTree(path, copyVisitor);
	}

	/**
	 * Copies the folder structure and all files to a folder, without depth restriction.<br>
	 * Only consider records that are validated by the pattern (accepted pattern are : "regex:" or "glob:")<br>
	 * <br>
	 * See example of pattern here
	 * {@link java.nio.file.FileSystem#getPathMatcher(String)}
	 * 
	 * @param to
	 * @param pattern
	 * @throws IOException
	 */

	public void copy(Path to, String pattern) throws IOException
	{
		CopyVisitor copyVisitor = new CopyVisitor(path, to);
		copyVisitor.setPattern(pattern);
		
		Files.walkFileTree(path, copyVisitor);
	}
	
	/**
	 * Deletes all files and folders, including the initial folder.
	 *
	 * @throws IOException
	 */
	public void delete() throws IOException
	{
		Files.walkFileTree(path, new DeleteVisitor());
	}

	/**
	 * give the size of folder
	 * @return size
	 * @throws IOException 
	 */
	public long folderSize() throws IOException
	{
		SizeVisitor sizeVisitor = new SizeVisitor();
		Files.walkFileTree(path, sizeVisitor);

		return sizeVisitor.getSize();
	}

	/**
	 * give the size of folder, apply pattern filter (glob or regex)
	 * 
	 * <br>
	 * See example of pattern here
	 * {@link java.nio.file.FileSystem#getPathMatcher(String)}
	 * 
	 * @param pattern glob or regex
	 * @return size
	 * @throws IOException 
	 */
	public long folderSize(String pattern) throws IOException
	{
		SizeVisitor sizeVisitor = new SizeVisitor(pattern);
		Files.walkFileTree(path, sizeVisitor);

		return sizeVisitor.getSize();
	}

	/**
	 * List only files in recursive structure
	 * 
	 * @return list of path
	 * @throws IOException
	 */
	public List<Path> list() throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}
	
	public List<Path> list(Type type) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}
	
	/**
	 * List only files in recursive structure
	 * 
	 * @return list of path
	 * @throws IOException
	 */
	public List<Path> list(int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}


	/**
	 * List only files in recursive structure, apply predicate to filter
	 * 
	 * @param predicate the predicate to filter path
	 * @return list of path
	 * @throws IOException
	 */
	public List<Path> list(Predicate<String> predicate) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setPredicate(predicate);
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}

	/**
	 * List only files in recursive structure, apply pattern filter (glob or regex)
	 * 
	 * <br>
	 * See example of pattern here
	 * {@link java.nio.file.FileSystem#getPathMatcher(String)}
	 * 
	 * @param pattern glob or regex
	 * @return list of path
	 * @throws IOException
	 */
	public List<Path> list(String pattern) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setPattern(pattern);
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}

	/**
	 * List only files in recursive structure, apply pattern filter (glob or regex) at a depth 
	 *
	 * @param pattern glob or regex
	 * @param maxDepth the max depth
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Path> list(String pattern, int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setPattern(pattern);
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}
	
	/**
	 * List only files in recursive structure, apply predicate to filter
	 *
	 * @param pattern glob or regex
	 * @param maxDepth the max depth
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Path> list(Predicate<String> predicate, int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setPredicate(predicate);
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}

	public List<Path> list(Type type, String pattern) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		listVisitor.setPattern(pattern);
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}
	
	public List<Path> list(Type type, Predicate<String> predicate) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		listVisitor.setPredicate(predicate);
		Files.walkFileTree(path, listVisitor);

		return listVisitor.getList();
	}
	
	public List<Path> list(Type type, int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}
	
	public List<Path> list(Type type, String pattern, int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		listVisitor.setPattern(pattern);
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}
	
	public List<Path> list(Type type, Predicate<String> predicate, int maxDepth) throws IOException
	{
		ListVisitor listVisitor = new ListVisitor();
		listVisitor.setType(type);
		listVisitor.setPredicate(predicate);
		Files.walkFileTree(path, Collections.emptySet(), maxDepth, listVisitor);

		return listVisitor.getList();
	}

	/**
	 * Moves a folder, all its sub-folders and files into a new folder
	 *
	 * @param to
	 * @throws IOException
	 */
	public void move(Path to) throws IOException
	{
		Files.walkFileTree(path, new CopyVisitor(path, to));
		Files.walkFileTree(path, new DeleteVisitor());
	}

	/**
	 * validate path
	 * @param paths
	 */
	private void check()
	{
		Objects.requireNonNull(path);
		
		if (!Files.exists(path))
		{
			throw new SecurityException(String.format("%s does not exist", path.toString()));
		}
		
		if (!Files.isDirectory(path))
		{
			throw new IllegalArgumentException(String.format("%s is not a directory", path.toString()));
		}
	}
}
