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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.pronoia.junit.asserts.file.FileAssert;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the  class.
 */
public class TestDirectoryTest {
  static final String TEST_DATA_DIRECTORY_NAME = "src/test/data";

  static final String TEST_CHILD_DIRECTORY_NAME = "child-directory";
  static final String TEST_FILE_NAME = "test.txt";

  static final File TEST_DATA_DIRECTORY = new File(TEST_DATA_DIRECTORY_NAME);
  static final File TEST_DATA_FILE = new File(TEST_DATA_DIRECTORY, TEST_FILE_NAME);

  @Rule
  public TestDirectory instance = new TestDirectory();

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetFile() throws Exception {
    for (String filename : Arrays.asList("", null)) {
      try {
        instance.getFile(filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to get a File object from the '%s' directory - the filename argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.getFile(TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to get a File object for the '%s' file from the '%s' directory - the file does not exist", TEST_FILE_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    File expected = instance.newFile(TEST_FILE_NAME);
    File actual = instance.getFile(TEST_FILE_NAME);
    assertEquals(expected, actual);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testGetFileFromChildDirectory() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        instance.getFileFromChildDirectory(directoryName, TEST_FILE_NAME);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to get a File object from the child directory in the '%s' directory - the child directory name argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    for (String filename : Arrays.asList("", null)) {
      try {
        instance.getFileFromChildDirectory(TEST_CHILD_DIRECTORY_NAME, filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to get a File object from the '%s' child directory in the '%s' directory - the filename argument cannot be null or empty", TEST_CHILD_DIRECTORY_NAME, instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.getFileFromChildDirectory(TEST_CHILD_DIRECTORY_NAME, TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the child directory does not exist", TEST_FILE_NAME, TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    File tmpDirectory = instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      instance.getFileFromChildDirectory(TEST_CHILD_DIRECTORY_NAME, TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to get a File object for the '%s' file from the '%s' child directory in the '%s' directory - the file does not exist", TEST_FILE_NAME, TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    File expected = new File(tmpDirectory, TEST_FILE_NAME);
    expected.createNewFile();
    File actual = instance.getFileFromChildDirectory(TEST_CHILD_DIRECTORY_NAME, TEST_FILE_NAME);
    assertEquals(expected, actual);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDeleteFile() throws Exception {
    for (String filename : Arrays.asList("", null)) {
      try {
        instance.deleteFile(filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to delete a file from the '%s' directory - the filename argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.deleteFile(TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to delete the '%s' file from the '%s' directory - the file does not exist", TEST_FILE_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      instance.deleteFile(TEST_CHILD_DIRECTORY_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to delete the '%s' file from the '%s' directory - the filename does not refer to a file", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    instance.deleteFile(TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void tesReadFile() throws Exception {
    for (String filename : Arrays.asList("", null)) {
      try {
        instance.readFile(filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to read a file in the '%s' directory - the filename argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.readFile(TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to read the '%s' file in the '%s' directory - the file does not exist", TEST_FILE_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      instance.readFile(TEST_CHILD_DIRECTORY_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to read the '%s' file in the '%s' directory - the filename does not refer to a file", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    final String expected = "Some Test Data One\nSome Test Data Two\nSome Test Data Three\nSome Test Data Four\nSome Test Data Five";

    instance.copyFile(TEST_DATA_FILE);

    assertEquals(expected, instance.readFile(TEST_FILE_NAME));
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testReadFileLine() throws Exception {
    for (String filename : Arrays.asList("", null)) {
      try {
        instance.readFileLines(filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to read the lines of a file in the '%s' directory - the filename argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.readFileLines(TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to read the lines of the '%s' file in the '%s' directory - the file does not exist", TEST_FILE_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    try {
      instance.readFileLines(TEST_CHILD_DIRECTORY_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to read the lines of the '%s' file in the '%s' directory - the filename does not refer to a file", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    final List<String> expected = Arrays.asList("Some Test Data One", "Some Test Data Two", "Some Test Data Three", "Some Test Data Four", "Some Test Data Five");

    instance.copyFile(TEST_DATA_FILE);

    assertEquals(expected, instance.readFileLines(TEST_FILE_NAME));
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testNewFile() throws Exception {
    for (String filename : Arrays.asList("", null)) {
      try {
        instance.newFile(filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to create a new file in the '%s' directory - the filename argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    instance.assertNotContainsFile(TEST_FILE_NAME);
    instance.newFile(TEST_FILE_NAME);
    instance.assertContainsFile(TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testCopyFile() throws Exception {
    try {
      instance.copyFile((File) null);
      fail("Operation should have thrown an exception");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("Failed to copy a source file to the '%s' directory - the source file File object argument cannot be null", instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    final String nonExistentFileName = "non-existent-file.txt";
    try {
      instance.copyFile( new File(nonExistentFileName));
      fail("Operation should have thrown an exception");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("Failed to copy the '%s' source file to the '%s' directory - the source file does not exist", nonExistentFileName, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.assertNotContainsFile(TEST_FILE_NAME);
    instance.copyFile(TEST_DATA_FILE);
    instance.assertContainsFile(TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testCopyFileWithNewName() throws Exception {
    final String newFileName = "new-" + TEST_FILE_NAME;

    try {
      instance.copyFile((File) null, newFileName);
      fail("Operation should have thrown an exception");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("Failed to copy a source file to the '%s' directory as the '%s' file - the File argument cannot be null", instance.directory, newFileName);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    final String nonExistentFileName = "non-existent-file.txt";
    try {
      instance.copyFile( new File(nonExistentFileName), newFileName);
      fail("Operation should have thrown an exception");
    } catch (IllegalArgumentException expectedEx) {
      final String expectedMessage = String.format("Failed to copy the '%s' source file to the '%s' directory as the '%s' file - the source file does not exist", nonExistentFileName, instance.directory, newFileName);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    for (String filename : Arrays.asList("", null)) {
      try {
        instance.copyFile(TEST_DATA_FILE, filename);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = String.format("Failed to copy the '%s' source file to the '%s' directory with a new filename - the new filename argument cannot be null or empty", TEST_DATA_FILE, instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    instance.assertNotContainsFile(newFileName);
    instance.copyFile(TEST_DATA_FILE, newFileName);
    instance.assertContainsFile(newFileName);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testCopyFileWithString() throws Exception {
    instance.assertNotContainsFile(TEST_FILE_NAME);
    instance.copyFile(TEST_DATA_FILE.toString());
    instance.assertContainsFile(TEST_FILE_NAME);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testCopyFileWithStringAndNewName() throws Exception {
    final String newFileName = "new-" + TEST_FILE_NAME;

    instance.assertNotContainsFile(newFileName);
    instance.copyFile(TEST_DATA_FILE.toString(), newFileName);
    instance.assertContainsFile(newFileName);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testNewDirectory() throws Exception {
    instance.assertNotContainsDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.assertContainsDirectory(TEST_CHILD_DIRECTORY_NAME);
  }


  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDeleteAfterTest() throws Exception {
    instance.setDeleteAfterTest(true);
    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.newFile(TEST_FILE_NAME);
    // Will have to manually check the directory for this one
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testNewFileWithBody() throws Exception {
    final String expectedBody = "Some Body";

    instance.assertNotContainsFile(TEST_FILE_NAME);
    instance.newFileWithBody(TEST_FILE_NAME, expectedBody);
    instance.assertContainsFile(TEST_FILE_NAME);

    String actualBody = instance.readFile(TEST_FILE_NAME);
    assertEquals(expectedBody, actualBody);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testDeleteDirectory() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        instance.deleteDirectory(directoryName);
        fail("Operation should have thrown an exception");
      } catch (IllegalArgumentException expectedEx) {
        String expectedMessage = String.format("Failed to delete a child directory from the '%s' directory - the child directory name argument cannot be null or empty", instance.directory);
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      instance.deleteDirectory(TEST_CHILD_DIRECTORY_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to delete the '%s' child directory from the '%s' directory - the child directory does not exist", TEST_CHILD_DIRECTORY_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }

    instance.newFile(TEST_FILE_NAME);
    try {
      instance.deleteDirectory(TEST_FILE_NAME);
      fail("Operation should have thrown an exception");
    } catch (IllegalStateException expectedEx) {
      String expectedMessage = String.format("Failed to delete the '%s' child directory from the '%s' directory - the child directory name does not refer to a directory", TEST_FILE_NAME, instance.directory);
      assertEquals(expectedMessage, expectedEx.getMessage());
    }


    instance.newDirectory(TEST_CHILD_DIRECTORY_NAME);
    instance.deleteDirectory(TEST_CHILD_DIRECTORY_NAME);
  }
}