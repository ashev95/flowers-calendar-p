package ashev.flowers_calendar.db.entity;

import ashev.flowers_calendar.db.util.HibernateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.Set;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flower {

	@Getter private Long id;
	@Getter private String name;
	@Getter private String description;
	@Getter private String waterAmount;
	@Getter private String comment;
	@Getter private LightType lightType;
	@Getter private Blob image;
	@Getter private String imageExt;
	@Getter private Set<Calendar> calendars;

	private transient byte[] imageBytes;

	public Flower(String name, String description, String waterAmount, String comment, LightType lightType, Blob image,
				  String imageExt) {
		this.name = name;
		this.description = description;
		this.waterAmount = waterAmount;
		this.comment = comment;
		this.lightType = lightType;
		this.image = image;
		this.imageExt = imageExt;
	}

	public byte[] getImageBytes() throws Exception {
		if (image == null) {
			return null;
		} else if (imageBytes == null) {
			imageBytes = HibernateUtil.readBytesFromBlobWithFree(image);
		}
		return imageBytes;
	}

}
