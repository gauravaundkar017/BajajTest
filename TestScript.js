const axios = require('axios');

// Base URL of the API
const url = 'https://bfhldevapigw.healthrx.co.in/automation-campus/create/user';

// Test cases
async function runTests() {
  // Positive Test Case: Valid Input
  await testCreateUser('Test', 'User', '9999999991', 'test1.user@example.com', true);

  // Negative Test Cases
  await testCreateUser('Test', 'User', '9999999999', 'test2.user@example.com', true); // Duplicate phone number
  await testCreateUser('Test', 'User', '9999999991', 'test1.user@example.com', true); // Duplicate email ID
  await testCreateUser('', 'User', '9999999992', 'test3.user@example.com', true);     // Missing firstName
  await testCreateUser('Test', '', '9999999993', 'test4.user@example.com', true);     // Missing lastName
  await testCreateUser('Test', 'User', '', 'test5.user@example.com', true);           // Missing phoneNumber
  await testCreateUser('Test', 'User', '9999999994', '', true);                       // Missing emailId
  await testCreateUser('Test', 'User', '123', 'test6.user@example.com', true);        // Invalid phoneNumber
  await testCreateUser('Test', 'User', '9999999995', 'test6.user@.com', true);        // Invalid emailId

  // Edge Case: Missing roll-number header
  await testCreateUser('Test', 'User', '9999999996', 'test7.user@example.com', false); // Missing roll-number
}

// Function to create a user and print the response
async function testCreateUser(firstName, lastName, phoneNumber, emailId, includeRollNumberHeader) {
  const data = {
    firstName: firstName,
    lastName: lastName,
    phoneNumber: phoneNumber,
    emailId: emailId
  };

  const headers = {
    'Content-Type': 'application/json'
  };

  if (includeRollNumberHeader) {
    headers['roll-number'] = '1';
  }

  try {
    const response = await axios.post(url, data, { headers });
    console.log(`Test Passed - Response Code: ${response.status}`);
    console.log(`Response: ${JSON.stringify(response.data, null, 2)}`);
  } catch (error) {
    if (error.response) {
      console.log(`Test Failed - Response Code: ${error.response.status}`);
      console.log(`Error: ${JSON.stringify(error.response.data, null, 2)}`);
    } else {
      console.error(`Error: ${error.message}`);
    }
  }
}

// Run the tests
runTests();
