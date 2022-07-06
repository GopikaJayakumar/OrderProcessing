package com.demo.order.util;

import com.demo.order.exception.InvalidInputException;
import com.demo.order.transport.OrderRequestTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationUtils {

	public static void validate(OrderRequestTO request) throws InvalidInputException {
		List<String> errorFields = new ArrayList<>();
		if (StringUtils.isEmpty(request.getItemId())) {
			errorFields.add("ITEM ID");
		}

		if (StringUtils.isEmpty(request.getItemTitle())) {
			errorFields.add("Item Title");
		}

		if (StringUtils.isEmpty(request.getPhoneNumber()) || request.getPhoneNumber().length() != 10) {
			errorFields.add("phone number");
		}

		if (errorFields.size() > 0) throw new InvalidInputException(errorFields);
	}
}
