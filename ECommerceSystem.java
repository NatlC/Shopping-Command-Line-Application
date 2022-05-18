import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/*
 * Models a simple ECommerce system. Keeps track of products for sale, registered customers, product orders and
 * orders that have been shipped to a customer
 */
public class ECommerceSystem {
	Map<String, Product> products = new TreeMap<String, Product>();
	ArrayList<Customer> customers = new ArrayList<Customer>();

	ArrayList<ProductOrder> orders = new ArrayList<ProductOrder>();
	ArrayList<ProductOrder> shippedOrders = new ArrayList<ProductOrder>();

	// These variables are used to generate order numbers, customer id's, product id's 
	int orderNumber = 500;
	int customerId = 900;
	int productId = 700;

	// General variable used to store an error message when something is invalid (e.g. customer id does not exist)  
	String errMsg = null;

	// Random number generator
	Random random = new Random();

	public ECommerceSystem() {
		// NOTE: do not modify or add to these objects!! - the TAs will use for testing
		// If you do the class Shoes bonus, you may add shoe products

		// Create some products
		try {
			for (Product p : readProductFile()) {
				products.put(p.getId(), p);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// Create some customers
		customers.add(new Customer(generateCustomerId(), "Inigo Montoya", "1 SwordMaker Lane, Florin"));
		customers.add(new Customer(generateCustomerId(), "Prince Humperdinck", "The Castle, Florin"));
		customers.add(new Customer(generateCustomerId(), "Andy Dufresne", "Shawshank Prison, Maine"));
		customers.add(new Customer(generateCustomerId(), "Ferris Bueller", "4160 Country Club Drive, Long Beach"));
	}

	private ArrayList<Product> readProductFile() throws FileNotFoundException {
		ArrayList<Product> products = new ArrayList<Product>();
		Scanner in = new Scanner(new File("products.txt"));
		while (in.hasNextLine()) {
			// Everything in a Product's parameters is stored in this list
			ArrayList<String> values = new ArrayList<String>();
			String line = in.nextLine().trim();
			values.add(line);

			// Iterate from the second line after the Category
			for (int i = 0; i < 4; i++) {
				String otherLine = in.nextLine().trim();
				values.add(otherLine);
			}

			// Checks if the Product is book and takes out the semicolon in the last line of the parameter
			// Adds author, title, and year separated from the semicolons into values for construction of Book
			// I didn't know how to use delimiter
			if (values.get(4).contains(":")) {
				int i = 0;
				String s = "";
				while (i < values.get(4).length()) {
					if (Character.toString(values.get(4).charAt(i)).equals(":")) {
						values.add(s);
						s = "";
						i++;
						continue;
					}
					s += values.get(4).charAt(i);
					i++;
				}
				values.add(s);

				Book book = new Book(values.get(1), generateProductId(), Double.parseDouble(values.get(2)),
						4, 2, values.get(5), values.get(6),
						Integer.parseInt(values.get(7)));

				products.add(book);

			} else {
				Product.Category category = Product.Category.COMPUTERS;

				if (values.get(0).equals("GENERAL")) {
					category = Product.Category.GENERAL;
				} else if (values.get(0).equals("CLOTHING")) {
					category = Product.Category.CLOTHING;
				} else if (values.get(0).equals("FURNITURE")) {
					category = Product.Category.FURNITURE;
				}

				Product product = new Product(values.get(1), generateProductId(),
						Double.parseDouble(values.get(2)), Integer.parseInt(values.get(3)), category);

				products.add(product);
			}
		}
		in.close();

		return products;
	}


	private String generateOrderNumber() {
		return "" + orderNumber++;
	}

	private String generateCustomerId() {
		return "" + customerId++;
	}

	private String generateProductId() {
		return "" + productId++;
	}

	public String getErrorMessage() {
		return errMsg;
	}

	public void printAllProducts() {
		Set<String> KeySet = products.keySet();
		for (String key : KeySet) {
			Product p = products.get(key);
			p.print();
		}
	}

	public void printAllBooks() {
		Set<String> KeySet = products.keySet();
		for (String key : KeySet) {
			Product p = products.get(key);
			if (p.getCategory() == Product.Category.BOOKS)
				p.print();
		}
	}

	public ArrayList<Book> booksByAuthor(String author) {
		ArrayList<Book> books = new ArrayList<Book>();

		Set<String> KeySet = products.keySet();
		for (String key : KeySet) {
			Product p = products.get(key);
			if (p.getCategory() == Product.Category.BOOKS) {
				Book book = (Book) p;
				if (book.getAuthor().equals(author))
					books.add(book);
			}
		}
		return books;
	}

	public void printAllOrders() {
		for (ProductOrder o : orders)
			o.print();
	}

	public void printAllShippedOrders() {
		for (ProductOrder o : shippedOrders)
			o.print();
	}

	public void printCustomers() {
		for (Customer c : customers)
			c.print();
	}

	/*
	 * Given a customer id, print all the current orders and shipped orders for them (if any)
	 */
	public void printOrderHistory(String customerId) {
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");

		}
		System.out.println("Current Orders of Customer " + customerId);
		for (ProductOrder order : orders) {
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
		System.out.println("\nShipped Orders of Customer " + customerId);
		for (ProductOrder order : shippedOrders) {
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
	}

	public String orderProduct(String productId, String customerId, String productOptions) {
		// Get customer
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		Customer customer = customers.get(index);

		// Get product

		Product product = products.get(productId);

		if (product == null) {
			throw new UnknownProductException("Product " + productId + " Not Found");
		}

		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions)) {
			throw new InvalidProductOptionsException("Product " + product.getName() + " ProductId " + productId + " Invalid Options: " + productOptions);
		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0) {
			throw new ProductOutOfStockException("Product " + product.getName() + " ProductId " + productId + " Out of Stock");
		}

		// Create a ProductOrder
		ProductOrder order = new ProductOrder(generateOrderNumber(), product, customer, productOptions);
		product.reduceStockCount(productOptions);

		// Add to orders and return
		orders.add(order);

		return order.getOrderNumber();
	}

	/*
	 * Create a new Customer object and add it to the list of customers
	 */

	public void createCustomer(String name, String address) {
		// Check to ensure name is valid
		if (name == null || name.equals("")) {
			throw new InvalidCustomerNameException("Invalid Customer Name " + name);
		}
		// Check to ensure address is valid
		if (address == null || address.equals("")) {
			throw new InvalidCustomerAddressException("Invalid Customer Address " + address);
		}

		Customer customer = new Customer(generateCustomerId(), name, address);
		customers.add(customer);

	}

	public ProductOrder shipOrder(String orderNumber) {
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber, null, null, ""));
		if (index == -1) {
			throw new InvalidOrderNumberException("Order " + orderNumber + " Not Found");
		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
		shippedOrders.add(order);
		return order;
	}

