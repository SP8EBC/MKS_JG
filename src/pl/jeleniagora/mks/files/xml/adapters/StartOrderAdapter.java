package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.start.order.StartOrderInterface;

public class StartOrderAdapter extends XmlAdapter<String, StartOrderInterface> {

	@Override
	public String marshal(StartOrderInterface arg0) throws Exception {
		return arg0.toString();
	}

	@Override
	public StartOrderInterface unmarshal(String arg0) throws Exception {
		return null;
	}

}
