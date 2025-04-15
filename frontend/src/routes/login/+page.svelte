<!DOCTYPE html>
<html>
<body>
    <h1>Whiteboard</h1>
    <form name="loginForm" action="ValidationServlet" method="GET">
            <legend>Login</legend>
            
            <label for="username">Username:</label>
            <!-- Validation for username, but doesn't work for guest 
                 pattern="[A-Za-z ]{3,}" -->
            <input type="text" id="username" name="username" required 
                   placeholder="johndoe1"><br><br>
            
            
            <!-- Validation for password, but doesn't work for guest 
                 pattern="[A-Za-z ]{3,}" -->
            <label for="password">Password:</label>
            <input type="text" id="password" name="password" required 
                   placeholder="password1"><br><br>

            <!-- Radio buttons with checked attribute -->
            <label>Action:</label>
            <input type="radio" id="register" name="action" value="register" checked>
            <label for="register">Register New User</label>
            <input type="radio" id="login" name="action" value="login">
            <label for="login">Login Existing User</label>
            <input type="radio" id="guest" name="action" value="guest">
            <label for="guest">Guest User</label><br><br>
        
        <!-- Submit and reset buttons -->
        <input type="submit" value="Submit">
        <input type="reset" value="Reset">
    </form>
</body>
</html>