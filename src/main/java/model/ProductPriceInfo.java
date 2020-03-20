package model;

import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class ProductPriceInfo implements Comparable<ProductPriceInfo>{
    private Double regularPrice;
    private Double price;
    private Integer discount;
    private String currency;

    @Override
    public int compareTo(ProductPriceInfo o) {
        if(Objects.isNull(o)){
            throw new RuntimeException("Can't compare with null");
        }
        double tPrice = (this.regularPrice!=null)?this.regularPrice:this.price;

        double oPrice = (o.regularPrice!=null)?o.regularPrice:o.price;

        return Double.compare(tPrice,oPrice);
    }


}
