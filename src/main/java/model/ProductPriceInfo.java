package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPriceInfo implements Comparable<ProductPriceInfo>{
    private Double regularPrice;
    private Double price;
    private Integer discount;
    private String currency;

    @Override
    public int compareTo(ProductPriceInfo o) {
        double tPrice = (this.regularPrice!=null)?this.regularPrice:this.price;
        double oPrice = (o.regularPrice!=null)?o.regularPrice:o.price;
        return Double.compare(tPrice,oPrice);
    }


}
