<?php

    // Clear all user cookies
    setcookie("user", "", time() - 3600);
    setcookie("email", "", time() - 3600);
    setcookie("address", "", time() - 3600);


    // Go back to the login page
    header("Location: ./index.php?menu=login");

?>