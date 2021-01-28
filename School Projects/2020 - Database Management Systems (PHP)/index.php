<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Check what menu we should show
    if(!isset($_GET["menu"])) {
        $menu_id = "login";
    } else {
        $menu_id = $_GET["menu"];
    }

    // Check if there are any error cookies
    if(!isset($_COOKIE["error"])) {
        $error_id = "none";
    } else {
        $error_id = $_COOKIE["error"];
        // Reset the error
        setcookie("error", "", time() - 3600);
    }

    // Check if the user is logged in
    if(isset($_COOKIE["user"])) {
        $user = $_COOKIE["user"];
        $email = $_COOKIE["email"];
        $address = $_COOKIE["address"];
        
        // Make sure a logged in user can't log in or register again
        if (strcmp($menu_id,"login") == 0 or strcmp($menu_id,"register") == 0)
            $menu_id = "categories";
        
    } else {
        $user = "";
        $email = "";
        $address = "";
    }
?>
<!DOCTYPE html >
<html>
<head>
<title>Tools Shop</title>
<link rel="stylesheet" type="text/css" href="style.css?rand=<?php echo rand(555,99999); ?>">
</head>
<body>
		<div class="header">
		<h1>Tools Shop</h1>
		</div>
		<div class="container" id="login-container" <?php if (strcmp($menu_id,"login") != 0) echo "hidden";?> >
			<h3>Login</h3>
			<form id="login-form" method="post" align="left">
                <?php if (strcmp($error_id,"login") == 0) echo "<p style=\"color:red;\">Error: Invalid username or password!<p>";?>
                <table border="0.5" >
                    <tr>
                        <td><label for="username">User Name:</label></td>
                        <td><input type="text" name="username" id="username" /></td>
                    </tr>
                    <tr>
                        <td><label for="password">Password:</label></td>
                        <td><input type="password" name="password" id="password" /></td>
                    </tr>
                    <tr>
                        <td><input type="submit" formaction="login.php" value="login" /></td>
                        <td><input type="submit" formaction="register.php" value="register" /></td>
                    </tr>
                </table>
            </form>
		</div>
        <div class="container" id="register-container" <?php if (strcmp($menu_id,"register") != 0) echo "hidden";?> >
			<h3>Register</h3>
			<form id="register-form" method="post" align="left">
                <?php if (strcmp($error_id,"register") == 0) echo "<p style=\"color:red;\">Error: That username is already taken!<p>";?>
                <table border="0.5" >
                    <tr>
                        <td><label for="username">User Name:</label></td>
                        <td><input type="text" name="username" id="username" /></td>
                    </tr>
                    <tr>
                        <td><label for="password">Password:</label></td>
                        <td><input type="password" name="password" id="password" /></td>
                    </tr>
                    <tr>
                        <td><label for="email">Email:</label></td>
                        <td><input type="email" name="email" id="email" /></td>
                    </tr>
                    <tr>
                        <td><label for="payment">Payment Card:</label></td>
                        <td><input type="payment" name="payment" id="payment" /></td>
                    </tr>
                    <tr>
                        <td><label for="address">Billing Address:</label></td>
                        <td><input type="address" name="address" id="address" /></td>
                    </tr>
                    <tr>
                        <td><input type="submit" formaction="register.php" value="register" /></td>
                        <td><input type="submit" formaction="logout.php" value="cancel" /></td>
                    </tr>
                </table>
            </form>
		</div>
        <div class="container" id="account-container" <?php if (strcmp($user,"") == 0) echo "hidden"; ?> >
            <h3>Account:</h3>
            <p><b>User:</b> <?php echo $user; ?>
            <p><b>Email:</b> <?php echo $email; ?>
            <p><b>Billing Address:</b> <?php echo $address; ?>
            <p><a href="index.php?menu=cart">- Go To Cart -</a>
            <p><a href="index.php?menu=orders">- Previous Orders -</a>
            <p><a href="logout.php">- Log Out -</a>
        </div> 
        <div class="container" id="categories-container" <?php if (strcmp($menu_id,"categories") != 0) echo "hidden";?> >
			<h3>Categories:</h3>