	/*
	 * Cancel a specific order based on order number
	 */
	public void cancelOrder(String orderNumber) {
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber, null, null, ""));
		if (index == -1) {
			throw new InvalidOrderNumberException("Order " + orderNumber + " Not Found");
		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
	}

	public void addCartItem(String productId, String customerId, String productOptions) {
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		Customer customer = customers.get(index);

		// Get product
		Product product = products.get(productId);

		if (product == null) {
			throw new UnknownProductException("Product " + productId + " Not Found");
		}

		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions)) {
			throw new InvalidProductOptionsException("Product " + product.getName() + " ProductId " + productId + " Invalid Options: " + productOptions);
		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0) {
			throw new ProductOutOfStockException("Product " + product.getName() + " ProductId " + productId + " Out of Stock");
		}

		// Create a ProductOrder

		CartItem item = new CartItem(product, productOptions);

		customer.getCart().addItem(item);
	}

	public String stringValidOptions(String productId) {
		Product product = products.get(productId);

		if (product == null) {
			throw new UnknownProductException("Product " + productId + " Not Found");
		}

		if (product.getCategory().equals(Product.Category.BOOKS)) {
			return " e.g. Book Options: Paperback HardCover EBook";
		} else {
			return "type \"null\" or nothing for product options";
		}
	}

	public void removeCartItem(String productId, String customerId) {
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		Customer customer = customers.get(index);

		// Get product
		Product product = products.get(productId);

		if (product == null) {
			throw new UnknownProductException("Product " + productId + " Not Found");
		}

		for (int i = 0; i < customer.getCart().getItems().size(); i++) {
			if (customer.getCart().getItems().get(i).equals(product)) {
				customer.getCart().removeItem(i);
				break;
			}
		}
	}

	public void printCartItems(String customerId) {
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}

		Customer customer = customers.get(index);
		for (CartItem item : customer.getCart().getItems()) {
			item.print();
		}
	}

	public ArrayList<CartItem> getCartItems(String customerId) {
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1) {
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}

		Customer customer = customers.get(index);

		return customer.getCart().getItems();
	}

	// Sort products by increasing price
	public void printByPrice() {

		ArrayList<Product> sortProducts = new ArrayList<Product>();

		Set<String> KeySet = products.keySet();
		for (String key : KeySet) {
			Product p = products.get(key);
			sortProducts.add(p);
		}
		Collections.sort(sortProducts, new PriceComparator());

		for (Product p : sortProducts) {
			p.print();
		}
	}

	private class PriceComparator implements Comparator<Product> {
		public int compare(Product a, Product b) {
			if (a.getPrice() > b.getPrice()) return 1;
			if (a.getPrice() < b.getPrice()) return -1;
			return 0;
		}
	}

	// Sort products alphabetically by product name
	public void printByName() {
		ArrayList<Product> sortProducts = new ArrayList<Product>();

		Set<String> KeySet = products.keySet();
		for (String key : KeySet) {
			Product p = products.get(key);
			sortProducts.add(p);
		}
		Collections.sort(sortProducts, new NameComparator());

		for (Product p : sortProducts) {
			p.print();
		}
	}

	private class NameComparator implements Comparator<Product> {
		public int compare(Product a, Product b) {
			return a.getName().compareTo(b.getName());
		}
	}

	// Sort products alphabetically by product name
	public void printCustomersByName() {
		Collections.sort(customers);
	}

	public void printByStats() {
		ArrayList<Integer> ordered = new ArrayList<Integer>();

		Map<String, Integer> productOrder = new TreeMap<String, Integer>();

		// 0 is amount of times ordered.
		for (ProductOrder p : orders) {
			productOrder.put(p.getProduct().getId(), 0);
		}

		// Go through products and check how many times the product is in the productOrder.
		// Then the key and value is updated.
		Set<String> pKeySet1 = products.keySet();
		Set<String> poKeySet2 = productOrder.keySet();
		for (String pKey1 : pKeySet1) {
			for (String poKey2 : poKeySet2) {
				if (productOrder.get(pKey1) != null) {
					int amount_ordered = productOrder.get(poKey2);
					amount_ordered++;
					productOrder.put(poKey2, amount_ordered);
				}
			}
		}

		// Sorts the amount ordered in productOrder
		Set<String> poKeySet = productOrder.keySet();
		for (String key : poKeySet) {
			ordered.add(productOrder.get(key));
		}

		// Add the products that are not in product order / ordered list
		while (ordered.size() != products.size()) {
			ordered.add(0);
		}

		Collections.sort(ordered, Collections.reverseOrder());

		// Print out the "STATS".
			Set<String> KeySet = productOrder.keySet();
			for (int i = 0; i < ordered.size(); i++) {
				for (String poKey : KeySet) {
					int amount = productOrder.get(poKey);

					Product product = products.get(poKey);
					System.out.println("Product id: " + poKey + " Product name: " + product.getName() +
							" Amount of times ordered: " + amount);

			}
		}
	}
}
	class UnknownCustomerException extends RuntimeException {

		public UnknownCustomerException(String message) {
			super(message);
		}
	}

	class UnknownProductException extends RuntimeException {
		public UnknownProductException(String message) {
			super(message);
		}
	}

	class InvalidProductOptionsException extends RuntimeException {
		public InvalidProductOptionsException(String message) {
			super(message);
		}
	}

	class ProductOutOfStockException extends RuntimeException {
		public ProductOutOfStockException(String message) {
			super(message);
		}
	}

	class InvalidCustomerNameException extends RuntimeException {
		public InvalidCustomerNameException(String message) {
			super(message);
		}
	}

	class InvalidCustomerAddressException extends RuntimeException {
		public InvalidCustomerAddressException(String message) {
			super(message);
		}
	}

	class InvalidOrderNumberException extends RuntimeException {
		public InvalidOrderNumberException(String message) {
			super(message);
		}
	}

