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

package com.pronoia.junit.asserts.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assertions for {@link File} objects referring to directories in the filesystem.
 */
public final class DirectoryAssert {
  static final Logger LOG = LoggerFactory.getLogger(DirectoryAssert.class);

  private DirectoryAssert() {}
  /**
   * Asserts that a {@link File} exists and refers to a directory.
   *
   * @param directoryName expected directory name
   */
  public static void assertDirectoryExists(final String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      throw new IllegalArgumentException("Directory name argument cannot be null or empty");
    }

    assertDirectoryExists(new File(directoryName));
  }

  /**
   * Asserts that a {@link File} does not exist.
   *
   * @param directoryName expected directory name
   */
  public static void assertDirectoryNotExists(final String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      throw new IllegalArgumentException("Directory name argument cannot be null or empty");
    }

    assertDirectoryNotExists(new File(directoryName));
  }

  /**
   * Asserts that a {@link File} exists and refers to a directory.
   *
   * @param directory expected directory
   */
  public static void assertDirectoryExists(final File directory) {
    assertTrue( String.format("Directory %s does not exist", directory), directory.exists());
    assertTrue( String.format("%s does not refer to a directory", directory), directory.isDirectory());
  }

  /**
   * Asserts that a {@link File} does not exist.
   *
   * @param directory expected directory
   */
  public static void assertDirectoryNotExists(final File directory) {
    assertFalse( String.format("%s exists", directory), directory.exists());
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and does not contain any children.
   *
   * @param directoryName expected directory name
   */
  public static void assertDirectoryIsEmpty(final String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      throw new IllegalArgumentException("Directory name argument cannot be null or empty");
    }

    assertDirectoryIsEmpty(new File(directoryName));
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and does not contain any children.
   *
   * @param directory expected directory
   */
  public static void assertDirectoryIsEmpty(final File directory) {
    assertDirectoryExists(directory);

    List<String> directoryEntries = Arrays.asList(directory.list());
    assertTrue( String.format("Directory %s is not empty - contains %s", directory, directoryEntries), directoryEntries.isEmpty());
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains children.
   *
   * @param directoryName expected directory name
   */
  public static void assertDirectoryNotEmpty(final String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      throw new IllegalArgumentException("Directory name argument cannot be null or empty");
    }

    assertDirectoryNotEmpty(new File(directoryName));
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains children.
   *
   * @param directory expected directory
   */
  public static void assertDirectoryNotEmpty(final File directory) {
    assertDirectoryExists(directory);

    assertTrue( String.format("Directory %s is empty", directory), directory.list().length > 0);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of children.
   *
   * @param directoryName expected directory name
   * @param expectedChildCount expected number of children in directory
   */
  public static void assertDirectoryChildCountEquals(final String directoryName, int expectedChildCount) {
    assertDirectoryExists(directoryName);

    assertDirectoryChildCountEquals(new File(directoryName), expectedChildCount);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of children.
   *
   * @param directory expected directory
   * @param expectedChildCount expected number of children in directory
   */
  public static void assertDirectoryChildCountEquals(final File directory, int expectedChildCount) {
    assertDirectoryExists(directory);

    assertEquals( String.format("Unexpected number of children in directory %s", directory), expectedChildCount, directory.list().length);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of files.
   *
   * @param directoryName expected directory name
   * @param expectedFileCount expected number of files in directory
   */
  public static void assertDirectoryChildFileCountEquals(final String directoryName, int expectedFileCount) {
    assertDirectoryExists(directoryName);

    assertDirectoryChildFileCountEquals(new File(directoryName), expectedFileCount);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of files.
   *
   * @param directory expected directory
   * @param expectedFileCount expected number of files in directory
   */
  public static void assertDirectoryChildFileCountEquals(final File directory, int expectedFileCount) {
    assertDirectoryExists(directory);

    int actual = 0;
    for (File child : directory.listFiles()) {
      if (child.isFile()) {
        ++actual;
      }
    }

    assertEquals( String.format("Unexpected number of files in directory %s", directory), expectedFileCount, actual);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of child directories.
   *
   * @param directoryName expected directory name
   * @param expectedDirectoryCount expected number of child directories in directory
   */
  public static void assertDirectoryChildDirectoryCountEquals(final String directoryName, int expectedDirectoryCount) {
    assertDirectoryExists(directoryName);

    assertDirectoryChildDirectoryCountEquals(new File(directoryName), expectedDirectoryCount);
  }

  /**
   * Asserts that a {@link File} exists, refers to a directory and contains a specific number of child directories.
   *
   * @param directory expected directory
   * @param expectedDirectoryCount expected number of child directories in directory
   */
  public static void assertDirectoryChildDirectoryCountEquals(final File directory, int expectedDirectoryCount) {
    assertDirectoryExists(directory);

    int actual = 0;
    for (File child : directory.listFiles()) {
      if (child.isDirectory()) {
        ++actual;
      }
    }

    assertEquals( String.format("Unexpected number of files in directory %s", directory), expectedDirectoryCount, actual);
  }

  /**
   * Asserts that a {@link File} exists in a directory and is a file.
   *
   * @param directoryName directory to check for file
   * @param fileName expected file name
   */
  public static void assertDirectoryContainsFile(final String directoryName, final String fileName) {
    assertDirectoryExists(directoryName);

    assertDirectoryContainsFile(new File(directoryName), fileName);
  }

  /**
   * Asserts that a {@link File} exists in a directory and is a file.
   *
   * @param directory directory to check for file
   * @param fileName expected file name
   */
  public static void assertDirectoryContainsFile(final File directory, final String fileName) {
    assertDirectoryExists(directory);

    File expected = new File(directory, fileName);
    assertTrue( String.format("File %s does not exist in directory %s", fileName, directory), expected.exists());
    assertTrue( String.format("%s in directory %s does not refer to a file", fileName, directory), expected.isFile());
  }

  /**
   * Asserts that a {@link File} does not exist in a directory.
   *
   * @param directoryName directory to check for file
   * @param fileName expected file name
   */
  public static void assertDirectoryNotContainsFile(final String directoryName, final String fileName) {
    assertDirectoryExists(directoryName);

    assertDirectoryNotContainsFile(new File(directoryName), fileName);
  }

  /**
   * Asserts that a {@link File} does not exist in a directory.
   *
   * @param directory directory to check for file
   * @param fileName expected file name
   */
  public static void assertDirectoryNotContainsFile(final File directory, final String fileName) {
    assertDirectoryExists(directory);

    File expected = new File(directory, fileName);
    if (expected.exists()) {
      if (expected.isFile()) {
        fail( String.format("File %s exists in directory %s", fileName, directory));
      } else {
        LOG.warn("The directory {} contains {}, but it is not a file", directory, fileName);
      }
    }
  }

  /**
   * Asserts that a {@link File} exists in a directory and is a directory.
   *
   * @param directoryName directory to check for child directory
   * @param childDirectoryName expected child directory name
   */
  public static void assertDirectoryContainsDirectory(final String directoryName, final String childDirectoryName) {
    assertDirectoryExists(directoryName);

    assertDirectoryContainsDirectory(new File(directoryName), childDirectoryName);
  }

  /**
   * Asserts that a {@link File} exists in a directory and is a directory.
   *
   * @param directory directory to check for file
   * @param directoryName expected directory name
   */
  public static void assertDirectoryContainsDirectory(final File directory, final String directoryName) {
    assertDirectoryExists(directory);

    File expected = new File(directory, directoryName);
    assertTrue( String.format("Directory %s does not exist in directory %s", directoryName, directory), expected.exists());
    assertTrue( String.format("%s in directory %s does not refer to a directory", directoryName, directory), expected.isDirectory());
  }

  /**
   * Asserts that a {@link File} does not exist in a directory.
   *
   * @param directoryName directory to check for child directory
   * @param childDirectoryName child directory name
   */
  public static void assertDirectoryNotContainsDirectory(final String directoryName, final String childDirectoryName) {
    assertDirectoryExists(directoryName);

    assertDirectoryNotContainsDirectory(new File(directoryName), childDirectoryName);
  }

  /**
   * Asserts that a {@link File} does not exist in a directory.
   *
   * @param directory directory to check for file
   * @param directoryName expected directory name
   */
  public static void assertDirectoryNotContainsDirectory(final File directory, final String directoryName) {
    assertDirectoryExists(directory);

    File expected = new File(directory, directoryName);
    if (expected.exists()) {
      if (expected.isDirectory()) {
        fail( String.format("Directory %s exists in directory %s", directoryName, directory));
      } else {
        LOG.warn("The directory {} contains {}, but it is not a directory", directory, directoryName);
      }
    }
  }

}
