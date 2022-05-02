package re.jpayet.mentdb.ext.stripe;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Builder;

public class StripeManager {
	
	public static synchronized String get_paiement_session(String jsonItems, String successUrl, String cancelUrl, String secret_key) throws ParseException, StripeException {
		
		JSONArray jsonItemsArray = (JSONArray) new JSONParser().parse(jsonItems);
		
		Stripe.apiKey = secret_key;
		
		Builder builder = SessionCreateParams.builder()
			    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
			    .setMode(SessionCreateParams.Mode.PAYMENT)
			    .setSuccessUrl(successUrl)
			    .setCancelUrl(cancelUrl);
		
		for(Object o : jsonItemsArray) {
			JSONObject obj = (JSONObject) o;
			
			builder.addLineItem(
			          SessionCreateParams.LineItem.builder()
			            .setQuantity(Long.parseLong(""+obj.get("Quantity")))
			            .setPriceData(
			              SessionCreateParams.LineItem.PriceData.builder()
			                .setCurrency(""+obj.get("Currency"))
			                .setUnitAmount(Long.parseLong(""+obj.get("UnitAmount")))
			                .setProductData(
			                  SessionCreateParams.LineItem.PriceData.ProductData.builder()
			                    .setName(""+obj.get("Product"))
			                    .build())
			                .build())
			            .build());
			
		}
		
		SessionCreateParams params = builder.build();
		
		Session session = Session.create(params);
		
		return session.getRawJsonObject().toString();
		
	}

}
