import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple E-Commerce System (like Amazon)

public class ECommerceUserInterface
{
	public static void main(String[] args)
	{
		// Create the system
		ECommerceSystem amazon = new ECommerceSystem();

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		// Process keyboard actions
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();

			if (action == null || action.equals(""))
			{
				System.out.print("\n>");
				continue;
			}
			else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
				return;

			else if (action.equalsIgnoreCase("PRODS"))	// List all products for sale
			{
				amazon.printAllProducts();
			}
			else if (action.equalsIgnoreCase("BOOKS"))	// List all books for sale
			{
				amazon.printAllBooks();
			}
			else if (action.equalsIgnoreCase("BOOKSBYAUTHOR"))	// ship an order to a customer
			{
				String author = "";

				System.out.print("Author: ");
				if (scanner.hasNextLine())
					author = scanner.nextLine();

				ArrayList<Book> books = amazon.booksByAuthor(author);
				Collections.sort(books);
				for (Book book: books)
					book.print();
			}
			else if (action.equalsIgnoreCase("CUSTS")) 	// List all registered customers
			{
				amazon.printCustomers();
			}
			else if (action.equalsIgnoreCase("ORDERS")) // List all current product orders
			{
				amazon.printAllOrders();
			}
			else if (action.equalsIgnoreCase("SHIPPED"))	// List all orders that have been shipped
			{
				amazon.printAllShippedOrders();
			}
			else if (action.equalsIgnoreCase("NEWCUST"))	// Create a new registered customer
			{
				String name = "";
				String address = "";

				while (true) {
					System.out.print("Name: ");
					if (scanner.hasNextLine())
						name = scanner.nextLine();

					System.out.print("\nAddress: ");
					if (scanner.hasNextLine())
						address = scanner.nextLine();

					try {
						amazon.createCustomer(name, address);
						break;
					} catch (InvalidCustomerNameException e) {
						System.out.println("Please enter a valid customer name: " + e.getMessage());
					} catch (InvalidCustomerAddressException e) {
						System.out.println("Please enter a valid customer address: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("SHIP"))	// ship an order to a customer
			{
				String orderNumber = "";

				while (true) {
					System.out.print("Order Number: ");
					if (scanner.hasNextLine())
						orderNumber = scanner.nextLine();
					try {
						ProductOrder order = amazon.shipOrder(orderNumber);
						order.print();
						break;
					} catch (InvalidOrderNumberException e) {
						System.out.println("Please enter a valid order number: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("CUSTORDERS")) // List all the current orders and shipped orders for this customer
			{
				String customerId = "";

				while (true) {
					System.out.print("Customer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();


					// Prints all current orders and all shipped orders for this customer
					try {
						amazon.printOrderHistory(customerId);
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("ORDER")) // order a product for a certain customer
			{
				String productId = "";
				String customerId = "";

				while (true) {

					System.out.print("Product Id: ");
					if (scanner.hasNextLine())
						productId = scanner.nextLine();

					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();

					// Prints all current orders and all shipped orders for this customer
					try {
						String orderNumber = amazon.orderProduct(productId, customerId, "");
						System.out.println("Order #" + orderNumber);
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					} catch (UnknownProductException e) {
						System.out.println("Please enter a valid productId: " + e.getMessage());
					} catch (InvalidProductOptionsException e) {
						System.out.println("Please enter a valid ProductOptions: " + e.getMessage());
					} catch (ProductOutOfStockException e) {
						System.out.println(e.getMessage());
					}
				}

			}
			else if (action.equalsIgnoreCase("ORDERBOOK")) // order a book for a customer, provide a format (Paperback, Hardcover or EBook)
			{
				String productId = "";
				String customerId = "";
				String format = "";

				while (true) {
					System.out.print("Product Id: ");
					if (scanner.hasNextLine())
						productId = scanner.nextLine();

					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();

					System.out.print("\nFormat [Paperback Hardcover EBook]: ");
					if (scanner.hasNextLine())
						format = scanner.nextLine();

					try {
						String orderNumber = amazon.orderProduct(productId, customerId, format);
						System.out.println("Order #" + orderNumber);
						break;
					} catch (UnknownCustomerException e) {
					System.out.println("Please enter a valid customerId: " + e.getMessage());
					} catch (UnknownProductException e) {
						System.out.println("Please enter a valid productId: " + e.getMessage());
					} catch (InvalidProductOptionsException e) {
						System.out.println("Please enter a valid ProductOptions: " + e.getMessage());
					} catch (ProductOutOfStockException e) {
						System.out.println(e.getMessage());
					}

					}
				}
			else if (action.equalsIgnoreCase("ORDERSHOES")) // order a book for a customer, provide a format (Paperback, Hardcover or EBook)
			{
				String productId = "";
				String customerId = "";
				String sizeColor = "";

				while (true) {
					System.out.print("Product Id: ");
					if (scanner.hasNextLine())
						productId = scanner.nextLine();

					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();

					System.out.print("\nSize (6, 7, 8, 9, 10) and Color (Black or Brown): ");
					if (scanner.hasNextLine())
						sizeColor = scanner.nextLine();

					try {
						String orderNumber = amazon.orderProduct(productId, customerId, sizeColor);
						System.out.println("Order #" + orderNumber);
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					} catch (UnknownProductException e) {
						System.out.println("Please enter a valid productId: " + e.getMessage());
					} catch (InvalidProductOptionsException e) {
						System.out.println("Please enter a valid ProductOptions: " + e.getMessage());
					} catch (ProductOutOfStockException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("CANCEL")) // Cancel an existing order
			{
				String orderNumber = "";

				while (true) {
					System.out.print("Order Number: ");
					if (scanner.hasNextLine())
						orderNumber = scanner.nextLine();
					try {
						amazon.cancelOrder(orderNumber);
						break;
					} catch (InvalidOrderNumberException e) {
						System.out.println("Please enter a valid order number: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("ADDTOCART"))
			{
				String productId = "";
				String customerId = "";
				String productOptions = "";

				while (true) {
					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();

					while (true) {
						System.out.print("Product Id: ");
						if (scanner.hasNextLine())
							productId = scanner.nextLine();
						try {
							System.out.println(amazon.stringValidOptions(productId));
							break;
						} catch (UnknownProductException e) {
							System.out.println("Please enter a valid productId: " + e.getMessage());
						}
					}

					System.out.print("ProductOptions: ");
					if (scanner.hasNextLine())
						productOptions = scanner.nextLine();

					try {
						amazon.addCartItem(productId, customerId, productOptions);
						System.out.println("Product has been added to cart");
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					} catch (UnknownProductException e) {
						System.out.println("Please enter a valid productId: " + e.getMessage());
					} catch (InvalidProductOptionsException e) {
						System.out.println("Please enter a valid ProductOptions: " + e.getMessage());
					} catch (ProductOutOfStockException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("REMCARTITEM"))
			{
				String productId = "";
				String customerId = "";

				while (true) {
					System.out.print("Product Id: ");
					if (scanner.hasNextLine())
						productId = scanner.nextLine();

					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();
					try {
						amazon.removeCartItem(productId, customerId);
						System.out.println("Product has been removed from the cart");
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					} catch (UnknownProductException e) {
						System.out.println("Please enter a valid productId: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("PRINTCART"))
			{
				String customerId = "";

				while (true) {
					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();
					try {
						amazon.printCartItems(customerId);
						break;
					} catch (UnknownCustomerException e) {
						System.out.println("Please enter a valid customerId: " + e.getMessage());
					}
				}
			}
			else if (action.equalsIgnoreCase("ORDERITEMS"))
			{
				String customerId = "";

				while (true) {
					System.out.print("\nCustomer Id: ");
					if (scanner.hasNextLine())
						customerId = scanner.nextLine();
					try {
						ArrayList<CartItem> items = amazon.getCartItems(customerId);

						for (CartItem item: items) {
							amazon.orderProduct(item.getProduct().getId(), customerId, item.getProductOptions());
							System.out.println("Products have been ordered");

						}
						break;
						} catch (UnknownCustomerException e) {
							System.out.println("Please enter a valid customerId: " + e.getMessage());
						}
					}
				}
			else if (action.equalsIgnoreCase("PRINTBYPRICE")) // sort products by price
			{
				amazon.printByPrice();
			}
			else if (action.equalsIgnoreCase("PRINTBYNAME")) // sort products by name (alphabetic)
			{
				amazon.printByName();
			}
			else if (action.equalsIgnoreCase("PRINTBYCUSTS")) // sort products by name (alphabetic)
			{
				amazon.printCustomersByName();
			}
			else if (action.equalsIgnoreCase("STATS")) {
				amazon.printByStats();
			}
			System.out.print("\n>");
		}
	}
}
