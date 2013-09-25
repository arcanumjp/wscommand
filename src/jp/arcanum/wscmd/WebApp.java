package jp.arcanum.wscmd;

import jp.arcanum.wscmd.page.top.TopPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class WebApp extends WebApplication{



	@Override
	protected void init() {
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return TopPage.class;
	}



}
