package com.demo.order.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("message")
	private String message = null;

	@JsonProperty("root_cause")
	private String rootCause = null;
}

