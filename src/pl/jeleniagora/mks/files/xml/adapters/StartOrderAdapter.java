package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.start.order.FilOrder;
import pl.jeleniagora.mks.start.order.SimpleOrder;
import pl.jeleniagora.mks.start.order.StartOrderInterface;

public class StartOrderAdapter extends XmlAdapter<String, StartOrderInterface> {

	@Override
	public String marshal(StartOrderInterface arg0) throws Exception {
		return arg0.toString();
	}

	@Override
	public StartOrderInterface unmarshal(String arg0) throws Exception {
		if (arg0.equals("SIMPLE_ORDER")) {
			return new SimpleOrder();
		}
		else if (arg0.equals("FIL_ORDER")) {
			return new FilOrder();
		}
		return null;
	}

}