<?php
    // Make sure we're looking at the categories menu
    if (strcmp($menu_id,"categories") == 0) {
        
        // Get a list of all categories
        $sql = "SELECT * FROM category";
        $result = $sqlcon->query($sql);
        
        // Check if any categories were found
        if ($result != false and $result->num_rows > 0) {
            
            // Show each category
            while($row = $result->fetch_assoc()) {
                echo "<hr><div class=\"item-list\"><br><a href=\"items.php?category=" . $row["CategoryID"] . "\"><b>" . $row["CategoryName"] . ":</b> " . $row["CategoryDescription"] . "</a>";
                
                // Allow the Admin to delete categories
                if (strcmp($user,"Admin") == 0) 
                    echo " | <a href=\"deletecategory.php?id=" . $row["CategoryID"] . "\">(Admin: Delete Category)</a>";
                
                echo "<br>&nbsp;</div>";
            }
            
        } else {
            
            echo "<hr><div class=\"item-list\"><br>No categories found!<br>&nbsp;</div>";
        }
    }
?>
            <hr>
            <div <?php if (strcmp($user,"Admin") != 0) echo "hidden";?> >
                <h3>(Admin) Add Category:</h3>
                <form id="category-form" method="post" align="left">
                    <table border="0.5" >
                        <tr>
                            <td><label for="name">Category Name:</label></td>
                            <td><input type="text" name="name" id="name" /></td>
                        </tr>
                        <tr>
                            <td><label for="description">Description:</label></td>
                            <td><input type="text" name="description" id="description" /></td>
                        </tr>
                        <tr>
                            <td><input type="submit" formaction="addcategory.php" value="add" /></td>
                        </tr>
                    </table>
                </form>
            </div>
		</div>
        <div class="container" id="items-container" <?php if (strcmp($menu_id,"items") != 0) echo "hidden";?> >
			<h3>
<?php
    // Make sure we're looking at the items menu and a category is set
    if (strcmp($menu_id,"items") == 0 and isset($_COOKIE["categoryid"])) {
        
        $category = $_COOKIE["categoryid"];
        
        // Get the name of the current category
        $sql = $sqlcon->prepare("SELECT CategoryName FROM category WHERE CategoryID = ?");
        $sql->bind_param('i', $category);
        $sql->execute();
        $result = $sql->get_result();
        echo $result->fetch_assoc()["CategoryName"];
        
?></h3>
            <a href="index.php?menu=categories"><-- Back to categories list</a>
<?php
        
        // Get a list of all items in the current category
        $sql = $sqlcon->prepare("SELECT * FROM item WHERE ItemID IN (SELECT ItemID FROM group_table WHERE CategoryID = ?)");
        $sql->bind_param('i', $category);
        $sql->execute();
        $result = $sql->get_result();
        
        // Check if any items were found
        if ($result != false and $result->num_rows > 0) {
            
            // Show each category
            while($row = $result->fetch_assoc()) {
                echo "<hr><div class=\"item-list\"><br><b>" . $row["ItemName"] . ":</b></div>";
                echo "<div class=\"item-list\">" . $row["ItemDescription"] . " | Price: " . $row["ItemPrice"] . " | In Stock: " . $row["StockQuantity"];
                echo "</div><div class=\"item-list\"><a href=\"reviews.php?item=" . $row["ItemID"] . "\">Read Reviews</a> | <a href=\"addcart.php?item=" . $row["ItemID"] . "\">Add to Cart</a>";
                
                // Allow the Admin to delete items
                if (strcmp($user,"Admin") == 0) 
                    echo " | <a href=\"deleteitem.php?id=" . $row["ItemID"] . "\">(Admin: Delete Item)</a>";
                
                echo "<br>&nbsp;</div>";
            }
            
        } else {
            
            echo "<hr><div class=\"item-list\"><br>No items found!<br>&nbsp;</div>";
        }
    }            
?>			<hr>
            <div <?php if (strcmp($user,"Admin") != 0) echo "hidden";?> >
                <h3>(Admin) Add Item:</h3>
                <form id="item-form" method="post" align="left">
                    <table border="0.5" >
                        <tr>
                            <td><label for="name">Item Name:</label></td>
                            <td><input type="text" name="name" id="name" /></td>
                        </tr>
                        <tr>
                            <td><label for="description">Description:</label></td>
                            <td><input type="text" name="description" id="description" /></td>
                        </tr>
                        <tr>
                            <td><label for="price">Price:</label></td>
                            <td><input type="text" name="price" id="price" /></td>
                        </tr>
                        <tr>
                            <td><label for="quantity">Stock Quantity:</label></td>
                            <td><input type="text" name="quantity" id="quantity" /></td>
                        </tr>
                        <tr>
                        <input hidden type="text" name="category" id="category" value="<?php echo $_COOKIE["categoryid"]; ?>"/>
                            <td><input type="submit" formaction="additem.php" value="add" /></td>
                        </tr>
                    </table>
                </form>
            </div>
		</div>
        <div class="container" id="cart-container" <?php if (strcmp($menu_id,"cart") != 0) echo "hidden";?> >
			<h3>Cart:</h3>
            <a href="index.php?menu=categories"><-- Back to categories list</a>
