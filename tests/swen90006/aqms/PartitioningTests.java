package swen90006.aqms;

import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * PartitioningTests class for some testing different equivalence classes when using JUnit.
 */
public class PartitioningTests {
    // AQMS instance shared across tests
    protected AQMS aqms;
    // Add data to ensure length is > 0
    List<Integer> data = Arrays.asList(1, 2, 3); // Example data

    @Before
    public void setUp() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException, InvalidDeviceIDException, NoSuchUserException {
        aqms = new AQMS();
        aqms.register("UserNameA", "Password1!", "1234");
    }

    @After
    public void tearDown() {
        // No specific teardown needed
    }


    // ----------- Tests for the Register Method --------------
    // EC1: Username unique
    @Test
    public void testRegisterUsernameUniqueness() throws Throwable {

        aqms.register("UserNameB", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserNameB"));


        try {
            aqms.register("UserNameA", "ValidPass1!", "1234");
            fail("Expected DuplicateUserException for duplicate username");
        } catch (DuplicateUserException e) {
            // Expected
        }
    }

    // EC2
    @Test
    public void testRegisterUsernameLength() throws Throwable {
        // On: Length = 4
        aqms.register("User", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("User"));

        // Off: Length = 3
        try {
            aqms.register("Usr", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for short username");
        } catch (InvalidUsernameException e) {
            // Expected
        }
    }

    // EC
    @Test
    public void testRegisterUsernameOnlyLetters() throws Throwable {

        aqms.register("User123", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("User123"));


        try {
            aqms.register("OnlyLetters", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for only letters in username");
        } catch (InvalidUsernameException e) {
            // Expected
        }
    }

    // EC4
    @Test
    public void testRegisterPasswordLength() throws Throwable {

        aqms.register("UserNameC", "Password1!", "1234");
        assertTrue(aqms.isUser("UserNameC"));

        // Off: Password length < 8 (invalid)
        try {
            aqms.register("UserNameD", "Pass1!", "1234");
            fail("Expected InvalidPasswordException for short password");
        } catch (InvalidPasswordException e) {
            //
        }
    }

    // EC5
    @Test
    public void testRegisterPasswordContainsLetterAndDigit() throws Throwable {
        // On: Password contains letters and digits (valid)
        aqms.register("UserNameE", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserNameE"));

        // Off: Password does not contain any letters (invalid)
        try {
            aqms.register("UserNameF", "12345678!", "1234");
            fail("Expected InvalidPasswordException for password without letters");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }

    // EC6
    @Test
    public void testRegisterPasswordContainsDigit() throws Throwable {
        // On: Password contains digits (valid)
        aqms.register("UserNameG", "Password1!", "1234");
        assertTrue(aqms.isUser("UserNameG"));

        // Off: Password does not contain any digits (invalid)
        try {
            aqms.register("UserNameH", "Password!", "1234");
            fail("Expected InvalidPasswordException for password without digits");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }

    // EC7
    @Test
    public void testRegisterPasswordSpecialCharacter() throws Throwable {
        // On: Password contains special character (valid)
        aqms.register("UserNameI", "Password1!", "1234");
        assertTrue(aqms.isUser("UserNameI"));

        // Off: Password does not contain special characters (invalid)
        try {
            aqms.register("UserNameJ", "Password12", "1234");
            fail("Expected InvalidPasswordException for password without special characters");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }

    // EC8
    @Test
    public void testRegisterDeviceIDLength() throws Throwable {
        // DeviceID length = 4 (int)
        aqms.register("UserNameK", "Password1!", "1234");
        assertTrue(aqms.isUser("UserNameK"));

        try {
            aqms.register("UserNameL", "Password1!", "123");
            fail("Expected InvalidDeviceIDException for short device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }

        // Off: DeviceID lengths > 4 (invalid)
        try {
            aqms.register("UserNameM", "Password1!", "12345");
            fail("Expected InvalidDeviceIDException for long device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }
    }


    @Test
    public void testRegisterDeviceIDContainsDigit() throws Throwable
    {
        // On: DeviceID contains digits (valid)
        aqms.register("UserNameN", "Password1!", "1234");
        assertTrue(aqms.isUser("UserNameN"));


        try {
            aqms.register("UserNameO", "Password1!", "abcd");
            fail("Expected InvalidDeviceIDException for device ID without digits");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }
    }

    // ----------- Tests for the login Method --------------

    // EC1
    @Test
    public void testLoginWithUnregisteredUsername() throws Throwable {
        try {
            aqms.login("NonExistentUser", "SomePassword", "1234");
            fail("Expected NoSuchUserException for unregistered username.");
        } catch (NoSuchUserException e) {
            //
        }
    }

    // EC2
    @Test
    public void testLoginWithIncorrectPassword() throws Throwable, InvalidPasswordException {
        aqms.login("UserNameA", "WrongPassword", "1234");
        fail("Expected InvalidPasswordException for incorrect password.");
    }

    // EC3
    @Test
    public void testLoginWithMatchingDeviceID() throws Throwable {
        //hh
        try {
            aqms.login("UserNameA", "Password1!", "1234");
            assertTrue(aqms.isAuthenticated("UserNameA"));
        } catch (Exception e) {
            fail("Unexpected exception during successful login: " + e.getMessage());
        }
    }

    // EC4
    @Test
    public void testLoginWithNonMatchingDeviceID() throws Throwable, InvalidDeviceIDException {
        aqms.login("UserNameA", "Password1!", "9999"); // Device ID does not match
        fail("Expected InvalidDeviceIDException for incorrect device ID.");
    }
    // ----------- Tests for assign_role Method --------------

    // EC1: Users does not exist
    @Test
    public void testAssignRoleToNonExistentUser() throws Throwable {
        try {
            aqms.assignRole("NonExistentUser", aqms.getRole("ADMIN"));
            fail("Expected NoSuchUserException for non-existent user.");
        } catch (NoSuchUserException e) {
            // Expected behavior
        }
    }

    // EC2:  is admin
    @Test
    public void testAssignRoleWithDowngrade() throws Throwable {
        // Assume that "UserNameA" is currently an admin
        aqms.assignRole("UserNameA", aqms.getRole("ADMIN"));
        assertEquals("admin", aqms.getRole("UserNameA"));

        // Now assign the user to a lower role (downgrade to user)
        aqms.assignRole("UserNameA", aqms.getRole("USER"));
        assertEquals("user", aqms.getRole("UserNameA"));
    }

    //
    @Test
    public void testAssignRoleWithoutDowngrade() throws Throwable {
        // Assign role to UserNameB
        aqms.assignRole("UserNameB", aqms.getRole("ADMIN"));
        assertEquals("admin", aqms.getRole("UserNameB"));
    }

    //
    @Test
    public void testAssignRoleUserHasDowngrade() throws Throwable {

        aqms.assignRole("UserNameA", aqms.getRole("ADMIN"));
        assertEquals("admin", aqms.getRole("UserNameA"));


        aqms.assignRole("UserNameA", aqms.getRole("USER"));
        assertEquals("user", aqms.getRole("UserNameA"));
    }

    // EC5
    @Test
    public void testAssignRoleUserWithoutDowngrade() throws Throwable {

        aqms.assignRole("UserNameB", aqms.getRole("USER"));
        assertEquals("user", aqms.getRole("UserNameB"));
    }
    // ----------- Tests for add_data Method --------------

    // EC1: Username is not registered
    @Test
    public void testRegisterUsernameNotRegistered() throws Throwable {
        try {
            aqms.register("NewUser", "ValidPass1!", "1234");
            assertTrue(aqms.isUser("NewUser"));
        } catch (DuplicateUserException e) {
            fail("Expected the registration of a new user to succeed, but a duplicate was detected.");
        }
    }

    // EC2
    @Test
    public void testUsernameNotAuthenticated() throws Throwable, UnauthenticatedUserException {
        // Register another user
        aqms.register("UserNameB", "ValidPass1!", "1234");

        // Ensure the user is not authenticated
        aqms.addData("UserNameB", data);
        fail("Expected UnauthenticatedUserException for an unauthenticated user.");
    }

    // EC3: Role is a User
    @Test
    public void testRoleIsUser() throws Throwable {
        // Register and login as user
        aqms.register("RegularUser", "ValidPass1!", "1234");
        aqms.login("RegularUser", "ValidPass1!", "1234");

        // Assign user role and verify
        aqms.assignRole("RegularUser", aqms.getRole("USER"));
        assertEquals("user", aqms.getRole("RegularUser"));

        // Test behavior for regular user
        try {
            aqms.addData("UserNameB", data);
            // Add additional assertions if necessary
        } catch (Exception e) {
            fail("Unexpected exception for regular user with valid role.");
        }
    }

    // EC4: Index < 0
    @Test
    public void testNegativeIndex() throws Throwable {
        // Assuming addData uses an index
        try {
            aqms.addData("UserNameB", data);
            fail("Expected IndexOutOfBoundsException for negative index.");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    // EC5
    @Test
    public void testIndexWithinBoundsLengthZero() throws Throwable {
        // ensure length is > 0
        List<Integer> data = Arrays.asList(); // Example data
        try {
            aqms.addData("UserNameB", data);
            // Assuming length(data) = 0, this should pass without errors
        } catch (Exception e) {
            fail("Unexpected exception when index is within bounds with length(data) = 0.");
        }
    }

    // EC6
    @Test
    public void testIndexOutOfBoundsLengthZero() throws Throwable {
        // Add data to ensure length is > 0
        try {
            aqms.addData("UserNameB", data); // Index out of bounds
            fail("Expected IndexOutOfBoundsException for index > length(data)-1.");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }
    // EC7
    @Test
    public void testIndexWithinBoundsLengthOne() throws Throwable {
        // Add data to ensure length is > 0
        List<Integer> data = Arrays.asList(1); // Example data
        aqms.addData("UserNameA", data); // Length = 1, index should be valid
        assertNotNull(aqms.getData("UserNameA", 0));
    }

    // EC8
    @Test
    public void testIndexWithinBoundsLengthGreaterThanOne() throws Throwable {

        List<Integer> data1 = Arrays.asList(); // Example data
        aqms.addData("UserNameA", data1);
        // Add data to ensure length is > 0
        List<Integer> data2 = Arrays.asList(1,2,3); // Example data
        aqms.addData("UserNameA", data2);

        assertNotNull(aqms.getData("UserNameA", 0));
        assertNotNull(aqms.getData("UserNameA", 1));
    }

}
