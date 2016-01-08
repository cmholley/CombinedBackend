package dash.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SimpleDateAdapter extends XmlAdapter<String, Date> {

	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mmZ";
	private SimpleDateFormat dateFormat;

	public SimpleDateAdapter() {
		super();

		dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		Date newdate = null;
		try {
			newdate = dateFormat.parse(v);
		} catch (ParseException e) {
			return null;
		}
		return newdate;

	}

	@Override
	public String marshal(Date v) throws Exception {
		return dateFormat.format(v);
	}

}
