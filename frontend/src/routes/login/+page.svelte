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
<head>
<style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #FECFEF;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100vh;
      margin: 0;
    }

    form {
      background: white;
      padding: 30px 40px;
      border-radius: 10px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      width: 350px;
    }

    h1 {
      text-align: center;
      color: #333;
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-top: 15px;
      font-weight: 500;
    }

    input[type="text"] {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border-radius: 5px;
      border: 2px solid #ccc;
      transition: border-color 0.3s;
    }

    input[type="text"]:focus {
      border:2px solid #FECFEF;
      outline: none;
    }

.radio-group {
  margin-top: 15px;
}

.radio-option {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.radio-option input[type="radio"] {
  accent-color: #f5a4dc;
  margin-right: 8px;
}

.radio-option label {
  margin: 0;
}



    .button-group {
      display: flex;
      justify-content: space-between;
      margin-top: 25px;
    }

    input[type="submit"],
    input[type="reset"] {
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-weight: bold;
    }

    input[type="submit"] {
      background-color: #cfcfcf;
      color: white;
    }

    input[type="reset"] {
      background-color: #cfcfcf;
      color: white;
    }

    input[type="submit"]:hover {
      background-color: #f5a4dc;
    }

    input[type="reset"]:hover {
      background-color: #f5a4dc;
    }
  </style>
</head>
<form name="loginForm" action="ValidationServlet" method="POST" on:submit={validateForm}>
    <h1>Whiteboard</h1>
    
    <label for="username">Username</label>
    <!-- Validation for username, but doesn't work for guest 
         pattern="[A-Za-z ]{3,}" -->
    <input type="text" id="username" name="username" bind:value={username}  
           placeholder="johndoe1"><br><br>
    

    <!-- Validation for password, but doesn't work for guest 
         pattern="[A-Za-z ]{3,}" -->
    <label for="password">Password</label>
    <input type="text" id="password" name="password" bind:value={password}  
           placeholder="password1"><br><br>

    <!-- Radio buttons with checked attribute -->
    <div class="radio-group">
  <label>I want to</label>
  <div class="radio-option">
    <input type="radio" id="register" name="action" value="register" bind:group={action} checked>
    <label for="register">Register as a new user</label>
  </div>
  <div class="radio-option">
    <input type="radio" id="login" name="action" value="login" bind:group={action}>
    <label for="login">Login as an existing user</label>
  </div>
  <div class="radio-option">
    <input type="radio" id="guest" name="action" value="guest" bind:group={action}>
    <label for="guest">Continue as guest</label>
  </div>
</div>


    <!-- Submit and reset buttons -->
    <input type="submit" value="Submit">
    <input type="reset" value="Reset">
</form>