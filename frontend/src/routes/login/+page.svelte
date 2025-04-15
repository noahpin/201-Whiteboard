<script>
  let username = '';
  let password = '';
  let action = 'register'; // Default to register

  function validateForm(event) {
    // For guests, we bypass username/password requirements
    if (action === 'guest') {
      // Allow guest access without username/password
      return true;
    }
    
    if (username == '') {
      alert('Please enter a username');
      event.preventDefault();
      return false;
    }
    
    if (password == '') {
      alert('Please enter a password');
      event.preventDefault();
      return false;
    }
    
    return true;
  }
</script>

<h1>Whiteboard</h1>
<form name="loginForm" action="ValidationServlet" method="GET" on:submit={validateForm}>
    <legend>Login</legend>
    
    <label for="username">Username:</label>
    <!-- Validation for username, but doesn't work for guest 
         pattern="[A-Za-z ]{3,}" -->
    <input type="text" id="username" name="username" bind:value={username} required 
           placeholder="johndoe1"><br><br>
    
    
    <!-- Validation for password, but doesn't work for guest 
         pattern="[A-Za-z ]{3,}" -->
    <label for="password">Password:</label>
    <input type="text" id="password" name="password" bind:value={password} required 
           placeholder="password1"><br><br>

    <!-- Radio buttons with checked attribute -->
    <label>Action:</label>
    <input type="radio" id="register" name="action" value="register" bind:group={action} checked>
    <label for="register">Register New User</label>
    <input type="radio" id="login" name="action" value="login" bind:group={action}>
    <label for="login">Login Existing User</label>
    <input type="radio" id="guest" name="action" value="guest" bind:group={action}>
    <label for="guest">Guest User</label><br><br>

    <!-- Submit and reset buttons -->
    <input type="submit" value="Submit">
    <input type="reset" value="Reset">
</form>