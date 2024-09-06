package swen90006.aqms;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import org.junit.*;
import static org.junit.Assert.*;




/**
 * This class implements some boundary value analysis tests for the AQMS system,
 * extending from the PartitioningTests to the reuse equivalence class tests.
 */
public class BoundaryTests extends PartitioningTests {

    protected AQMS aqms;

    @Before
    public void setUp() throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException, InvalidDeviceIDException {
        aqms = new AQMS();
        aqms.register("UserNameA", "Password1!", "1234");
    }

    @After
    public void tearDown() {
        // Cleanup after tests if necessary
    }

    // ----------- Boundary Value Tests for Register Method --------------

    //EC1
    @Test
    public void testRegisterUsernameUniquenessBoundary() throws Throwable
    {

        aqms.register("UserNameB", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserNameB"));


        try {


            aqms.register("UserNameA", "ValidPass1!", "1234");
            fail("Expected DuplicateUserException for duplicate username");
        } catch (DuplicateUserException e) {
            //dd
        }
    }


    //EC2
    @Test
    public void testRegisterUsernameLengthBoundary() throws Throwable {

        aqms.register("User", "ValidPass1!", "1234");




        assertTrue(aqms.isUser("User"));


        try {
            aqms.register("Usr", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for short username");
           } catch (InvalidUsernameException e) {
            // Expected
        }
    }


    //EC3
    @Test
    public void testRegisterUsernameSpecialCharactersBoundary() throws Throwable {



        /// test
        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));

        // Off: Contains special characters (invalid)
        try {
            aqms.register("User@Name", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for special characters in username");
        } catch (InvalidUsernameException e) {
            // Expected
        }
    }

    //EC4

    @Test
    public void testRegisterPasswordLengthBoundary() throws Throwable {
        // when Length = 8 on
        aqms.register("UserName", "ValidP1!", "1234");
        assertTrue(aqms.isUser("UserName"));




        try {
            aqms.register("UserName", "Pass12!", "1234");
            fail("Expected InvalidPasswordException for short password");
        } catch (InvalidPasswordException e) {
            //???
        }
    }

    //EC5 case
    @Test
    public void testRegisterPasswordContainsLettersBoundary() throws Throwable {
        // On: Password contains letters (valid)
        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));




        try {
            aqms.register("UserName", "1234567!", "1234");
            fail("Expected InvalidPasswordException for password without letters");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }


    //EC6

