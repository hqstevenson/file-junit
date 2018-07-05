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
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the  class.
 */
public class DirectoryAssertTest {
  static final File TEST_DIRECTORY = new File("target/directory-test");
  static final String TEST_CHILD_DIRECTORY_NAME = "child-directory";
  static final File TEST_CHILD_DIRECTORY = new File(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);
  static final String TEST_FILE_NAME = "test.txt";
  static final File TEST_FILE = new File(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);

  @Before
  public void setUp() throws Exception {
    if (TEST_DIRECTORY.exists()) {
      FileUtils.deleteQuietly(TEST_DIRECTORY);
    }

    TEST_DIRECTORY.mkdirs();
  }

  @After
  public void tearDown() throws Exception {
    FileUtils.deleteQuietly(TEST_DIRECTORY);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryExists() throws Exception {
    DirectoryAssert.assertDirectoryExists(TEST_DIRECTORY);

    try {
      DirectoryAssert.assertDirectoryExists(TEST_CHILD_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    TEST_FILE.createNewFile();

    try {
      DirectoryAssert.assertDirectoryExists(TEST_FILE);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a directory", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryExistsWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryExists(directoryName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryExists(TEST_DIRECTORY.toString());
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotExists() throws Exception {
    DirectoryAssert.assertDirectoryNotExists(TEST_CHILD_DIRECTORY);

    TEST_CHILD_DIRECTORY.mkdirs();
    TEST_FILE.createNewFile();

    for (File fileOrDirectory : Arrays.asList(TEST_CHILD_DIRECTORY, TEST_FILE)) {
      try {
        DirectoryAssert.assertDirectoryNotExists(fileOrDirectory);
        fail("Assertion should have failed");
      } catch (AssertionError expectedFailure) {
        final String expectedAssertionMessage = String.format("%s exists", fileOrDirectory);
        assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
      }
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotExistsWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryNotExists(directoryName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryNotExists(TEST_CHILD_DIRECTORY.toString());
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryIsEmpty() throws Exception {
    DirectoryAssert.assertDirectoryIsEmpty(TEST_DIRECTORY);

    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY);

    TEST_FILE.createNewFile();
    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is not empty - contains [%s]", TEST_CHILD_DIRECTORY, TEST_FILE_NAME);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_FILE);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a directory", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryIsEmptyWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryIsEmpty(directoryName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryIsEmpty(TEST_DIRECTORY.toString());

    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY.toString());

    TEST_FILE.createNewFile();
    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_CHILD_DIRECTORY.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is not empty - contains [%s]", TEST_CHILD_DIRECTORY, TEST_FILE_NAME);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    try {
      DirectoryAssert.assertDirectoryIsEmpty(TEST_FILE.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a directory", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotEmpty() throws Exception {
    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is empty", TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_CHILD_DIRECTORY);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryNotEmpty(TEST_DIRECTORY);
    DirectoryAssert.assertDirectoryNotEmpty(TEST_CHILD_DIRECTORY);

    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_FILE);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a directory", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotEmptyWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryNotEmpty(directoryName);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_DIRECTORY.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s is empty", TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_CHILD_DIRECTORY.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryNotEmpty(TEST_DIRECTORY.toString());
    DirectoryAssert.assertDirectoryNotEmpty(TEST_CHILD_DIRECTORY.toString());

    try {
      DirectoryAssert.assertDirectoryNotEmpty(TEST_FILE.toString());
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s does not refer to a directory", TEST_FILE);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildCount() throws Exception {
    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY, 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY, 1);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY, 2);
    }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildCountWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryChildCountEquals(directoryName, 0);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY.toString(), 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY.toString(), 1);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildCountEquals(TEST_DIRECTORY.toString(), 2);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildFileCount() throws Exception {
    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY, 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY, 0);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY, 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildFileCountWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryChildFileCountEquals(directoryName, 0);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY.toString(), 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY.toString(), 0);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildFileCountEquals(TEST_DIRECTORY.toString(), 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildDirectoryCount() throws Exception {
    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY, 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY, 1);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY, 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryChildDirectoryCountWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryChildDirectoryCountEquals(directoryName, 0);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY.toString(), 0);

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY.toString(), 1);

    new File(TEST_DIRECTORY, TEST_FILE_NAME).createNewFile();
    DirectoryAssert.assertDirectoryChildDirectoryCountEquals(TEST_DIRECTORY.toString(), 1);
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryContainsFile() throws Exception {
    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_DIRECTORY, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", TEST_FILE_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a file", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryContainsFile(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);

    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_CHILD_DIRECTORY, "non-existent.txt");
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", "non-existent.txt", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryContainsFileWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryContainsFile(directoryName, TEST_FILE_NAME);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_DIRECTORY.toString(), TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", TEST_FILE_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a file", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryContainsFile(TEST_CHILD_DIRECTORY.toString(), TEST_FILE_NAME);

    try {
      DirectoryAssert.assertDirectoryContainsFile(TEST_CHILD_DIRECTORY.toString(), "non-existent.txt");
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s does not exist in directory %s", "non-existent.txt", TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotContainsFile() throws Exception {
    DirectoryAssert.assertDirectoryNotContainsFile(TEST_DIRECTORY, TEST_FILE_NAME);

    TEST_CHILD_DIRECTORY.mkdirs();
    // This test will pass because the entry is not a file (it is a directory), but a warning will be logged
    DirectoryAssert.assertDirectoryNotContainsFile(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);

    DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);

    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY, "non-existent.txt");
    try {
      DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s exists in directory %s", TEST_FILE_NAME, TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotContainsFileWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryNotContainsFile(directoryName, TEST_FILE_NAME);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryNotContainsFile(TEST_DIRECTORY.toString(), TEST_FILE_NAME);

    TEST_CHILD_DIRECTORY.mkdirs();
    // This test will pass because the entry is not a file (it is a directory), but a warning will be logged
    DirectoryAssert.assertDirectoryNotContainsFile(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);

    DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY.toString(), TEST_FILE_NAME);

    TEST_FILE.createNewFile();
    DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY.toString(), "non-existent.txt");
    try {
      DirectoryAssert.assertDirectoryNotContainsFile(TEST_CHILD_DIRECTORY.toString(), TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("File %s exists in directory %s", TEST_FILE_NAME, TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryContainsDirectory() throws Exception {
    try {
      DirectoryAssert.assertDirectoryContainsDirectory(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist in directory %s", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryContainsDirectory(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);

    TEST_FILE.createNewFile();
    try {
      DirectoryAssert.assertDirectoryContainsDirectory(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a directory", TEST_FILE_NAME, TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryContainsDirectoryWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryContainsDirectory(directoryName, TEST_CHILD_DIRECTORY_NAME);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    try {
      DirectoryAssert.assertDirectoryContainsDirectory(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s does not exist in directory %s", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }

    TEST_CHILD_DIRECTORY.mkdirs();
    DirectoryAssert.assertDirectoryContainsDirectory(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);

    TEST_FILE.createNewFile();
    try {
      DirectoryAssert.assertDirectoryContainsDirectory(TEST_CHILD_DIRECTORY.toString(), TEST_FILE_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("%s in directory %s does not refer to a directory", TEST_FILE_NAME, TEST_CHILD_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }


  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotContainsDirectory() throws Exception {
    DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);

    TEST_CHILD_DIRECTORY.mkdirs();

    TEST_FILE.createNewFile();
    // This test will pass because the entry is not a directory (it is a file), but a warning will be logged
    DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_CHILD_DIRECTORY, TEST_FILE_NAME);

    try {
      DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_DIRECTORY, TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s exists in directory %s", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }

  /**
   * Description of test.
   *
   * @throws Exception in the event of a test error.
   */
  @Test
  public void testAssertDirectoryNotContainsDirectoryWithString() throws Exception {
    for (String directoryName : Arrays.asList("", null)) {
      try {
        DirectoryAssert.assertDirectoryNotContainsDirectory(directoryName, TEST_CHILD_DIRECTORY_NAME);
        fail("Operation should have failed");
      } catch (IllegalArgumentException expectedEx) {
        final String expectedMessage = "Directory name argument cannot be null or empty";
        assertEquals(expectedMessage, expectedEx.getMessage());
      }
    }

    DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);

    TEST_CHILD_DIRECTORY.mkdirs();

    TEST_FILE.createNewFile();
    // This test will pass because the entry is not a directory (it is a file), but a warning will be logged
    DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_CHILD_DIRECTORY.toString(), TEST_FILE_NAME);

    try {
      DirectoryAssert.assertDirectoryNotContainsDirectory(TEST_DIRECTORY.toString(), TEST_CHILD_DIRECTORY_NAME);
      fail("Assertion should have failed");
    } catch (AssertionError expectedFailure) {
      final String expectedAssertionMessage = String.format("Directory %s exists in directory %s", TEST_CHILD_DIRECTORY_NAME, TEST_DIRECTORY);
      assertEquals("Unexpected assertion message", expectedAssertionMessage, expectedFailure.getMessage());
    }
  }
}