<?php
    // Make sure we're looking at the cart menu
    if (strcmp($menu_id,"cart") == 0) {
         
        // Get a list of all items in the cart
        $sql = $sqlcon->prepare("SELECT * FROM cart WHERE UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('s', $user);
        $sql->execute();
        $result = $sql->get_result();
        
        // Check if any items were found
        if ($result != false and $result->num_rows > 0) {
            
            // Show each item
            while($row = $result->fetch_assoc()) {
                
                // Get the item information
                $sql = $sqlcon->prepare("SELECT * FROM item WHERE ItemID = ?");
                $sql->bind_param('i', $row["ItemID"]);
                $sql->execute();
                $result2 = $sql->get_result();
                
                // Make sure the item exists
                if ($row2 = $result2->fetch_assoc()) {
                
                    echo "<hr><div class=\"item-list\"><br><b>" . $row2["ItemName"] . ":</b></div>";
                    echo "<div class=\"item-list\">" . $row2["ItemDescription"] . " | Price: " . $row2["ItemPrice"] . " | Quantity: " . $row["ItemQuantity"];
                    echo "</div><div class=\"item-list\"><a href=\"reviews.php?item=" . $row["ItemID"] . "\">Read Reviews</a> | <a href=\"addcart.php?item=" . $row["ItemID"] . "\">Add Another</a> | <a href=\"deletecart.php?num=1&item=" . $row["ItemID"] . "\">Remove One</a> | <a href=\"deletecart.php?num=0&item=" . $row["ItemID"] . "\">Remove All</a>";
                    echo "<br>&nbsp;</div>";
                }
            }
            
        } else {
            
            echo "<hr><div class=\"item-list\"><br>Your cart is empty!<br>&nbsp;</div>";
        } 
    }    
?>			
        <hr><div class="item-list"><br><a href="checkout.php">Checkout</a> | <a href="clearcart.php">Clear Cart</a><br>&nbsp;</div>
		</div>
        <div class="container" id="orders-container" <?php if (strcmp($menu_id,"orders") != 0) echo "hidden";?> >
			<h3>Previous Orders:</h3>
            <a href="index.php?menu=categories"><-- Back to categories list</a>
<?php
    // Make sure we're looking at the orders menu
    if (strcmp($menu_id,"orders") == 0) {
         
        // Get a list of all orders
        $sql = $sqlcon->prepare("SELECT * FROM order_table WHERE UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('s', $user);
        $sql->execute();
        $result = $sql->get_result();
        
        // Check if any orders were found
        if ($result != false and $result->num_rows > 0) {
            
            // Show each order
            while($row = $result->fetch_assoc()) {
                
                echo "<hr><div class=\"item-list\"><br><b>Order number: " . $row["OrderID"] . " | Total Price: " . $row["TotalPrice"] . " | Date: " . $row["OrderDate"] . "</b>";
                
                // Get the items in the order
                $sql2 = $sqlcon->prepare("SELECT * FROM purchase WHERE OrderID = ?");
                $sql2->bind_param('i', $row["OrderID"]);
                $sql2->execute();
                $result2 = $sql2->get_result();
                
                // Show every item in the order
                while ($row2 = $result2->fetch_assoc()) {
                    
                    // Get the name of each item
                    $sql3 = $sqlcon->prepare("SELECT ItemName FROM item WHERE ItemID = ?");
                    $sql3->bind_param('i', $row2["ItemID"]);
                    $sql3->execute();
                    $result3 = $sql3->get_result();
                    
                    if ($row3 = $result3->fetch_assoc()) {
                        echo "<p>&emsp;&emsp;" . $row3["ItemName"] . " | Price: " . $row2["PurchasePrice"] . " | Quantity: " . $row2["ItemQuantity"];
                        echo " | <a href=\"reviews.php?item=" . $row2["ItemID"] . "\">Read Reviews</a> | <a href=\"reviewing.php?item=" . $row2["ItemID"] . "\">Write A Review</a>";
                    }
                }
                
                echo "<br>&nbsp;</div>";
            }
            
        } else {
            
            echo "<hr><div class=\"item-list\"><br>You have no previous orders!<br>&nbsp;</div>";
        } 
    }         
?>			
		</div>
        <div class="container" id="reviews-container" <?php if (strcmp($menu_id,"reviews") != 0) echo "hidden";?> >
			<h3>Reviews:</h3>
            <a href="index.php?menu=items"><-- Back to items</a>
<?php
    // Make sure we're looking at the reviews menu and we have an item selected
    if (strcmp($menu_id,"reviews") == 0 and isset($_COOKIE['item'])) {
        
        $item = $_COOKIE['item'];
        
        // Get the item name
        $sql = $sqlcon->prepare("SELECT ItemName FROM item WHERE ItemID = ?");
        $sql->bind_param('i', $item);
        $sql->execute();
        $result = $sql->get_result();
        
        // Make sure this item exists
        if ($result != false and $result->num_rows > 0 and $row = $result->fetch_assoc())
            $itemName = $row["ItemName"];
        
        // Get the total review score
        $sql1 = $sqlcon->prepare("SELECT SUM(ReviewRating) as ReviewTotal FROM review WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql1->bind_param('is', $item, $user);
        $sql1->execute();
        $result1 = $sql1->get_result();
        
        // Make sure we have a total
        $total = 0;
        if ($result1 != false and $result1->num_rows > 0 and $row1 = $result1->fetch_assoc())
            $total = $row1["ReviewTotal"];
        
        // Show the item name and total rating
        echo "<hr><div class=\"item-list\"><br><b>Item: " . $itemName . "</b><p>Rating: " . $total . "/5<br>&nbsp;</div>";

        // Get a list of all reviews
        $sql2 = $sqlcon->prepare("SELECT account.UserID as UserID, UserName, ReviewRating, ReviewText FROM review,account WHERE ItemID = ? AND review.UserID = account.UserID");
        $sql2->bind_param('i', $item);
        $sql2->execute();
        $result2 = $sql2->get_result();
        
        // Check if any reviews were found
        if ($result2 != false and $result2->num_rows > 0) {
            
            // Show each review
            while ($row2 = $result2->fetch_assoc()) {
                
                echo "<hr><div class=\"item-list\"><br>&emsp;&emsp;<b>Reviewed by " . $row2["UserName"] . ": " . $row2["ReviewRating"] . "/5</b><p>&emsp;" . $row2["ReviewText"];
                
                // Allow Admin to delete reviews
                if (strcmp($user,"Admin") == 0)
                    echo "<br>&nbsp;<br>&emsp;&emsp;<a href=\"deletereview.php?user=" . $row2["UserID"] . "&item=" . $item . "\">(Admin: Delete Review)</a>";
                
                echo "<br>&nbsp;</div>";
            }
        } else {
            echo "<hr><div class=\"item-list\"><br>This item has no reviews yet!<br>&nbsp;</div>";
        }   
    }
?>			
		</div>
        <div class="container" id="reviewing-container" <?php if (strcmp($menu_id,"reviewing") != 0) echo "hidden";?> >
			<h3>New Review:</h3>
            <form method="post" id="reviewform">
                <tr>
                    <td>Rating:<br></td>
                    <td><input type="radio" id="r0" name="rating" value="0">
                    <label for="r0">0</label><br></td>
                    <td><input type="radio" id="r1" name="rating" value="1">
                    <label for="r1">1</label><br></td>
                    <td><input type="radio" id="r2" name="rating" value="2">
                    <label for="r2">2</label><br></td>
                    <td><input type="radio" id="r3" name="rating" value="3">
                    <label for="r3">3</label><br></td>
                    <td><input type="radio" id="r4" name="rating" value="4">
                    <label for="r4">4</label><br></td>
                    <td><input type="radio" id="r5" name="rating" value="5">
                    <label for="r5">5</label><br>&nbsp;<br></td>
                    <td><textarea name="review" form="reviewform" rows="10" cols="60">Enter review here...</textarea><br>&nbsp;<br></td>
                </tr>
                <tr>
                    <td><input type="submit" formaction="addreview.php" value="Submit Review" /></td>
                </tr>
            </form>
            <p><a href="index.php?menu=orders"><-- Back to orders</a>			
		</div>
        <br>&emsp;<br>&emsp;<br>&emsp;
	</body>
</html>