    @Test
    public void testRegisterPasswordContainsDigitsBoundary() throws Throwable
    {

        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));





        try {
            aqms.register("UserName", "Password!", "1234");
            fail("Expected InvalidPasswordException for password without digits");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }


    //EC7
    @Test
    public void testRegisterPasswordContainsSpecialCharactersBoundary() throws Throwable {

        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));


        try {
            aqms.register("UserName", "ValidPass12", "1234");//ensure the same leg
            fail("Expected InvalidPasswordException for password without special characters");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }

    //EC8

    @Test
    public void testRegisterDeviceIDLengthBoundary() throws Throwable {

        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));




        try {
            aqms.register("UserName", "ValidPass1!", "123");



            fail("Expected InvalidDeviceIDException for short device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }
    }

    //EC 9
    @Test
    public void testRegisterDeviceIDLengthGreaterThan4Boundary() throws Throwable {

        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));


        try {
            aqms.register("UserName", "ValidPass1!", "12345");
            fail("Expected InvalidDeviceIDException for device ID length greater than 4");
        } catch (InvalidDeviceIDException e) {
            // Expecteddd
        }
    }


    //EC 10
    @Test
    public void testRegisterDeviceIDNotOnlyDigitsBoundary() throws Throwable {

        try {
            aqms.register("UserName", "ValidPass1!", "12a#");
            fail("Expected InvalidDeviceIDException for non-numeric device ID");
        } catch (InvalidDeviceIDException e) {
            //
        }


        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));
    }

    //EC 11
    @Test
    public void testRegisterDeviceIDOnlyDigitsBoundary() throws Throwable {

        aqms.register("UserName", "ValidPass1!", "1234");

        assertTrue(aqms.isUser("UserName"));


        try {
            aqms.register("UserName", "ValidPass1!", "12a#");
            fail("Expected InvalidDeviceIDException for non-numeric device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }
    }


    //EC13
    @Test
    public void testRegisterUsernameNotUniqueBoundary() throws Throwable {
        // Register the first user
        aqms.register("UserName", "ValidPass1!", "1234");

        // Off: Username already exists (invalid)
        try {
            aqms.register("UserName", "ValidPass1!", "1234");
            fail("Expected DuplicateUserException for duplicate username");
        } catch (DuplicateUserException e) {
            // Expected
        }
    }


    //EC 14

    @Test
    public void testRegisterUsernameLengthLessThan4Boundary() throws Throwable {
        // On: Length = 4
        aqms.register("User", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("User"));

        // Off: Length < 4
        try {
            aqms.register("Usr", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for short username");
        } catch (InvalidUsernameException e) {
            // Expected
        }
    }


    //EC15

    @Test
    public void testRegisterUsernameContainsNonLettersBoundary() throws Throwable {
        // On: Username contains characters
        aqms.register("UserName1", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName1"));

        // Off: Username  only  letters
        try {
            aqms.register("UserName", "ValidPass1!", "1234");
            fail("Expected InvalidUsernameException for username with only letters");
        } catch (InvalidUsernameException e) {
            // Expected
        }
    }


    //EC16

    @Test
    public void testRegisterPasswordLengthLessThan8Boundary() throws Throwable {

        aqms.register("UserName", "ValidPA1!", "1234");
        assertTrue(aqms.isUser("UserName"));


        try {
            aqms.register("UserName", "Pass12!", "1234");
            fail("Expected InvalidPasswordException for short password");
        } catch (InvalidPasswordException e) {
            // Expected
        }
    }


    //EC17

    @Test
    public void testRegisterPasswordNoLettersBoundary() throws Throwable {

        try {
            aqms.register("UserName", "12345678!", "1234");
            fail("Expected InvalidPasswordException for password without letters");
        } catch (InvalidPasswordException e) {
            // Expected
        }


        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));
    }


    //EC18
    @Test
    public void testRegisterPasswordNoDigitsBoundary() throws Throwable {

        try {
            aqms.register("UserName", "Password!", "1234");
            fail("Expected InvalidPasswordException for password without digits");
        } catch (InvalidPasswordException e) {
            // Expected
        }


        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));
    }

    //EC19
    @Test
    public void testRegisterPasswordNoSpecialCharactersBoundary() throws Throwable {

        try {
            aqms.register("UserName", "Password1", "1234");
            fail("Expected InvalidPasswordException for password without special characters");
        } catch (InvalidPasswordException e) {
            // Expected
        }


        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));
    }

    //EC20
    @Test
    public void testRegisterDeviceIDLength4Boundary() throws Throwable {

        aqms.register("UserName", "ValidPass1!", "1234");
        assertTrue(aqms.isUser("UserName"));


        try {
            aqms.register("UserName", "ValidPass1!", "123");
            fail("Expected InvalidDeviceIDException for short device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }


        try {
            aqms.register("UserName", "ValidPass1!", "12345");
            fail("Expected InvalidDeviceIDException for long device ID");
        } catch (InvalidDeviceIDException e) {
            // Expected
        }
    }


    // ----------- Boundary Value Tests for some the Login Method --------------

    //EC1
    @Test
    public void testLoginUsernameExistsBoundary() throws Throwable {

        aqms.register("UserName", "CorrectPass1!", "1234");
        aqms.login("UserName", "CorrectPass1!", "1234");

        assertTrue(aqms.isAuthenticated("UserName"));


        try {
            aqms.login("NoUser", "CorrectPass1!", "1234");
            fail("Expected NoSuchUserException for non-existent username");
        } catch (NoSuchUserException e) {
            // Expected
        }
    }

    //EC2

    @Test
    public void testLoginPasswordCorrectBoundary() throws Throwable {
        aqms.register("UserName", "CorrectPass1!", "1234");
        aqms.login("UserName", "CorrectPass1!", "1234");

        assertTrue(aqms.isAuthenticated("UserName"));
    }



    //EC3

    @Test
    public void testLoginDeviceIDCorrectBoundary() throws Throwable {
        //reg
        aqms.register("UserName", "CorrectPass1!", "1234");


        aqms.login("UserName", "CorrectPass1!", "1234");

        assertTrue(aqms.isAuthenticated("UserName"));  // 使用 isAuthenticated 方法

        // Off
        try {
            aqms.login("UserName", "CorrectPass1!", "4321");
            fail("Expected IncorrectDeviceIDException for incorrect device ID");
        } catch (IncorrectDeviceIDException e) {
            // Expected
        }
    }


    // ----------- Boundary Value Tests for some the Assign_Role Method --------------

    //EC1
    @Test
    public void testAssignRoleUsernameExistsBoundary() throws Throwable {

        aqms.register("ExistingUser", "ValidPass1!", "1234");
        aqms.assignRole("ExistingUser", aqms.getRole("USER"));
        assertEquals("USER", "ExistingUser");


        try {
            aqms.assignRole("NonUser", aqms.getRole("USER"));
            fail("Expected NoSuchUserException for non-existent username");
        } catch (NoSuchUserException e) {
            // Expected
        }
    }

    //EC2
    @Test
    public void testAssignRoleUpgradeSameDowngradeBoundary() throws Throwable {

        aqms.register("UserWithUserRole", "ValidPass1!", "1234");
        aqms.assignRole("UserWithUserRole", aqms.getRole("USER"));
        assertEquals("USER", aqms.getRole("UserWithUserRole"));




        aqms.assignRole("UserWithUserRole", aqms.getRole("ADMIN"));
        assertEquals("ADMIN", aqms.getRole("UserWithUserRole"));

        // Off: Role downgrade (invalid)
        aqms.assignRole("UserWithUserRole", aqms.getRole("USER"));
        assertEquals("ADMIN", aqms.getRole("UserWithUserRole")); // Should still be ADMIN
    }

    // ----------- Boundary Value Tests for some the Get_Data Methods --------------

    //EC1
    @Test
    public void testGetDataUsernameExistsBoundary() throws Throwable {

        aqms.register("ExistingUser", "ValidPass1!", "1234");
        aqms.assignRole("ExistingUser", aqms.getRole("USER"));
        assertNotNull(aqms.getData("ExistingUser", 0));


        try {
            aqms.getData("NonUser", 0);
            fail("Expected NoSuchUserException for non-existent username");
        } catch (NoSuchUserException e) {
            // Expected
        }
    }

    //EC2
    @Test
    public void testGetDataAuthenticationStatusBoundary() throws Throwable {



        aqms.register("AuthenticatedAdmin", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedAdmin", aqms.getRole("ADMIN"));


        assertNotNull(aqms.getData("AuthenticatedAdmin", 0));


        try {
            aqms.getData("NonAuthenticatedUser", 0);
            fail("Expected UnauthenticatedUserException for unauthenticated user");
        } catch (UnauthenticatedUserException e) {
            // Expected
        }
    }


    //EC3

    @Test
    public void testGetDataRoleCheckBoundary() throws Throwable {

        aqms.register("AuthenticatedAdmin", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedAdmin", aqms.getRole("ADMIN"));
        aqms.register("AuthenticatedUser", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedUser", aqms.getRole("USER"));

        // On: Admin
        assertNotNull(aqms.getData("AuthenticatedAdmin", 0));

        // Off: User
        try {
            aqms.getData("AuthenticatedUser", 0);
            fail("Expected AccessRightException for user role");
        } catch (AccessRightException e) {
            // Expected
        }
    }


    //EC4
    @Test
    public void testGetDataIndexBoundsBoundary() throws Throwable {

        aqms.register("AuthenticatedAdmin", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedAdmin", aqms.getRole("ADMIN"));

       //Supposee data length is 3 for this example
        int dataLength = 3;


        assertNotNull(aqms.getData("AuthenticatedAdmin", 0));


        assertNotNull(aqms.getData("AuthenticatedAdmin", dataLength - 1));


        try {
            aqms.getData("AuthenticatedAdmin", -1);
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        //
        try {
            aqms.getData("AuthenticatedAdmin", dataLength);
            fail("Expected IndexOutOfBoundsException for out-of-range index");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    //EC5
    @Test
    public void testGetDataIndexInRangeBoundary() throws Throwable {

        aqms.register("AuthenticatedAdmin", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedAdmin", aqms.getRole("ADMIN"));

        // Add data to in ensure length is > 0
        List<Integer> data = Arrays.asList(1, 2, 3); // Example data
        // Wrap data at another list to match addData's expected parameters
        List<List<Integer>> wrappedData = new ArrayList<>();
        wrappedData.add(data);
        aqms.addData("AuthenticatedAdmin", data);

        // On: Valid index within in range
        assertEquals(data, aqms.getData("AuthenticatedAdmin", 0)); // index = 0

        // Off: Invalid index out of the bounds
        try {
            aqms.getData("AuthenticatedAdmin", -1);
            fail("Expected IndexOutOfBoundsException for index < 0");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Assuming `data` contains 1 list, the length is 1.
        List<List<Integer>> dataList = new ArrayList<>();
        dataList.add(data);
        int length = dataList.size();

        try {
            aqms.getData("AuthenticatedAdmin", length);
            fail("Expected IndexOutOfBoundsException for index >= len(data)");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }



    //EC6
    @Test
    public void testGetDataIndexGreaterThanLengthBoundary() throws Throwable {

        aqms.register("AuthenticatedAdmin", "ValidPass1!", "1234");
        aqms.assignRole("AuthenticatedAdmin", aqms.getRole("ADMIN"));


        List<Integer> data = Arrays.asList(1, 2, 3); // Example data
        aqms.addData("AuthenticatedAdmin", data);


        assertEquals(Integer.valueOf(1), aqms.getData("AuthenticatedAdmin", 0).get(0)); // index = 0


        try {
            aqms.getData("AuthenticatedAdmin", data.size());
            fail("Expected IndexOutOfBoundsException for index >= len(data)");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }


}