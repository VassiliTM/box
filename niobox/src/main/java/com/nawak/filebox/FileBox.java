package com.nawak.filebox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

/**
 * 
 *  Static class of function related to files path
 *  date : 08/07/2016
 * 
 * 
 * @author v_ta
 * 
 * @version 1.4
 * 
 * 16/09/2016 : add function delete specific char sequence in file name.
 * 05/10/2016 : set to english javadoc
 */
public class FileBox
{
	
	private FileBox()
	{
		throw new IllegalStateException("Utility class");
	}

	
	/**
	 * Return the complete path of file (without file name).
	 * 
	 * @param file - the file name to search
	 * @return String - the absolute file path
	 */
	public static String getPathWithoutName(File file)
	{
		String absolute = file.getAbsolutePath();
		return absolute.substring(0, absolute.lastIndexOf("\\"));
	}

	/**
	 * Recover extension of file name.
	 * the extension must be between 1 and 4 char length
	 * 
	 * @param file - the file to search
	 * @return String - the extension
	 */
	public static String getExtension(File file)
	{
		String result = "";
		String fileName = file.getName();
		int idx = fileName.lastIndexOf(".") + 1;
		int extLength = fileName.length() - idx;

		if (idx > 0)
		{
			if (extLength > 0 && extLength < 5)
			{
				result = fileName.substring(idx).toLowerCase();
			}
			else
			{
				result = "";
			}
		}
		return result;
	}

	/**
	 * Recover extension of string file name
	 * the extension must be between 1 and 4 char length
	 * @param str - the string to search
	 * @return String - the extension
	 */
	public static String getExtension(String str)
	{
		String result = "";
		int idx = str.lastIndexOf(".") + 1;
		int extLength = str.length() - idx;

		if (idx > 0)
		{
			if (extLength > 0 && extLength < 5)
			{
				result = str.substring(idx).toLowerCase();
			}
			else
			{
				result = "";
			}
		}
		return result;
	}

	/**
	 * Recover extension of string file name
	 * the extension must be between 1 and 4 char length
	 * @param str - the string to search
	 * @return String - the extension
	 */
	public static String getExtension(Path path)
	{
		return getExtension(path.toString());
	}

