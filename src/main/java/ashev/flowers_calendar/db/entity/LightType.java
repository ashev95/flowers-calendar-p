package ashev.flowers_calendar.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LightType {

	private Long id;
	private String name;
	public LightType(String name) {
		this.name = name;
	}

}
