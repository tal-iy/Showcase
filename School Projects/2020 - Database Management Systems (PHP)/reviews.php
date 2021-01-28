<?php

    // Make sure an item was selected
    if (isset($_GET['item'])) {

        // Save which item is being shown
        setcookie("item", htmlspecialchars($_GET["item"]), time() + 2592000, "/");
        
    }

    // Go back to the index page
    header("Location: ./index.php?menu=reviews");
    
?>