	/**
	 * TODO a modifier !!!!!!!!!
	 * prendre exemple sur getNameWithoutExtension(Path path)
	 * Recover the file name without extension
	 * @param file - the file to search
	 * @return String - the file name without extension
	 */
	public static String getNameWithoutExtension(File file)
	{
		String fileName = file.getName();
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static String getNameWithoutExtension(Path path)
	{
		String p = path.getFileName().toString();

		if (p.contains("."))
		{
			return p.substring(0, p.lastIndexOf("."));
		}
		else
		{
			return p.toString();
		}
	}

	/**
	 * TODO a modifier !!!!!!!!!
	 * prendre exemple sur getNameWithoutExtension(Path path)
	 * Recover the file name without extension
	 * @param str - the string to search
	 * @return String - the file name without extension
	 */
	public static String getNameWithoutExtension(String str)
	{
		return str.substring(str.lastIndexOf("\\") + 1, str.lastIndexOf("."));
	}

	/**
	 * Recover the file name in a String like absolute path
	 * @param str - the string to search
	 * @return String - the file name
	 */
	public static String getFileName(String str)
	{
		return str.substring(str.lastIndexOf("\\") + 1);
	}

	/**
	 * Give the current folder (last level) where the file is
	 * @param file - file to search
	 * @return String - the name of the current path 
	 */
	public static String getCurrentFolder(File file)
	{
		return file.getParent().substring(file.getParent().lastIndexOf("\\") + 1);
	}

	/**
	 * Give the current folder (last level) where the file is
	 * @param str - str to search
	 * @return String - the name of the current path 
	 */
	public static String getCurrentFolder(String str)
	{
		File file = new File(str);
		return file.getParent().substring(file.getParent().lastIndexOf("\\") + 1);
	}

	/**
	 * test if file extension are UpperCase
	 * 
	 * @param file
	 * @return true if UpperCase, false otherwise
	 */
	public static boolean isExtensionUpperCase(File file)
	{
		int index = 0;
		String nameFile = "";
		if (!file.isDirectory())
		{
			nameFile = file.getName();
			index = nameFile.lastIndexOf('.') + 1;
			for (int i = index; i < nameFile.length(); i++)
			{
				if (Character.isUpperCase(nameFile.charAt(i)))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * test if file extension are UpperCase
	 * 
	 * @param path
	 * @return true if UpperCase, false otherwise
	 */
	public static boolean isExtensionUpperCase(Path path)
	{
		return isExtensionUpperCase(path.toFile());
	}

	/**
	 * Remove a char sequence in file name
	 * @param file - to treat
	 * @param wordToRemove - char sequence to remove
	 * @return String - the new name without char sequence
	 */
	public static String removeWordInFileName(File file, String wordToRemove)
	{
		String newName = getPathWithoutName(file) + File.separator + file.getName().replace(wordToRemove, "");
		file.renameTo(new File(newName));
		return newName;
	}

	/**
	 * Replace extension in file
	 * @param file - file to treat
	 * @param newExt - the new extension
	 * @return String - newName with new extension
	 */
	public static String replaceExtension(File file, String newExt)
	{
		String newName = getPathWithoutName(file) + File.separator + getNameWithoutExtension(file) + "." + newExt;
		file.renameTo(new File(newName));
		return newName;
	}

	/**
	 * Change extension to lowerCase
	 * @param file
	 * @return newName
	 */
	public static String extToLowerCase(File file)
	{
		String newName = getPathWithoutName(file) + File.separator + getNameWithoutExtension(file) + "." + getExtension(file);
		file.renameTo(new File(newName));
		return newName;
	}

	public static boolean hasExtension(String sentence)
	{
		return sentence.matches("^.*\\.[a-zA-Z7]{1,4}$");
	}

	/**
	 * move file with specific folder tree to output folder with same specific folder.
	 * 
	 * moveSpecificTreeFileToSpecificTreeFile
	 * 
	 * <ul>example :
	 * <li>file = C:\folder_in\folder1\file.txt
	 * <li>fRootIn = folder_in
	 * <li>fRootOut = exclude
	 * </ul>
	 * <ul>moving
	 * <li>from C:\folder_in\folder1\file.txt
	 * <li>to   C:\exclude\folder1\file.txt
	 * </ul>
	 * @param file to move
	 * @param fRootIn the original folder
	 * @param fRootOut the destination folder
	 * 
	 */
	public static void movePartialTreeFile(File file, String fRootIn, String fRootOut)
	{
		try
		{
			String newPath = file.getAbsolutePath().replaceAll(fRootIn, fRootOut);
			File fileTo = new File(newPath);

			new File(fileTo.getParent()).mkdirs();

			if (!file.renameTo(fileTo))
			{
				System.out.println("Fail to move file " + file);
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void movePartialTreeFile(Path path, Path fRootIn, Path fRootOut) throws IOException
	{
		Path folderStructureDestination;
		Path in = fRootIn.relativize(path);
		Path folderStructure = in.getParent();

		if (folderStructure == null)
		{
			folderStructureDestination = fRootOut;
		}
		else
		{
			folderStructureDestination = fRootOut.resolve(folderStructure);
		}

		if (!Files.exists(folderStructureDestination))
		{
			Files.createDirectories(folderStructureDestination);
		}

		Files.move(path, folderStructureDestination.resolve(in.getFileName()));
	}

	/**
	 * Copy a rename file to a specific folder
	 * @param file
	 * @param folderOut
	 * @param newName
	 */
	public static void copyFile(File file, String folderOut, String newName)
	{
		File dest = new File(folderOut + File.separator + newName);
		try
		{
			FileUtils.copyFile(file, dest);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
