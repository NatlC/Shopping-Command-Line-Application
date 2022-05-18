public class CartItem extends Product
{
    private Product product;
    private String productOptions;

    public CartItem(Product product, String productOptions)
    {
        this.product = product;
        this.productOptions = productOptions;
    }
    public Product getProduct()
    {
        return this.product;
    }
    public String getProductOptions()
    {
        return this.productOptions;
    }
    public void print()
    {
        System.out.printf("\nId: %-5s Category: %-9s Name: %-20s Price: %7.1f",
                product.getId(), product.getCategory(), product.getName(), product.getPrice());
    }
    public boolean equals(Object other)
    {
        Product otherP = (Product) other;
        return product.getId().equals(otherP.getId());
    }
}
