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
public class FlowerRow {
    private Long id;
    private String name;
    private Date pourDate;
}
