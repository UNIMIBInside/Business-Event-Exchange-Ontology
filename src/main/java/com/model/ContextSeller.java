package com.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * ContextSeller
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-23T10:25:57.123Z[GMT]")
public class ContextSeller {
	/**
	 * Gets or Sets _atId
	 */
	public enum AtIdEnum {
		SELLER("schema:seller");

		private String value;

		AtIdEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static AtIdEnum fromValue(String text) {
			for (AtIdEnum b : AtIdEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("@id")
	private AtIdEnum _atId = null;

	/**
	 * Gets or Sets _atType
	 */
	public enum AtTypeEnum {
		ID("@id");

		private String value;

		AtTypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static AtTypeEnum fromValue(String text) {
			for (AtTypeEnum b : AtTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("@type")
	private AtTypeEnum _atType = null;

	@JsonProperty("@context")
	private ContextSellerContext _atContext = null;

	public ContextSeller _atId(AtIdEnum _atId) {
		this._atId = _atId;
		return this;
	}

	/**
	 * Get _atId
	 * 
	 * @return _atId
	 **/
	@ApiModelProperty(value = "")

	public AtIdEnum getAtId() {
		return _atId;
	}

	public void setAtId(AtIdEnum _atId) {
		this._atId = _atId;
	}

	public ContextSeller _atType(AtTypeEnum _atType) {
		this._atType = _atType;
		return this;
	}

	/**
	 * Get _atType
	 * 
	 * @return _atType
	 **/
	@ApiModelProperty(value = "")

	public AtTypeEnum getAtType() {
		return _atType;
	}

	public void setAtType(AtTypeEnum _atType) {
		this._atType = _atType;
	}

	public ContextSeller _atContext(ContextSellerContext _atContext) {
		this._atContext = _atContext;
		return this;
	}

	/**
	 * Get _atContext
	 * 
	 * @return _atContext
	 **/
	@ApiModelProperty(value = "")

	@Valid
	public ContextSellerContext getAtContext() {
		return _atContext;
	}

	public void setAtContext(ContextSellerContext _atContext) {
		this._atContext = _atContext;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContextSeller contextSeller = (ContextSeller) o;
		return Objects.equals(this._atId, contextSeller._atId) && Objects.equals(this._atType, contextSeller._atType)
				&& Objects.equals(this._atContext, contextSeller._atContext);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_atId, _atType, _atContext);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContextSeller {\n");

		sb.append("    _atId: ").append(toIndentedString(_atId)).append("\n");
		sb.append("    _atType: ").append(toIndentedString(_atType)).append("\n");
		sb.append("    _atContext: ").append(toIndentedString(_atContext)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
