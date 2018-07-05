/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.junit.file;

import com.pronoia.junit.asserts.file.DirectoryAssert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The TestDirectory Rule creates a directory and ensures it is empty before a test.
 *
 * The rule can also delete the test directory after a test.
 */
public class TestDirectory extends ExternalResource {
  public static final String DEFAULT_DIRECTORY = "target/test-files";

  final Logger log = LoggerFactory.getLogger(this.getClass());

  final File directory;
  boolean deleteAfterTest = false;

  /**
   * Create a {@link TestDirectory} in the Maven target directory.
   */
  public TestDirectory() {
    this(DEFAULT_DIRECTORY);
  }

  /**
   * Create a test directory using the specified directory name.
   *
   * @param directoryName test directory name.
   */
  public TestDirectory(String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      throw new IllegalArgumentException("The directory name argument for the test directory cannot be null or empty");
    }

    this.directory = new File(directoryName);

    if (directory.exists()) {
      if (!directory.isDirectory()) {
        throw new IllegalArgumentException(String.format("The specified directory name '%s' does not refer to a directory", directoryName));
      }
    }
  }

  /**
   * Create a {@link TestDirectory} using the specified directory.
   *
   * @param directory the test directory {@link File} object.
   */
  public TestDirectory(File directory) {
    if (directory == null) {
      throw new IllegalArgumentException("The directory argument for the test directory cannot be null");
    } else if (directory.exists()) {
      if (!directory.isDirectory()) {
        throw new IllegalArgumentException(String.format("The test directory File object argument '%s' does not refer to a directory", directory));
      }

    }
    this.directory = directory;
  }

  /**
   * Delete a file with the given name in test directory.
   *
   * @param fileName the name of the file to delete.
   */
  public File getFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to get a File object from the '%s' directory - the filename argument cannot be null or empty", directory));
    }

    File tmpFile = new File(toFile(), fileName);
    if (!tmpFile.exists()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' directory - the file does not exist", fileName, directory));
    } else if (!tmpFile.isFile()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' directory -  the filename does not refer to a file", fileName, directory));
    }

    return tmpFile;
  }

  /**
   * Delete a file with the given name in test directory.
   *
   * @param fileName the name of the file to delete.
   */
  public File getFileFromChildDirectory(String childDirectoryName, String fileName) {
    if (childDirectoryName == null || childDirectoryName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to get a File object from the child directory in the '%s' directory - the child directory name argument cannot be null or empty", directory));
    } else if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to get a File object from the '%s' child directory in the '%s' directory - the filename argument cannot be null or empty", childDirectoryName, directory));
    }

    File tmpDirectory = new File(toFile(), childDirectoryName);
    if (!tmpDirectory.exists()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the child directory does not exist", fileName, childDirectoryName, directory));
    } else if (!tmpDirectory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the child directory name does not refer to a directory", fileName, childDirectoryName, directory));
    }

    File tmpFile = new File(tmpDirectory, fileName);
    if (!tmpFile.exists()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the file does not exist", fileName, childDirectoryName, directory));
    } else if (!tmpFile.isFile()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the filename does not refer to a file", fileName, childDirectoryName, directory));
    }

    return tmpFile;
  }

  /**
   * Delete a file with the given name in test directory.
   *
   * @param fileName the name of the file to delete.
   */
  public void deleteFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to delete a file from the '%s' directory - the filename argument cannot be null or empty", directory));
    }

    File file = new File(toFile(), fileName);
    if (!file.exists()) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' file from the '%s' directory - the file does not exist", fileName, directory));
    } else if (!file.isFile()) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' file from the '%s' directory - the filename does not refer to a file", fileName, directory));
    } else if (!file.delete()) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' file from the '%s' directory - File.delete() returned false", fileName, directory));
    }
  }

  /**
   * Read a file with the given name in test directory.
   *
   * @param fileName the name of the file to read.
   *
   * @return the contents of the file as a String
   */
  public String readFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to read a file in the '%s' directory - the filename argument cannot be null or empty", directory));
    }

    File file = new File(toFile(), fileName);
    if (!file.exists()) {
      throw new IllegalStateException(String.format("Failed to read the '%s' file in the '%s' directory - the file does not exist", fileName, directory));
    } else if (!file.isFile()) {
      throw new IllegalStateException(String.format("Failed to read the '%s' file in the '%s' directory - the filename does not refer to a file", fileName, directory));
    }

    try {
      return FileUtils.readFileToString(file.getAbsoluteFile(), Charset.defaultCharset());
    } catch (IOException readEx) {
      throw new IllegalStateException(String.format("Failed to read the '%s' file in the '%s' directory", fileName, directory), readEx);
    }
  }

  /**
   * Read a file with the given name in test directory.
   *
   * @param fileName the name of the file to read.
   *
   * @return the contents of the file as a List of Strings
   */
  public List<String> readFileLines(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to read the lines of a file in the '%s' directory - the filename argument cannot be null or empty", directory));
    }

    File file = new File(toFile(), fileName);
    if (!file.exists()) {
      throw new IllegalStateException(String.format("Failed to read the lines of the '%s' file in the '%s' directory - the file does not exist", fileName, directory));
    } else if (!file.isFile()) {
      throw new IllegalStateException(String.format("Failed to read the lines of the '%s' file in the '%s' directory - the filename does not refer to a file", fileName, directory));
    }

    try {
      return FileUtils.readLines(file, Charset.defaultCharset());
    } catch (IOException readEx) {
      throw new IllegalStateException(String.format("Failed to read the lines of the '%s' file in the '%s' directory", fileName, directory), readEx);
    }
  }

  /**
   * Create a new file with the given name in test directory.
   *
   * @param fileName the name of the new file.
   *
   * @return a {@link File} object for the new file
   */
  public File newFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to create a new file in the '%s' directory - the filename argument cannot be null or empty", directory));
    }

    File file = new File(toFile(), fileName);
    try {
      if (!file.createNewFile()) {
        throw new IllegalStateException(String.format("Failed to create the '%s' file in the '%s' directory - File.createNewFile returned false", fileName, directory));
      }
    } catch (IOException ioEx) {
      throw new IllegalStateException(String.format("Failed to create the '%s' file in the '%s' directory", fileName, directory), ioEx);
    }

    return file;
  }

  /**
   * Create a new file with the given name and body in test directory.
   *
   * @param fileName the name of the new file.
   * @param body     the body for the new file
   *
   * @return a {@link File} object for the new file
   */
  public File newFileWithBody(String fileName, String body) {
    File file = newFile(fileName);
    FileWriter writer;
    try {
      writer = new FileWriter(file);
      writer.append(body);
      writer.flush();
    } catch (IOException writeEx) {
      final String errorMessage = String.format("Failed to write body to new '%s' file in '%s' directory", file, directory);
      throw new IllegalStateException(errorMessage, writeEx);
    }

    return file;
  }

  /**
   * Copy a file to the test directory.
   *
   * @param sourceFileName the name of the source file.
   */
  public void copyFile(String sourceFileName) {
    copyFile(new File(sourceFileName));
  }

  /**
   * Copy a file to the test directory.
   *
   * @param sourceFile the source file.
   */
  public void copyFile(File sourceFile) {
    if (sourceFile == null) {
      throw new IllegalArgumentException(String.format("Failed to copy a source file to the '%s' directory - the source file File object argument cannot be null", directory));
    } else if (!sourceFile.exists()) {
      throw new IllegalArgumentException(String.format("Failed to copy the '%s' source file to the '%s' directory - the source file does not exist", sourceFile, directory));
    } else if (!sourceFile.isFile()) {
      throw new IllegalArgumentException(String.format("Failed to copy the '%s' source file to the '%s' directory - the source file does not refer to a file", sourceFile, directory));
    }

    try {
      FileUtils.copyFileToDirectory(sourceFile, directory);
    } catch (IOException ioEx) {
      final String errorMessage = String.format("Failed to copy the '%s' source file to the '%s' directory", sourceFile, directory);
      throw new IllegalStateException(errorMessage, ioEx);
    }
  }

  /**
   * Copy a file to the test directory with the specified new file name.
   *
   * @param sourceFileName the name of the source file.
   * @param newFileName    the name of the new file.
   */
  public void copyFile(String sourceFileName, String newFileName) {
    copyFile(new File(sourceFileName), newFileName);
  }

  /**
   * Copy a file to the test directory with the specified new file name.
   *
   * @param sourceFile  the source file.
   * @param newFileName the name of the new file.
   */
  public void copyFile(File sourceFile, String newFileName) {
    if (sourceFile == null) {
      throw new IllegalArgumentException(String.format("Failed to copy a source file to the '%s' directory as the '%s' file - the File argument cannot be null", directory, newFileName));
    } else if (!sourceFile.exists()) {
      throw new IllegalArgumentException(String.format("Failed to copy the '%s' source file to the '%s' directory as the '%s' file - the source file does not exist", sourceFile, directory, newFileName));
    } else if (newFileName == null || newFileName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to copy the '%s' source file to the '%s' directory with a new filename - the new filename argument cannot be null or empty", sourceFile, directory));
    }

    try {
      FileUtils.copyFile(sourceFile, new File(toFile(), newFileName));
    } catch (IOException ioEx) {
      final String errorMessage = String.format("Failed to copy the '%s' source file to the '%s' directory under the new name %s", sourceFile, directory, newFileName);
      throw new IllegalStateException(errorMessage, ioEx);
    }
  }

  /**
   * Delete a directory with the given name in test directory.
   *
   * @param childDirectoryName the name of the directory to delete.
   */
  public void deleteDirectory(String childDirectoryName) {
    if (childDirectoryName == null || childDirectoryName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to delete a child directory from the '%s' directory - the child directory name argument cannot be null or empty", directory));
    }

    File tmpDirectory = new File(toFile(), childDirectoryName);
    if (!tmpDirectory.exists()) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' child directory from the '%s' directory - the child directory does not exist", childDirectoryName, directory));
    } else if (!tmpDirectory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' child directory from the '%s' directory - the child directory name does not refer to a directory", childDirectoryName, directory));
    }

    try {
      FileUtils.deleteDirectory(tmpDirectory);
    } catch (IOException deleteEx) {
      throw new IllegalStateException(String.format("Failed to delete the '%s' child directory from the '%s' directory", childDirectoryName, directory), deleteEx);
    }
  }

  /**
   * Get a File object for the directory with the given name in test directory.
   *
   * @param childDirectoryName the name of the directory to get.
   */
  public File getDirectory(String childDirectoryName) {
    if (childDirectoryName == null || childDirectoryName.isEmpty()) {
      throw new IllegalArgumentException(String.format("Failed to get a File object for a child directory from the '%s' directory - the child directory name argument cannot be null or empty", directory));
    }

    File tmpDirectory = new File(toFile(), childDirectoryName);
    if (!tmpDirectory.exists()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the %s child directory from the '%s' directory - the child directory does not exist", childDirectoryName, directory));
    } else if (!tmpDirectory.isDirectory()) {
      throw new IllegalStateException(String.format("Failed to get a File object for the %s child directory from the '%s' directory - the child directory name does not refer to a directory", childDirectoryName, directory));
    }

    return tmpDirectory;
  }

  /**
   * Returns a new directory with the given name in the test directory.
   *
   * @param childDirectoryName the name of the new directory.
   *
   * @return a {@link File} object for the new directory
   */
  public File newDirectory(String childDirectoryName) {
    File childDirectory = new File(toFile(), childDirectoryName);
    if (!childDirectory.mkdirs()) {
      throw new IllegalStateException(String.format("Failed to create a new '%s' child directory in the '%s' directory  - File.mkdirs() returned false", childDirectoryName, directory));
    }

    return childDirectory;
  }

  /**
   * Get the File object for the test directory.
   *
   * @return the File object for the test directory.
   */
  public File toFile() {
    if (directory == null) {
      throw new IllegalStateException("The TestDirectory File object is null");
    }

    return directory;
  }


  public boolean isDeleteAfterTest() {
    return deleteAfterTest;
  }

  public void setDeleteAfterTest(boolean deleteAfterTest) {
    this.deleteAfterTest = deleteAfterTest;
  }

  public TestDirectory deleteAfterTest() {
    setDeleteAfterTest(true);

    return this;
  }

  public TestDirectory deleteAfterTest(boolean delete) {
    setDeleteAfterTest(delete);

    return this;
  }

  @Override
  protected void before() {
    initialize();
  }

  @Override
  protected void after() {
    if (deleteAfterTest && directory != null) {
      cleanup();
    }
  }

  @Override
  public String toString() {
    return directory.toString();
  }

  public void initialize() {
    if (directory == null) {
      throw new IllegalStateException("The TestDirectory File object is null");
    }

    if (directory.exists()) {
      cleanup();
    } else {
      if (!directory.mkdirs()) {
        throw new IllegalStateException("Failed to create " + directory + " directory - File.mkdirs() returned false");
      }
    }
  }

  public void cleanup() {
    if (directory.exists()) {
      String[] entries = directory.list();
      if (entries.length > 0) {
        log.info("Clearing test directory {} contents {}", directory, entries);
        try {
          FileUtils.cleanDirectory(directory);
        } catch (IOException cleanEx) {
          throw new IllegalStateException("Failed to clean exiting " + directory.toString() + " directory", cleanEx);
        }
      }
    }

  }

  /**
   * Asserts that a {@link TestDirectory} does not contain any children.
   */
  public void assertIsEmpty() {
    DirectoryAssert.assertDirectoryIsEmpty(directory);
  }

  /**
   * Asserts that a {@link TestDirectory} contains children.
   */
  public void assertNotEmpty() {
    DirectoryAssert.assertDirectoryNotEmpty(directory);
  }

  /**
   * Asserts that a {@link TestDirectory} contains the child directory and that the child directory does not contain any children.
   */
  public void assertChildDirectoryIsEmpty(String childDirectoryName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryIsEmpty(new File(directory, childDirectoryName));
  }

  /**
   * Asserts that a {@link TestDirectory} contains the child directory and that the child directory contains children.
   */
  public void assertChildDirectoryNotEmpty(String childDirectoryName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryNotEmpty(new File(directory, childDirectoryName));
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of children.
   *
   * @param expectedChildCount expected number of children in directory
   */
  public void assertChildCountEquals(int expectedChildCount) {
    DirectoryAssert.assertDirectoryChildCountEquals(directory, expectedChildCount);
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of files.
   *
   * @param expectedFileCount expected number of files in directory
   */
  public void assertFileCountEquals(int expectedFileCount) {
    DirectoryAssert.assertDirectoryChildFileCountEquals(directory, expectedFileCount);
  }

  /**
   * Asserts that a {@link TestDirectory} contains a specific number of child directories.
   *
   * @param expectedDirectoryCount expected number of child directories in directory
   */
  public void assertChildDirectoryCountEquals(int expectedDirectoryCount) {
    DirectoryAssert.assertDirectoryChildFileCountEquals(directory, expectedDirectoryCount);
  }

  /**
   * Asserts that a child directory in the {@link TestDirectory} contains a specific number of children.
   *
   * @param expectedChildCount expected number of children in directory
   */
  public void assertChildCountInChildDirectoryEquals(String childDirectoryName, int expectedChildCount) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryChildCountEquals(new File(directory, childDirectoryName), expectedChildCount);
  }

  /**
   * Asserts that a child directory in the {@link TestDirectory} contains a specific number of files.
   *
   * @param expectedFileCount expected number of files in directory
   */
  public void assertFileCountInChildDirectoryEquals(String childDirectoryName, int expectedFileCount) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryChildFileCountEquals(new File(directory, childDirectoryName), expectedFileCount);
  }

  /**
   * Asserts that a child directory in the {@link TestDirectory} contains a specific number of child directories.
   *
   * @param expectedDirectoryCount expected number of child directories in directory
   */
  public void assertChildDirectoryInChildDirectoryCountEquals(String childDirectoryName, int expectedDirectoryCount) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryChildFileCountEquals(new File(directory, childDirectoryName), expectedDirectoryCount);
  }


  /**
   * Asserts that a file exists in the {@link TestDirectory}.
   *
   * @param fileName expected file name
   */
  public void assertContainsFile(final String fileName) {
    DirectoryAssert.assertDirectoryContainsFile(directory, fileName);
  }

  /**
   * Asserts that a file does not exist in the {@link TestDirectory}.
   *
   * @param fileName expected file name
   */
  public void assertNotContainsFile(final String fileName) {
    DirectoryAssert.assertDirectoryNotContainsFile(directory, fileName);
  }

  /**
   * Asserts that a directory exists in the {@link TestDirectory}.
   *
   * @param directoryName expected directory name
   */
  public void assertContainsDirectory(final String directoryName) {
    DirectoryAssert.assertDirectoryContainsDirectory(directory, directoryName);
  }

  /**
   * Asserts that a directory does not exist in the {@link TestDirectory}.
   *
   * @param directoryName expected directory name
   */
  public void assertNotContainsDirectory(final String directoryName) {
    DirectoryAssert.assertDirectoryNotContainsDirectory(directory, directoryName);
  }

  /**
   * Asserts that a file exists in the {@link TestDirectory}.
   *
   * @param fileName expected file name
   */
  public void assertContainsFileInChildDirectory(final String childDirectoryName, final String fileName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryContainsFile(new File(directory, childDirectoryName), fileName);
  }

  /**
   * Asserts that a file does not exist in the {@link TestDirectory}.
   *
   * @param fileName expected file name
   */
  public void assertNotContainsFileInChildDirectory(final String childDirectoryName, final String fileName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryNotContainsFile(new File(directory, childDirectoryName), fileName);
  }

  /**
   * Asserts that a directory exists in the {@link TestDirectory}.
   *
   * @param directoryName expected directory name
   */
  public void assertContainsDirectoryInChildDirectory(final String childDirectoryName, final String directoryName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryContainsDirectory(new File(directory, childDirectoryName), directoryName);
  }

  /**
   * Asserts that a directory does not exist in the {@link TestDirectory}.
   *
   * @param directoryName expected directory name
   */
  public void assertNotContainsDirectoryInChildDirectory(final String childDirectoryName, final String directoryName) {
    assertContainsDirectory(childDirectoryName);

    DirectoryAssert.assertDirectoryNotContainsDirectory(new File(directory, childDirectoryName), directoryName);
  }
}