package ashev.flowers_calendar.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {

	private Long id;
	private Flower flower;
	private Date pourDate;
	private String note;

	public Calendar(Flower flower, Date pourDate, String note) {
		this.flower = flower;
		this.pourDate = pourDate;
		this.note = note;
	}

}
