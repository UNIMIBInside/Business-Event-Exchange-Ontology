package it.disco.unimib.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Array of events extendend with json-ld context
 */
@ApiModel(description = "Array of events extendend with json-ld context")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-23T10:25:57.123Z[GMT]")
public class EventsArray {

	@JsonProperty("eventArray")
	@Valid
	private List<Event> eventArray = null;

	/**
	 * Get _atContext
	 * 
	 * @return _atContext
	 **/
	@ApiModelProperty(value = "")

	public EventsArray eventArray(List<Event> eventArray) {
		this.eventArray = eventArray;
		return this;
	}

	public EventsArray addEventArrayItem(Event eventArrayItem) {
		if (this.eventArray == null) {
			this.eventArray = new ArrayList<Event>();
		}
		this.eventArray.add(eventArrayItem);
		return this;
	}

	/**
	 * Get eventArray
	 * 
	 * @return eventArray
	 **/
	@ApiModelProperty(value = "")
	@Valid
	public List<Event> getEventArray() {
		return eventArray;
	}

	public void setEventArray(List<Event> eventArray) {
		this.eventArray = eventArray;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EventsArray eventsArray = (EventsArray) o;
		return Objects.equals(this.eventArray, eventsArray.eventArray);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventArray);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EventsArray {\n");

		sb.append("    eventArray: ").append(toIndentedString(eventArray)).append("\n");
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
